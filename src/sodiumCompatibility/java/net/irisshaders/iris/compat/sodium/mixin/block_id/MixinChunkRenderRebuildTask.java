package net.irisshaders.iris.compat.sodium.mixin.block_id;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadWinding;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.irisshaders.iris.compat.sodium.impl.block_context.ChunkBuildBuffersExt;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.vertices.ExtendedDataHelper;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Passes additional information indirectly to the vertex writer to support the mc_Entity and at_midBlock parts of the vertex format.
 */
@Mixin(ChunkRenderRebuildTask.class)
public class MixinChunkRenderRebuildTask { va
	private final ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();

	@Inject(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "net/minecraft/world/level/block/state/BlockState.getRenderShape()" +
			"Lnet/minecraft/world/level/block/RenderShape;"),
		locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$setLocalPos(ChunkBuildContext context,
								  CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> cir,
								  ChunkRenderData.Builder renderData, VisGraph occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers,
								  ChunkRenderCacheLocal cacheLocal,
								  WorldSlice slice, int baseX, int baseY, int baseZ, int maxX, int maxY, int maxZ,
								  BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos renderOffset,
								  int relY, int relZ, int relX, BlockState blockState) {
		if (WorldRenderingSettings.INSTANCE.shouldVoxelizeLightBlocks() && blockState.getBlock() instanceof LightBlock) {
			ChunkModelBuilder buildBuffers = buffers.get(DefaultMaterials.CUTOUT);
			((ChunkBuildBuffersExt) buffers).iris$setLocalPos(0, 0, 0);
			((ChunkBuildBuffersExt) buffers).iris$ignoreMidBlock(true);
			((ChunkBuildBuffersExt) buffers).iris$setMaterialId(blockState, (short) 0, (byte) blockState.getLightEmission());

			for (int i = 0; i < 4; i++) {
				float x = (float) ((relX & 15)) + 0.25f;
				float y = (float) ((relY & 15)) + 0.25f;
				float z = (float) ((relZ & 15)) + 0.25f;
				float u = 0;
				float v = 0;
				int color = 0;
				int light = blockState.getLightEmission() << 4 | blockState.getLightEmission() << 20;
				bounds.addBlock(relX & 15, relY & 15, relZ & 15);

				buildBuffers.getVertexSink().writeVertex(x, y, z, color, u, v, light, buildBuffers.getChunkId());
			}
			buildBuffers.getIndexBufferBuilder(ModelQuadFacing.UNASSIGNED).add(vertexStart, ModelQuadWinding.CLOCKWISE);

			buildBuffers.getVertexSink().flush();
			((ChunkBuildBuffersExt) buffers).iris$ignoreMidBlock(false);
			return;
		}

		if (context.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) context.buffers).iris$setLocalPos(relX, relY, relZ);
		}
	}

	@Inject(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$wrapGetBlockLayer(ChunkBuildContext context,
										CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> cir,
										ChunkRenderData.Builder renderData, VisGraph occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers,
										ChunkRenderCacheLocal cacheLocal,
										WorldSlice slice, int baseX, int baseY, int baseZ, int maxX, int maxY, int maxZ,
										BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos renderOffset,
										int relY, int relZ, int relX, BlockState blockState) {
		if (context.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) context.buffers).iris$setMaterialId(blockState, ExtendedDataHelper.BLOCK_RENDER_TYPE, (byte) blockState.getLightEmission());
		}
	}

	@Inject(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/FluidRenderer;render(Lme/jellysquid/mods/sodium/client/world/WorldSlice;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildBuffers;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$wrapGetFluidLayer(ChunkBuildContext context,
										CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> cir,
										ChunkRenderData.Builder renderData, VisGraph occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers,
										ChunkRenderCacheLocal cacheLocal,
										WorldSlice slice, int baseX, int baseY, int baseZ, int maxX, int maxY, int maxZ,
										BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos renderOffset,
										int relY, int relZ, int relX, BlockState blockState, boolean rendered, FluidState fluidState) {
		if (context.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) context.buffers).iris$setMaterialId(fluidState.createLegacyBlock(), ExtendedDataHelper.FLUID_RENDER_TYPE, (byte) blockState.getLightEmission());
		}
	}

	@Inject(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"))
	private void iris$resetContext(ChunkBuildContext buildContext, CancellationToken cancellationSource, CallbackInfoReturnable<ChunkBuildOutput> cir) {
		if (buildContext.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) buildContext.buffers).iris$resetBlockContext();
		}
	}
}
