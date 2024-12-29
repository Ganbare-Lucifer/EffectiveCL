//package org.ladysnake.effective.core.particle;
//
//import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.LightType;
//import net.minecraft.world.World;
//import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
//import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
//import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
//
//import java.util.HashMap;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class FireflyParticle extends LodestoneWorldParticle {
//	protected static final float BLINK_STEP = 0.05f;
//	protected float nextAlphaGoal = 0f;
//	protected double xTarget;
//	protected double yTarget;
//	protected double zTarget;
//	protected int targetChangeCooldown = 0;
//	protected int maxHeight;
//	private BlockPos lightTarget;
//
//	public FireflyParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
//		super(world, data, spriteSet, x, y, z, xd, yd, zd);
//
//		this.maxAge = ThreadLocalRandom.current().nextInt(400, 1201); // live between 20 seconds and one minute
//		this.maxHeight = 4;
//		this.alpha = 0f;
//		this.collidesWithWorld = false;
//	}
//
//	public static boolean canFlyThroughBlock(World world, BlockPos blockPos, BlockState blockState) {
//		return !blockState.shouldSuffocate(world, blockPos) && blockState.getFluidState().isEmpty();
//	}
//
//	public void tick() {
//		this.prevPosX = this.x;
//		this.prevPosY = this.y;
//		this.prevPosZ = this.z;
//
//		// fade and die on daytime or if old enough unless fireflies can spawn any time of day
//		if ((!world.getDimension().hasFixedTime() && !EffectiveCosmetics.isNightTime(world)) || this.age++ >= this.maxAge) {
//			nextAlphaGoal = 0;
//			if (this.alpha <= 0.01f) {
//				this.markDead();
//			}
//		}
//
//		// blinking
//		if (this.alpha > nextAlphaGoal - BLINK_STEP && this.alpha < nextAlphaGoal + BLINK_STEP) {
//			nextAlphaGoal = random.nextFloat();
//		} else {
//			if (nextAlphaGoal > this.alpha) {
//				this.alpha = Math.min(this.alpha + BLINK_STEP, 1f);
//			} else if (nextAlphaGoal < this.alpha) {
//				this.alpha = Math.max(this.alpha - BLINK_STEP, 0f);
//			}
//		}
//
////		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;
//
//		if (random.nextInt(20) == 0 || (xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9) {
//			selectBlockTarget();
//		}
//
//		Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
//		double length = targetVector.length();
//		targetVector = targetVector.multiply(0.1 / length);
//
//		BlockPos blockPos = BlockPos.ofFloored(this.x, this.y - 0.1, this.z);
//		if (!canFlyThroughBlock(this.world, blockPos, this.world.getBlockState(blockPos))) {
//			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
//			velocityY = 0.05;
//			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
//		} else {
//			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
//			velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
//			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
//		}
//		if (!BlockPos.ofFloored(x, y, z).equals(this.getTargetPosition())) {
//			this.move(velocityX, velocityY, velocityZ);
//		}
//	}
//
//	private void selectBlockTarget() {
//		if (this.lightTarget == null) {
//			// Behaviour
//			double groundLevel = 0;
//			for (int i = 0; i < 20; i++) {
//				BlockPos checkedPos = BlockPos.ofFloored(this.x, this.y - i, this.z);
//				BlockState checkedBlock = this.world.getBlockState(checkedPos);
//				if (canFlyThroughBlock(this.world, checkedPos, checkedBlock)) {
//					groundLevel = this.y - i;
//				}
//				if (groundLevel != 0) break;
//			}
//
//			this.xTarget = this.x + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10;
//			this.yTarget = Math.min(Math.max(this.y + EffectiveUtils.getRandomFloatOrNegative(this.random) * 2, groundLevel), groundLevel + maxHeight);
//			this.zTarget = this.z + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10;
//
//			BlockPos targetPos = BlockPos.ofFloored(this.xTarget, this.yTarget, this.zTarget);
//			if (!canFlyThroughBlock(this.world, targetPos, this.world.getBlockState(targetPos))) {
//				this.yTarget += 1;
//			}
//
//			this.lightTarget = getMostLitBlockAround();
//		} else {
//			this.xTarget = this.lightTarget.getX() + EffectiveUtils.getRandomFloatOrNegative(this.random);
//			this.yTarget = this.lightTarget.getY() + EffectiveUtils.getRandomFloatOrNegative(this.random);
//			this.zTarget = this.lightTarget.getZ() + EffectiveUtils.getRandomFloatOrNegative(this.random);
//
//			if (this.world.getLightLevel(LightType.BLOCK, BlockPos.ofFloored(x, y, z)) > 0 && !this.world.isDay()) {
//				this.lightTarget = getMostLitBlockAround();
//			} else {
//				this.lightTarget = null;
//			}
//		}
//
////		targetChangeCooldown = random.nextInt() % 100;
//	}
//
//	public BlockPos getTargetPosition() {
//		return BlockPos.ofFloored(this.xTarget, this.yTarget + 0.5, this.zTarget);
//	}
//
//	private BlockPos getMostLitBlockAround() {
//		HashMap<BlockPos, Integer> randBlocks = new HashMap<>();
//
//		// get blocks adjacent to the fly
//		for (int x = -1; x <= 1; x++) {
//			for (int y = -1; y <= 1; y++) {
//				for (int z = -1; z <= 1; z++) {
//					BlockPos bp = BlockPos.ofFloored(this.x + x, this.y + y, this.z + z);
//					randBlocks.put(bp, this.world.getLightLevel(LightType.BLOCK, bp));
//				}
//			}
//		}
//
//		// get other random blocks to find a different light source
//		for (int i = 0; i < 15; i++) {
//			BlockPos randBP = BlockPos.ofFloored(this.x + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10, this.y + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10, this.z + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10);
//			randBlocks.put(randBP, this.world.getLightLevel(LightType.BLOCK, randBP));
//		}
//
//		return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
//	}
//}
