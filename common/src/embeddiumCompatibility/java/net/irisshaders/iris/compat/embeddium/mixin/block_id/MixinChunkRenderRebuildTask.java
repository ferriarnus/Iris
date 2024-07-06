package net.irisshaders.iris.compat.embeddium.mixin.block_id;

import net.irisshaders.iris.compat.embeddium.impl.block_context.ChunkBuildBuffersExt;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.vertices.ExtendedDataHelper;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.embeddedt.embeddium.api.render.chunk.BlockRenderContext;
import org.embeddedt.embeddium.impl.model.quad.properties.ModelQuadFacing;
import org.embeddedt.embeddium.impl.render.chunk.compile.ChunkBuildBuffers;
import org.embeddedt.embeddium.impl.render.chunk.compile.ChunkBuildContext;
import org.embeddedt.embeddium.impl.render.chunk.compile.ChunkBuildOutput;
import org.embeddedt.embeddium.impl.render.chunk.compile.buffers.ChunkModelBuilder;
import org.embeddedt.embeddium.impl.render.chunk.compile.pipeline.BlockRenderCache;
import org.embeddedt.embeddium.impl.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import org.embeddedt.embeddium.impl.render.chunk.data.BuiltSectionInfo;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.DefaultMaterials;
import org.embeddedt.embeddium.impl.render.chunk.vertex.format.ChunkVertexEncoder;
import org.embeddedt.embeddium.impl.util.task.CancellationToken;
import org.embeddedt.embeddium.impl.world.WorldSlice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Passes additional information indirectly to the vertex writer to support the mc_Entity and at_midBlock parts of the vertex format.
 */
@Mixin(ChunkBuilderMeshingTask.class)
public class MixinChunkRenderRebuildTask {
	private final ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();

	@Inject(method = "execute(Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildContext;Lorg/embeddedt/embeddium/impl/util/task/CancellationToken;)Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "net/minecraft/world/level/block/state/BlockState.getRenderShape()" +
			"Lnet/minecraft/world/level/block/RenderShape;"),
		locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$setLocalPos(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, BuiltSectionInfo.Builder renderData, VisGraph occluder, ChunkBuildBuffers buffers, BlockRenderCache cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos modelOffset, BlockRenderContext context, int relY, int relZ, int relX, BlockState blockState) {
		if (WorldRenderingSettings.INSTANCE.shouldVoxelizeLightBlocks() && blockState.getBlock() instanceof LightBlock) {
			ChunkModelBuilder buildBuffers = buffers.get(DefaultMaterials.CUTOUT);
			((ChunkBuildBuffersExt) buffers).iris$setLocalPos(0, 0, 0);
			((ChunkBuildBuffersExt) buffers).iris$ignoreMidBlock(true);
			((ChunkBuildBuffersExt) buffers).iris$setMaterialId(blockState, (short) 0, (byte) blockState.getLightEmission());
			for (int i = 0; i < 4; i++) {
				vertices[i].x = (float) ((relX & 15)) + 0.25f;
				vertices[i].y = (float) ((relY & 15)) + 0.25f;
				vertices[i].z = (float) ((relZ & 15)) + 0.25f;
				vertices[i].u = 0;
				vertices[i].v = 0;
				vertices[i].color = 0;
				vertices[i].light = blockState.getLightEmission() << 4 | blockState.getLightEmission() << 20;
			}
			buildBuffers.getVertexBuffer(ModelQuadFacing.UNASSIGNED).push(vertices, DefaultMaterials.CUTOUT);
			((ChunkBuildBuffersExt) buffers).iris$ignoreMidBlock(false);
			return;
		}

		if (buildContext.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) buildContext.buffers).iris$setLocalPos(relX, relY, relZ);
		}
	}

	@Inject(method = "execute(Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildContext;Lorg/embeddedt/embeddium/impl/util/task/CancellationToken;)Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$wrapGetBlockLayer(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, BuiltSectionInfo.Builder renderData, VisGraph occluder, ChunkBuildBuffers buffers, BlockRenderCache cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos modelOffset, BlockRenderContext context, int y, int z, int x, BlockState blockState) {
		if (buildContext.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) buildContext.buffers).iris$setMaterialId(blockState, ExtendedDataHelper.BLOCK_RENDER_TYPE, (byte) blockState.getLightEmission());
		}
	}

	@Inject(method = "execute(Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildContext;Lorg/embeddedt/embeddium/impl/util/task/CancellationToken;)Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE",
		target = "Lorg/embeddedt/embeddium/impl/render/chunk/compile/pipeline/FluidRenderer;render(Lorg/embeddedt/embeddium/impl/world/WorldSlice;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildBuffers;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void iris$wrapGetFluidLayer(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, BuiltSectionInfo.Builder renderData, VisGraph occluder, ChunkBuildBuffers buffers, BlockRenderCache cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos modelOffset, BlockRenderContext context, int y, int z, int x, BlockState blockState, FluidState fluidState) {
		if (buildContext.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) buildContext.buffers).iris$setMaterialId(fluidState.createLegacyBlock(), ExtendedDataHelper.FLUID_RENDER_TYPE, (byte) blockState.getLightEmission());
		}
	}

	@Inject(method = "execute(Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildContext;Lorg/embeddedt/embeddium/impl/util/task/CancellationToken;)Lorg/embeddedt/embeddium/impl/render/chunk/compile/ChunkBuildOutput;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"))
	private void iris$resetContext(ChunkBuildContext buildContext, CancellationToken cancellationSource, CallbackInfoReturnable<ChunkBuildOutput> cir) {
		if (buildContext.buffers instanceof ChunkBuildBuffersExt) {
			((ChunkBuildBuffersExt) buildContext.buffers).iris$resetBlockContext();
		}
	}
}
