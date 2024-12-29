//package org.ladysnake.effective.core.mixin.water.integration.sodium;
//
//@Mixin(FluidRenderer.class)
//public class SodiumWaterfallGeneratorAdder {
//	@Inject(method = "render", at = @At("HEAD"))
//	public void effective$generateWaterfall(LevelSlice world, BlockState blockState, FluidState fluidState, BlockPos pos, BlockPos offset, TranslucentGeometryCollector translucentGeometryCollector, ChunkBuildBuffers buffers, CallbackInfo ci) {
//		WaterfallCloudGenerators.addGenerator(fluidState, pos.toImmutable());
//	}
//}
