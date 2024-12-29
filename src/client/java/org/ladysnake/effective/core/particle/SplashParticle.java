package org.ladysnake.effective.core.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.particle.types.SplashParticleType;
import org.ladysnake.effective.core.render.entity.model.SplashBottomModel;
import org.ladysnake.effective.core.render.entity.model.SplashBottomRimModel;
import org.ladysnake.effective.core.render.entity.model.SplashModel;
import org.ladysnake.effective.core.render.entity.model.SplashRimModel;
import org.ladysnake.effective.core.utils.EffectiveUtils;

import java.awt.*;

public class SplashParticle extends Particle {
	static final int MAX_FRAME = 12;
	public float widthMultiplier;
	public float heightMultiplier;
	public int wave1End;
	public int wave2Start;
	public int wave2End;
	public int waterColor = -1;
	Model waveModel;
	Model waveBottomModel;
	Model waveRimModel;
	Model waveBottomRimModel;
	BlockPos blockPos;

	protected SplashParticle(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
		this.waveModel = new SplashModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashModel.MODEL_LAYER));
		this.waveBottomModel = new SplashBottomModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashBottomModel.MODEL_LAYER));
		this.waveRimModel = new SplashRimModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashRimModel.MODEL_LAYER));
		this.waveBottomRimModel = new SplashBottomRimModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashBottomRimModel.MODEL_LAYER));
		this.gravityStrength = 0.0F;
		this.widthMultiplier = 0f;
		this.heightMultiplier = 0f;

		this.wave1End = 12;
		this.wave2Start = 7;
		this.wave2End = 24;
		this.blockPos = BlockPos.ofFloored(x, y, z);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		// first splash
		if (age <= this.wave1End) {
			drawSplash(Math.round((this.age / (float) this.wave1End) * MAX_FRAME), camera, tickDelta);
		}
		// second splash
		if (age >= this.wave2Start) {
			drawSplash(Math.round(((float) (this.age - wave2Start) / (float) (this.wave2End - this.wave2Start)) * MAX_FRAME), camera, tickDelta, new Vector3f(0.5f, 2, 0.5f));
		}
	}

	private void drawSplash(int frame, Camera camera, float tickDelta, Vector3f multiplier) {
		if (waterColor == -1) {
			waterColor = BiomeColors.getWaterColor(world, BlockPos.ofFloored(this.x, this.y, this.z));
		}
		float r = (float) (waterColor >> 16 & 0xFF) / 255.0f;
		float g = (float) (waterColor >> 8 & 0xFF) / 255.0f;
		float b = (float) (waterColor & 0xFF) / 255.0f;
		waterColor = new Color(r, g, b, 1.0f).getRGB();

		Identifier texture = Effective.id("textures/entity/splash/splash_" + MathHelper.clamp(frame, 0, MAX_FRAME) + ".png");
		RenderLayer layer = RenderLayer.getEntityTranslucent(texture);
		Identifier rimTexture = Effective.id("textures/entity/splash/splash_rim_" + MathHelper.clamp(frame, 0, MAX_FRAME) + ".png");
		RenderLayer rimLayer = RenderLayer.getEntityTranslucent(rimTexture);

		// splash matrices
		MatrixStack modelMatrix = getMatrixStackFromCamera(camera, tickDelta);
		modelMatrix.scale(widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z());
		modelMatrix.translate(0, -1, 0);
		MatrixStack modelBottomMatrix = getMatrixStackFromCamera(camera, tickDelta);
		modelBottomMatrix.scale(widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z());
		modelBottomMatrix.translate(0, 0.001, 0);

		// splash bottom matrices
		MatrixStack modelRimMatrix = getMatrixStackFromCamera(camera, tickDelta);
		modelRimMatrix.scale(widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z());
		modelRimMatrix.translate(0, -1, 0);
		MatrixStack modelRimBottomMatrix = getMatrixStackFromCamera(camera, tickDelta);
		modelRimBottomMatrix.scale(widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(), widthMultiplier * multiplier.z());
		modelRimBottomMatrix.translate(0, 0.001, 0);

		int light = this.getBrightness(tickDelta);
		int rimLight = this.getRimBrightness(tickDelta);

		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

		VertexConsumer modelConsumer = immediate.getBuffer(layer);
		this.waveModel.render(modelMatrix, modelConsumer, light, OverlayTexture.DEFAULT_UV, waterColor);
		this.waveBottomModel.render(modelBottomMatrix, modelConsumer, light, OverlayTexture.DEFAULT_UV, waterColor);

		VertexConsumer rimModelConsumer = immediate.getBuffer(rimLayer);
		int rimColor = this.getRimColor(this.blockPos);
		this.waveRimModel.render(modelRimMatrix, rimModelConsumer, rimLight, OverlayTexture.DEFAULT_UV, rimColor);
		this.waveBottomRimModel.render(modelRimBottomMatrix, rimModelConsumer, rimLight, OverlayTexture.DEFAULT_UV, rimColor);

		immediate.draw();
	}

	public int getRimBrightness(float tickDelta) {
		return this.getBrightness(tickDelta);
	}

	public int getRimColor(BlockPos pos) {
		return 0xFFFFFFFF;
	}

	private void drawSplash(int frame, Camera camera, float tickDelta) {
		drawSplash(frame, camera, tickDelta, new Vector3f(1, 1, 1));
	}

	private MatrixStack getMatrixStackFromCamera(Camera camera, float tickDelta) {
		Vec3d cameraPos = camera.getPos();
		float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
		float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
		float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());

		MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(x, y, z);
		return matrixStack;
	}

	@Override
	public void tick() {
		if (this.widthMultiplier == 0f) {
			this.markDead();
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.widthMultiplier *= 1.03f;

		if (this.age++ >= this.wave2End) {
			this.markDead();
		}

		if (this.age == 1) {
			for (int i = 0; i < this.widthMultiplier * 10f; i++) {
				this.world.addParticle(Effective.DROPLET, this.x + (EffectiveUtils.getRandomFloatOrNegative(this.random) * this.widthMultiplier / 10f), this.y, this.z + (EffectiveUtils.getRandomFloatOrNegative(this.random) * this.widthMultiplier / 10f), EffectiveUtils.getRandomFloatOrNegative(this.random) / 10f * this.widthMultiplier / 2.5f, random.nextFloat() / 10f + this.heightMultiplier / 2.8f, EffectiveUtils.getRandomFloatOrNegative(this.random) / 10f * this.widthMultiplier / 2.5f);
			}
		} else if (this.age == wave2Start) {
			for (int i = 0; i < this.widthMultiplier * 5f; i++) {
				this.world.addParticle(Effective.DROPLET, this.x + (EffectiveUtils.getRandomFloatOrNegative(this.random) * this.widthMultiplier / 10f * .5f), this.y, this.z + (EffectiveUtils.getRandomFloatOrNegative(this.random) * this.widthMultiplier / 10f * .5f), EffectiveUtils.getRandomFloatOrNegative(this.random) / 10f * this.widthMultiplier / 5f, random.nextFloat() / 10f + this.heightMultiplier / 2.2f, EffectiveUtils.getRandomFloatOrNegative(this.random) / 10f * this.widthMultiplier / 5f);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		public Factory(SpriteProvider spriteProvider) {
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			SplashParticle instance = new SplashParticle(world, x, y, z);
			if (parameters instanceof SplashParticleType splashParameters && splashParameters.initialData != null) {
				final float width = (float) splashParameters.initialData.width() * 2;
				instance.widthMultiplier = width;
				instance.heightMultiplier = (float) splashParameters.initialData.velocityY() * width;
				instance.wave1End = 10 + Math.round(width * 1.2f);
				instance.wave2Start = 6 + Math.round(width * 0.7f);
				instance.wave2End = 20 + Math.round(width * 2.4f);
			}
			return instance;
		}
	}
}
