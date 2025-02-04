package org.ladysnake.effective.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.utils.EffectiveUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ParticleSpawningClientWorldMixin extends World {
	@Shadow
	@Final
	private WorldRenderer worldRenderer;

	protected ParticleSpawningClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
		at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 10), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50)).mutableCopy();
		BlockPos.Mutable pos2 = pos.mutableCopy();
		RegistryEntry<Biome> biome = this.getBiome(pos);

		// FIREFLIES
		if (EffectiveConfig.fireflyDensity > 0) {
//			FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome.getKey().get());
//			if (fireflySpawnSetting != null) {
//				if (random.nextFloat() * 250f <= fireflySpawnSetting.spawnChance() * EffectiveConfig.fireflyDensity && pos.getY() > this.getSeaLevel()) {
//					for (int y = this.getSeaLevel(); y <= this.getSeaLevel() * 2; y++) {
//						pos.setY(y);
//						pos2.setY(y-1);
//						boolean canSpawnFirefly = FireflyParticle.canFlyThroughBlock(this, pos, this.getBlockState(pos)) && !FireflyParticle.canFlyThroughBlock(this, pos2, this.getBlockState(pos2));
//
//						if (canSpawnFirefly) {
//							WorldParticleBuilder.create(Effective.FIREFLY)
//								.enableForcedSpawn()
//								.setColorData(ColorParticleData.create(fireflySpawnSetting.color(), fireflySpawnSetting.color()).build())
//								.setScaleData(GenericParticleData.create(0.05f + random.nextFloat() * 0.10f).build())
//								.setLifetime(ThreadLocalRandom.current().nextInt(40, 120))
//								.setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
//								.spawn(this, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat() * 5f, pos.getZ() + random.nextFloat());
//							break;
//						}
//					}
//				}
//			}
		}

		pos = blockPos.add(MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 25), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50)).mutableCopy();

		// WILL O' WISP
		if (EffectiveConfig.willOWispDensity > 0) {
			if (biome.matchesKey(BiomeKeys.SOUL_SAND_VALLEY)) {
				if (random.nextFloat() * 100f <= 0.01f * EffectiveConfig.willOWispDensity) {
					if (this.getBlockState(pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
						this.addParticle(Effective.WILL_O_WISP, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
					}
				}
			}
		}

		// EYES IN THE DARK
		if ((EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.ALWAYS || (EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.HALLOWEEN && LocalDate.now().getMonth() == Month.OCTOBER))
			&& random.nextFloat() <= 0.00002f) {
			this.addParticle(Effective.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}
}
