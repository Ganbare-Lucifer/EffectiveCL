package org.ladysnake.effective.core.render.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.ladysnake.effective.core.Effective;

public class SplashBottomRimModel<T extends Entity> extends EntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Effective.id("splash_bottom_rim"), "main");
	private final ModelPart splash;

	public SplashBottomRimModel(ModelPart root) {
		this.splash = root.getChild("splash_rim");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("splash_rim", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F), ModelTransform.pivot(0.0F, 0.0f, 0.0F));

		return TexturedModelData.of(modelData, 48, 28);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		splash.render(matrices, vertices, light, overlay, color);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}
