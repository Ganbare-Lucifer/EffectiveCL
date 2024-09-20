package org.ladysnake.effective.core.mixin.feedbacking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.sound.SoundCategory;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class FeedbackingParryFireballEffect {
	@Shadow
	public abstract boolean isMainPlayer();

	@Inject(method = "attack", at = @At("HEAD"))
	public void attack(Entity target, CallbackInfo ci) {
		if (EffectiveConfig.ultrakill && this.isMainPlayer() && target instanceof ExplosiveProjectileEntity) {
			MinecraftClient.getInstance().player.playSound(Effective.PARRY);

			Effective.freezeFrames = 5;
		}
	}
}
