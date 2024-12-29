package org.ladysnake.effective.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ladysnake.effective.core.gui.ParryScreen;
import org.ladysnake.effective.core.particle.*;
import org.ladysnake.effective.core.particle.types.SplashParticleType;
import org.ladysnake.effective.core.render.entity.model.SplashBottomModel;
import org.ladysnake.effective.core.render.entity.model.SplashBottomRimModel;
import org.ladysnake.effective.core.render.entity.model.SplashModel;
import org.ladysnake.effective.core.render.entity.model.SplashRimModel;
import org.ladysnake.effective.core.world.RenderedHypnotizingEntities;
import org.ladysnake.effective.core.world.WaterfallCloudGenerators;
import org.ladysnake.satin.api.event.EntitiesPreRenderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;

public class Effective implements ClientModInitializer {
	public static final String MODID = "effective";
	// rainbow shader for jeb glow squid
	public static final ManagedCoreShader RAINBOW_SHADER = ShaderEffectManager.getInstance().manageCoreShader(Effective.id("jeb"));
	private static final Uniform1f uniformSTimeJeb = RAINBOW_SHADER.findUniform1f("Time");

	// squid hypno shader
	private static final ManagedShaderEffect HYPNO_SHADER = ShaderEffectManager.getInstance()
		.manage(Effective.id("shaders/post/hypnotize.json"));
	private static final Uniform1f intensityHypno = HYPNO_SHADER.findUniform1f("Intensity");
	private static final Uniform1f sTimeHypno = HYPNO_SHADER.findUniform1f("STime");
	private static final Uniform1f rainbowHypno = HYPNO_SHADER.findUniform1f("Rainbow");
	// freeze frames for feedbacking
	public static int freezeFrames = -1;
	// particle types
	public static SplashParticleType SPLASH;
	public static SimpleParticleType DROPLET;
	public static SimpleParticleType RIPPLE;
	public static SplashParticleType GLOW_SPLASH;
	public static SimpleParticleType GLOW_DROPLET;
	public static SimpleParticleType GLOW_RIPPLE;
	//	public static AllayTwinkleParticleType ALLAY_TWINKLE;
	public static SimpleParticleType CHORUS_PETAL;
	public static SimpleParticleType EYES;
	public static SimpleParticleType WILL_O_WISP;

	// lodestone particles
//	public static LodestoneWorldParticleType PIXEL = new LodestoneWorldParticleType();
//	public static LodestoneWorldParticleType WISP = new LodestoneWorldParticleType();
//	public static FlameParticleType FLAME = new FlameParticleType();
//	public static FlameParticleType DRAGON_BREATH = new FlameParticleType();
//	public static BubbleParticleType BUBBLE = new BubbleParticleType();
//	public static WaterfallCloudParticleType WATERFALL_CLOUD = new WaterfallCloudParticleType();
//	public static MistParticleType MIST = new MistParticleType();
//	public static FireflyParticleType FIREFLY = new FireflyParticleType();
	// sound events
	public static SoundEvent AMBIENCE_WATERFALL = SoundEvent.of(Effective.id("ambience.waterfall"));
	public static SoundEvent PARRY = SoundEvent.of(Effective.id("entity.parry"));
	private static int ticksJeb;

	public static boolean isNightTime(World world) {
		return world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491;
	}

//	public static void wheresTheHamiltonProductMojangski(Quaternion one, Quaternion two) {
//		float f = one.x;
//		float g = one.y;
//		float h = one.z;
//		float i = one.w;
//		float j = two.x;
//		float k = two.y;
//		float l = two.z;
//		float m = two.w;
//		one.set(i * j + f * m + g * l - h * k, i * k - f * l + g * m + h * j, i * l + f * k - g * j + h * m, i * m - f * j - g * k - h * l);
//	}

	public static Identifier id(String string) {
		return Identifier.of(MODID, string);
	}

