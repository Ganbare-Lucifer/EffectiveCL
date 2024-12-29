//package org.ladysnake.effective.core.particle;
//
//import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
//import net.minecraft.block.Blocks;
//import net.minecraft.client.world.ClientWorld;
//import net.minecraft.util.math.BlockPos;
//import team.lodestar.lodestone.systems.particle.world.FrameSetParticle;
//import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
//
//public class WaterfallCloudParticle extends FrameSetParticle {
//	public WaterfallCloudParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
//		super(world, data, spriteSet, x, y, z, xd, yd, zd);
//
//		this.setSprite(this.spriteSet.getSprite(world.random));
//	}
//
//	@Override
//	public void tick() {
//		super.tick();
//
//		this.setSpriteForAge(this.spriteSet);
//
//		this.prevPosX = this.x;
//		this.prevPosY = this.y;
//		this.prevPosZ = this.z;
//
//		if (this.onGround || (this.age > 10 && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER)) {
//			this.velocityX *= 0.5f;
//			this.velocityY *= 0.5f;
//			this.velocityZ *= 0.5f;
//		}
//
//		if (this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).isAir()) {
//			this.velocityX *= 0.9;
//			this.velocityY *= 0.9;
//			this.velocityZ *= 0.9;
//		}
//
//		this.velocityX *= 0.95f;
//		this.velocityY -= 0.02f;
//		this.velocityZ *= 0.95f;
//
//		this.move(velocityX, velocityY, velocityZ);
//	}
//}
