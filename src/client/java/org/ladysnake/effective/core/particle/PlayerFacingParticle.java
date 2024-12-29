//package org.ladysnake.effective.core.particle;
//
//import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
//import net.minecraft.client.render.Camera;
//import net.minecraft.client.render.VertexConsumer;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//import team.lodestar.lodestone.config.ClientConfig;
//import team.lodestar.lodestone.handlers.RenderHandler;
//import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
//import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
//import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
//
//public class PlayerFacingParticle extends LodestoneWorldParticle {
//	public PlayerFacingParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
//		super(world, data, spriteSet, x, y, z, xd, yd, zd);
//	}
//
//	@Override
//	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
//		VertexConsumer consumer = vertexConsumer;
//		if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue()) {
//			if (getType().equals(LodestoneWorldParticleRenderType.ADDITIVE)) {
//				consumer = RenderHandler.DELAYED_RENDER.getParticleBuffers().get(LodestoneWorldParticleRenderType.ADDITIVE);
//			}
//			if (getType().equals(LodestoneWorldParticleRenderType.TRANSPARENT)) {
//				consumer = RenderHandler.DELAYED_RENDER.getParticleBuffers().get(LodestoneWorldParticleRenderType.TRANSPARENT);
//			}
//		}
//
//		Vec3d vec3d = camera.getPos();
//		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
//		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
//		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
//		Quaternionf quaternion;
//		if (this.angle == 0.0F) {
//			quaternion = camera.getRotation();
//		} else {
//			quaternion = new Quaternionf(camera.getRotation());
//			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
//			quaternion.rotateZ(i);
//		}
//
//		Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
//		float j = this.getSize(tickDelta);
//
//		Vec3d v1 = new Vec3d(0, 0, 1);
//		Vec3d v2 = new Vec3d(this.x, this.y, this.z).subtract(camera.getPos()).normalize();
//		Vec3d a = v1.crossProduct(v2);
//		Quaternionf q = new Quaternionf(0f, (float) a.getY(), (float) a.getZ(), (float) (MathHelper.sqrt((float) ((v1.length() * v1.length()) * (v2.length() * v2.length()))) + v1.dotProduct(v2)));
//		q.normalize();
//
//		for (int k = 0; k < 4; ++k) {
//			Vector3f vec3f2 = vec3fs[k];
//			vec3f2.rotate(q);
//			vec3f2.mul(j);
//			vec3f2.add(f, g, h);
//		}
//
//		float minU = this.getMinU();
//		float maxU = this.getMaxU();
//		float minV = this.getMinV();
//		float maxV = this.getMaxV();
//		int l = this.getBrightness(tickDelta);
//
//		consumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(l);
//		consumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(l);
//		consumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(l);
//		consumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(l);
//	}
//}