	@Override
	public void onInitializeClient() {
		// load config
		EffectiveConfig.init(MODID, EffectiveConfig.class);

		// register model layers
		EntityModelLayerRegistry.registerModelLayer(SplashModel.MODEL_LAYER, SplashModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashBottomModel.MODEL_LAYER, SplashBottomModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashRimModel.MODEL_LAYER, SplashRimModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashBottomRimModel.MODEL_LAYER, SplashBottomRimModel::getTexturedModelData);

		// particles
		SPLASH = Registry.register(Registries.PARTICLE_TYPE, Effective.id("splash"), new SplashParticleType(true));
		ParticleFactoryRegistry.getInstance().register(SPLASH, SplashParticle.Factory::new);
		DROPLET = Registry.register(Registries.PARTICLE_TYPE, Effective.id("droplet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(DROPLET, DropletParticle.Factory::new);
		RIPPLE = Registry.register(Registries.PARTICLE_TYPE, Effective.id("ripple"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(RIPPLE, RippleParticle.Factory::new);
		GLOW_SPLASH = Registry.register(Registries.PARTICLE_TYPE, Effective.id("glow_splash"), new SplashParticleType(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_SPLASH, GlowSplashParticle.Factory::new);
		GLOW_DROPLET = Registry.register(Registries.PARTICLE_TYPE, Effective.id("glow_droplet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_DROPLET, GlowDropletParticle.Factory::new);
		GLOW_RIPPLE = Registry.register(Registries.PARTICLE_TYPE, Effective.id("glow_ripple"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_RIPPLE, GlowRippleParticle.Factory::new);
//		ALLAY_TWINKLE = Registry.register(Registries.PARTICLE_TYPE, Effective.id("allay_twinkle"), new AllayTwinkleParticleType());
//		ParticleFactoryRegistry.getInstance().register(ALLAY_TWINKLE, AllayTwinkleParticleType.Factory::new);
		CHORUS_PETAL = Registry.register(Registries.PARTICLE_TYPE, Effective.id("chorus_petal"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(CHORUS_PETAL, ChorusPetalParticle.Factory::new);
		EYES = Registry.register(Registries.PARTICLE_TYPE, Effective.id("eyes"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(EYES, EyesParticle.Factory::new);
		WILL_O_WISP = Registry.register(Registries.PARTICLE_TYPE, Effective.id("will_o_wisp"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(WILL_O_WISP, fabricSpriteProvider -> new WillOWispParticle.Factory(fabricSpriteProvider, Effective.id("textures/entity/will_o_wisp.png"), 0.1f, 0.75f, 1.0f, 0.0f, 0.1f, 1.0f));

		// lodestone particles
//		ParticleFactoryRegistry.getInstance().register(PIXEL, LodestoneWorldParticleType.Factory::new);
//		PIXEL = Registry.register(Registries.PARTICLE_TYPE, Effective.id("pixel"), PIXEL);
//		ParticleFactoryRegistry.getInstance().register(WISP, LodestoneWorldParticleType.Factory::new);
//		WISP = Registry.register(Registries.PARTICLE_TYPE, Effective.id("wisp"), WISP);
//		ParticleFactoryRegistry.getInstance().register(FLAME, FlameParticleType.Factory::new);
//		FLAME = Registry.register(Registries.PARTICLE_TYPE, Effective.id("flame"), FLAME);
//		ParticleFactoryRegistry.getInstance().register(DRAGON_BREATH, FlameParticleType.Factory::new);
//		DRAGON_BREATH = Registry.register(Registries.PARTICLE_TYPE, Effective.id("dragon_breath"), DRAGON_BREATH);
//		ParticleFactoryRegistry.getInstance().register(BUBBLE, BubbleParticleType.Factory::new);
//		BUBBLE = Registry.register(Registries.PARTICLE_TYPE, Effective.id("bubble"), BUBBLE);
//		ParticleFactoryRegistry.getInstance().register(WATERFALL_CLOUD, WaterfallCloudParticleType.Factory::new);
//		WATERFALL_CLOUD = Registry.register(Registries.PARTICLE_TYPE, Effective.id("waterfall_cloud"), WATERFALL_CLOUD);
//		ParticleFactoryRegistry.getInstance().register(MIST, MistParticleType.Factory::new);
//		MIST = Registry.register(Registries.PARTICLE_TYPE, Effective.id("mist"), MIST);
//		ParticleFactoryRegistry.getInstance().register(FIREFLY, FireflyParticleType.Factory::new);
//		FIREFLY = Registry.register(Registries.PARTICLE_TYPE, Effective.id("firefly"), FIREFLY);

		// sound events
		AMBIENCE_WATERFALL = Registry.register(Registries.SOUND_EVENT, AMBIENCE_WATERFALL.getId(), AMBIENCE_WATERFALL);
		PARRY = Registry.register(Registries.SOUND_EVENT, PARRY.getId(), PARRY);

		// events
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			WaterfallCloudGenerators.tick();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			WaterfallCloudGenerators.generators.clear();
			WaterfallCloudGenerators.particlesToSpawn.clear();
		});

		// hypnotizing glow squids
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			if (EffectiveConfig.shouldGlowSquidsHypnotize() && (!RenderedHypnotizingEntities.GLOWSQUIDS.isEmpty() || RenderedHypnotizingEntities.lockedIntensityTimer > 0 || RenderedHypnotizingEntities.lookIntensity > 0)) {
				double bestLookIntensity = 0;
				GlowSquidEntity bestSquid = null;

				for (GlowSquidEntity glowsquid : RenderedHypnotizingEntities.GLOWSQUIDS) {
					if (glowsquid.getPos().squaredDistanceTo(MinecraftClient.getInstance().player.getPos()) <= 49f) {
						Vec3d toSquid = glowsquid.getPos().subtract(MinecraftClient.getInstance().player.getPos()).normalize();
						double lookIntensity = toSquid.dotProduct(MinecraftClient.getInstance().player.getRotationVec(tickDelta)); // * 1 / Math.max(2, Math.sqrt(glowsquid.getPos().squaredDistanceTo(MinecraftClient.getInstance().player.getPos())) - 5f); // 1 = looking straight at squid
						if (lookIntensity > bestLookIntensity && MinecraftClient.getInstance().player.canSee(glowsquid)) {
							bestLookIntensity = lookIntensity;
							bestSquid = glowsquid;
						}
					}
				}

				RenderedHypnotizingEntities.lookIntensityGoal = bestLookIntensity;

				if (!MinecraftClient.getInstance().isPaused() && RenderedHypnotizingEntities.lockedIntensityTimer >= 0) {
					RenderedHypnotizingEntities.lookIntensityGoal = 1.0f;
					RenderedHypnotizingEntities.lookIntensity += 0.001f;
					RenderedHypnotizingEntities.lockedIntensityTimer--;
				}

				if (RenderedHypnotizingEntities.lookIntensity < RenderedHypnotizingEntities.lookIntensityGoal) {
					RenderedHypnotizingEntities.lookIntensity += 0.0005f;
				} else {
					RenderedHypnotizingEntities.lookIntensity -= 0.001f;
				}
				RenderedHypnotizingEntities.lookIntensity = MathHelper.clamp(RenderedHypnotizingEntities.lookIntensity, 0, 0.5f);

				if (bestSquid != null) {
					if (bestSquid.hasCustomName() && bestSquid.getCustomName().getString().equals("jeb_")) {
						rainbowHypno.set(1.0f);
					} else {
						rainbowHypno.set(0.0f);
					}
				}
				intensityHypno.set((float) Math.max(0, RenderedHypnotizingEntities.lookIntensity));
				sTimeHypno.set((MinecraftClient.getInstance().world.getTime() + tickDelta) / 20);
				HYPNO_SHADER.render(tickDelta);

				if (MinecraftClient.getInstance().player.age % 20 == 0 && !MinecraftClient.getInstance().isPaused()) {
					MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_GLOW_SQUID_AMBIENT, (float) RenderedHypnotizingEntities.lookIntensity, (float) RenderedHypnotizingEntities.lookIntensity);
				}

				// look at squid
				if (EffectiveConfig.glowSquidHypnotize == EffectiveConfig.GlowSquidHypnoOptions.ATTRACT && bestSquid != null && !MinecraftClient.getInstance().isPaused()) {
					ClientPlayerEntity player = MinecraftClient.getInstance().player;

					Vec3d target = bestSquid.getPos();
					Vec3d vec3d = player.getPos();
					double d = target.x - vec3d.x;
					double e = target.y - vec3d.y - 1;
					double f = target.z - vec3d.z;
					double g = Math.sqrt(d * d + f * f);
					float currentPitch = MathHelper.wrapDegrees(player.getPitch(tickDelta));
					float currentYaw = MathHelper.wrapDegrees(player.getYaw(tickDelta));
					float desiredPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
					float desiredYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f);

					Vec2f rotationChange = new Vec2f(
						MathHelper.wrapDegrees(desiredPitch - currentPitch),
						MathHelper.wrapDegrees(desiredYaw - currentYaw)
					);

					Vec2f rotationStep = rotationChange.normalize().multiply((float) RenderedHypnotizingEntities.lookIntensity * 10f * (MathHelper.clamp(rotationChange.length(), 0, 10) / 10f));

					player.setPitch(player.getPitch(tickDelta) + rotationStep.x);
					player.setYaw(player.getYaw(tickDelta) + rotationStep.y);
				}

				RenderedHypnotizingEntities.GLOWSQUIDS.clear();
			}
		});

		// jeb rainbow glow squids
		ClientTickEvents.END_CLIENT_TICK.register(client -> ticksJeb++);
		EntitiesPreRenderCallback.EVENT.register((camera, frustum, tickDelta) -> uniformSTimeJeb.set((ticksJeb + tickDelta) * 0.05f));

		// tick freeze frames for feedbacking
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (freezeFrames > 0) {
				freezeFrames--;

				boolean bl = client.isIntegratedServerRunning() && !client.getServer().isRemote();
				if (bl) {
					client.setScreen(new ParryScreen());
				}
			} else if (freezeFrames == 0) {
				client.setScreen(null);
				freezeFrames = -1;
			}
		});
	}
}
