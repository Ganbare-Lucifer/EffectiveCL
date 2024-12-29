package org.ladysnake.effective.cosmetics.particle.aura;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.ladysnake.effective.core.utils.EffectiveUtils;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.particle.LegacyFireflyParticle;

import java.util.Optional;

public class TwilightLegacyFireflyParticle extends LegacyFireflyParticle {
	private final PlayerEntity owner;

	public TwilightLegacyFireflyParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider);

		this.maxAge = 20;
		this.owner = world.getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(1D), this.x, this.y, this.z);
		this.maxHeight = 2;

		Optional.ofNullable(owner).map(EffectiveCosmetics::getCosmeticData).ifPresentOrElse(
			data -> {
				this.red = data.getColor1Red() / 255f;
				this.green = data.getColor1Green() / 255f;
				this.blue = data.getColor1Blue() / 255f;
				this.nextAlphaGoal = 1f;
			},
			this::markDead
		);

		this.setPos(this.x + TwilightLegacyFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 2d, this.z + TwilightLegacyFireflyParticle.getWanderingDistance(this.random));
	}

	public static double getWanderingDistance(Random random) {
		return EffectiveUtils.getRandomFloatOrNegative(random) / 5d;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternion2;
		if (this.angle == 0.0F) {
			quaternion2 = camera.getRotation();
		} else {
			quaternion2 = new Quaternionf(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion2.rotateZ(i);
		}

		Vector3f Vec3f = new Vector3f(-1.0F, -1.0F, 0.0F);
		Vec3f.rotate(quaternion2);
		Vector3f[] Vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		for (int k = 0; k < 4; ++k) {
			Vector3f Vec3f2 = Vec3fs[k];
			Vec3f2.rotate(quaternion2);
			Vec3f2.mul(j);
			Vec3f2.add(f, g, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int l = LightmapTextureManager.MAX_LIGHT_COORDINATE;
		float a = Math.min(1f, Math.max(0f, this.alpha));

		// colored layer
		vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, minV + (maxV - minV) / 2.0F).color(this.red, this.green, this.blue, a).light(l);
		vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(maxU, minV).color(this.red, this.green, this.blue, a).light(l);
		vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV).color(this.red, this.green, this.blue, a).light(l);
		vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(minU, minV + (maxV - minV) / 2.0F).color(this.red, this.green, this.blue, a).light(l);

		// white center
		vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, maxV).color(1f, 1f, 1f, 0.5f / 100f).light(l);
		vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(maxU, minV + (maxV - minV) / 2.0F).color(1f, 1f, 1f, 0.5f / 100f).light(l);
		vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV + (maxV - minV) / 2.0F).color(1f, 1f, 1f, 0.5f / 100f).light(l);
		vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(minU, maxV).color(1f, 1f, 1f, 0.5f / 100f).light(l);
	}


	@Override
	public void tick() {
		if (owner != null) {
			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;

			// fade and die on daytime or if old enough
			if (this.age++ >= this.maxAge) {
				nextAlphaGoal = -BLINK_STEP;
				if (this.alpha < 0f) {
					this.markDead();
				}
			}

			// blinking
			if (this.alpha > nextAlphaGoal - BLINK_STEP && this.alpha < nextAlphaGoal + BLINK_STEP) {
				nextAlphaGoal = random.nextFloat();
			} else {
				if (nextAlphaGoal > this.alpha) {
					this.alpha = Math.min(this.alpha + BLINK_STEP, 1f);
				} else if (nextAlphaGoal < this.alpha) {
					this.alpha = Math.max(this.alpha - BLINK_STEP, 0f);
				}
			}

			this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

			if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
				selectBlockTarget();
			}

			Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
			double length = targetVector.length();
			targetVector = targetVector.multiply(0.025 / length);

			BlockState stateBelow = this.world.getBlockState(BlockPos.ofFloored(this.x, this.y - 0.1, this.z));
			if (!stateBelow.getBlock().canMobSpawnInside(stateBelow)) {
				velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
				velocityY = 0.05;
				velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
			} else {
				velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
				velocityY = (0.2) * velocityY + (0.1) * targetVector.y;
				velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
			}

			if (!BlockPos.ofFloored(x, y, z).equals(this.getTargetPosition())) {
				this.move(velocityX, velocityY, velocityZ);
			}
		} else {
			this.markDead();
		}
	}

	private void selectBlockTarget() {
		// Behaviour
		double groundLevel = 0;
		for (int i = 0; i < 20; i++) {
			BlockState checkedBlock = this.world.getBlockState(BlockPos.ofFloored(this.x, this.y - i, this.z));
			if (!checkedBlock.getBlock().canMobSpawnInside(checkedBlock)) {
				groundLevel = this.y - i;
			}
			if (groundLevel != 0) break;
		}

		this.xTarget = owner.getX() + EffectiveUtils.getRandomFloatOrNegative(this.random);
		this.yTarget = Math.min(Math.max(owner.getY() + EffectiveUtils.getRandomFloatOrNegative(this.random), groundLevel), groundLevel + maxHeight);
		this.zTarget = owner.getZ() + EffectiveUtils.getRandomFloatOrNegative(this.random);

		BlockPos targetPos = BlockPos.ofFloored(this.xTarget, this.yTarget, this.zTarget);
		if (this.world.getBlockState(targetPos).isFullCube(world, targetPos)
			&& this.world.getBlockState(targetPos).isSolidBlock(world, targetPos)) {
			this.yTarget += 1;
		}

		targetChangeCooldown = random.nextInt() % 100;
	}


	public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new TwilightLegacyFireflyParticle(clientWorld, d, e, f, this.spriteProvider);
		}
	}

}
