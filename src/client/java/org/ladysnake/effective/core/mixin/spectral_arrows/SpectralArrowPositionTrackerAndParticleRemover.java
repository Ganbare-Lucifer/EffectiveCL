package org.ladysnake.effective.core.mixin.spectral_arrows;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowPositionTrackerAndParticleRemover extends PersistentProjectileEntity {
//	@Unique
//	public final TrailPointBuilder trailPointBuilder = TrailPointBuilder.create(20);

	protected SpectralArrowPositionTrackerAndParticleRemover(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		if (EffectiveConfig.spectralArrowTrails != EffectiveConfig.TrailOptions.NONE) {
//			Vec3d position = this.getCameraPosVec(MinecraftClient.getInstance().getRenderTime()).add(0, -.1f, 0f);
//			trailPointBuilder.addTrailPoint(position);
//			trailPointBuilder.tickTrailPoints();
		}
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	public void tick(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> voidOperation) {
		if (EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.NONE) {
			voidOperation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}

//	@Override
//	public List<TrailPoint> getPastPositions() {
//		return trailPointBuilder.getTrailPoints();
//	}
}
