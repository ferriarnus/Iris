package net.irisshaders.iris.compat.sodium.mixin;

import net.irisshaders.iris.Iris;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import org.embeddedt.embeddium.impl.gl.shader.GlProgram;
import org.embeddedt.embeddium.impl.render.chunk.ShaderChunkRenderer;
import org.embeddedt.embeddium.impl.render.chunk.shader.ChunkShaderInterface;
import org.embeddedt.embeddium.impl.render.chunk.shader.ChunkShaderOptions;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ShaderChunkRenderer.class, remap = false)
public abstract class MixinShaderChunkRenderer {
	@Shadow
	protected abstract GlProgram<ChunkShaderInterface> compileProgram(ChunkShaderOptions options);

	@Redirect(method = "begin", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/ShaderChunkRenderer;compileProgram(Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderOptions;)Lnet/caffeinemc/mods/sodium/client/gl/shader/GlProgram;"))
	private GlProgram<ChunkShaderInterface> redirectIrisProgram(ShaderChunkRenderer instance, ChunkShaderOptions options, TerrainRenderPass pass) {
		WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

		GlProgram<ChunkShaderInterface> program = null;

		if (pipeline instanceof IrisRenderingPipeline irisRenderingPipeline) {
			irisRenderingPipeline.getSodiumPrograms().getFramebuffer(pass).bind();
			program = irisRenderingPipeline.getSodiumPrograms().getProgram(pass);
		}

		if (program == null) {
			return this.compileProgram(options);
		}

		return program;
	}
}
