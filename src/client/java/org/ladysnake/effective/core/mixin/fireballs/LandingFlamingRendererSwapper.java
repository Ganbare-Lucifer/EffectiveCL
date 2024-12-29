package org.ladysnake.effective.core.mixin.fireballs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.boss.dragon.phase.LandingPhase;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LandingPhase.class)
public class LandingFlamingRendererSwapper {
	@WrapOperation(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	private void effective$swapDragonBreathParticles(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> voidOperation) {
		if (EffectiveConfig.improvedDragonFireballsAndBreath) {
//			WorldParticleBuilder.create(Effective.DRAGON_BREATH)
//				.enableForcedSpawn()
//				.setSpinData(SpinParticleData.create((float) (world.EffectiveUtils.getRandomFloatOrNegative(this.random) / 5f)).build())
//				.setScaleData(GenericParticleData.create(1f, 0f).setEasing(Easing.CIRC_OUT).build())
//				.setTransparencyData(GenericParticleData.create(0.2f).build())
//				.setColorData(
//					ColorParticleData.create(new Color(0xD21EFF), new Color(0x7800FF))
//						.setEasing(Easing.CIRC_OUT)
//						.build()
//				)
//				.enableNoClip()
//				.setLifetime(40)
//				.setMotion((float) velocityX, (float) velocityY + 0.05f, (float) velocityZ)
//				.spawn(world, x, y, z);
		} else {
			voidOperation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}
}
