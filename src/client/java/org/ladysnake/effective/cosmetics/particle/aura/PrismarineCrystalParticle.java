package org.ladysnake.effective.cosmetics.particle.aura;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PrismarineCrystalParticle extends SpriteBillboardParticle {
	private static final Random RANDOM = new Random();
	protected final float rotationFactor;
	private final int variant = RANDOM.nextInt(3);
	private final SpriteProvider spriteProvider;
	private final float groundOffset;

	public PrismarineCrystalParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.spriteProvider = spriteProvider;

		this.scale *= 1f + RANDOM.nextFloat();
		this.maxAge = ThreadLocalRandom.current().nextInt(400, 1201); // live between 20 seconds and one minute
		this.collidesWithWorld = true;
		this.setSprite(spriteProvider.getSprite(variant, 2));

		if (velocityY == 0f && velocityX == 0f && velocityZ == 0f) {
			this.alpha = 0f;
		}

		this.velocityX = random.nextFloat() * 0.01d;
		this.velocityY = -random.nextFloat() * 0.01d;
		this.velocityZ = random.nextFloat() * 0.01d;

		this.groundOffset = RANDOM.nextFloat() / 100f + 0.001f;

		this.rotationFactor = ((float) Math.random() - 0.5F) * 0.01F;
		this.angle = random.nextFloat() * 360f;
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
			if (this.onGround) {
				Vec3f2.rotate(new Quaternionf(90f, 0f, quaternion2.z(), 0));
			} else {
				Vec3f2.rotate(quaternion2);
			}
			Vec3f2.mul(j);
			Vec3f2.add(f, g + this.groundOffset, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int l = LightmapTextureManager.MAX_LIGHT_COORDINATE;

		vertexConsumer.vertex(Vec3fs[0].x(), Vec3fs[0].y(), Vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(l);
		vertexConsumer.vertex(Vec3fs[1].x(), Vec3fs[1].y(), Vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(l);
		vertexConsumer.vertex(Vec3fs[2].x(), Vec3fs[2].y(), Vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(l);
		vertexConsumer.vertex(Vec3fs[3].x(), Vec3fs[3].y(), Vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(l);
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		if (this.age++ < this.maxAge) {
			this.alpha = Math.min(1f, this.alpha + 0.01f);
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
			this.move(this.velocityX, this.velocityY, this.velocityZ);
		} else {
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= 0.9D;
			this.velocityY = -0.9D;
			this.velocityZ *= 0.9D;
		}

		if (this.age >= this.maxAge) {
			this.alpha = Math.max(0f, this.alpha - 0.01f);

			if (this.alpha <= 0f) {
				this.markDead();
			}
		}

		this.red = 0.8f + (float) Math.sin(this.age / 100f) * 0.2f;
//        this.blue = 0.9f + (float) Math.cos(this.age/100f) * 0.1f;

		this.prevAngle = this.angle;
		if (this.onGround) {
			this.velocityX = 0;
			this.velocityY = 0;
			this.velocityZ = 0;
		}

		if (this.velocityY != 0) {
			this.angle += Math.PI * Math.sin(rotationFactor * this.age) / 2;
		}
	}


	public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new PrismarineCrystalParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
