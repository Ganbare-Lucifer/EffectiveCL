package org.ladysnake.effective.core.mixin.screenshake;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RavagerEntity.class)
public abstract class RavagerRoarScreenshakeAdder extends HostileEntity {
	protected RavagerRoarScreenshakeAdder(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "roar", at = @At("HEAD"))
	public void roar(CallbackInfo ci) {
		if (EffectiveConfig.ravagerScreenShake) {
//			ScreenshakeInstance roarScreenShake = new PositionedScreenshakeInstance(10, this.getPos(), 20f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, EffectiveConfig.screenShakeIntensity, 0.0f);
//			ScreenshakeHandler.addScreenshake(roarScreenShake);
		}
	}
}
