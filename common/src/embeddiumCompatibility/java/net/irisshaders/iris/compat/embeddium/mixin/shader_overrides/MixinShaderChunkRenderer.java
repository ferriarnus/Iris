package net.irisshaders.iris.compat.embeddium.mixin.shader_overrides;

import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.iris.compat.embeddium.impl.shader_overrides.IrisChunkProgramOverrides;
import net.irisshaders.iris.compat.embeddium.impl.shader_overrides.IrisChunkShaderInterface;
import net.irisshaders.iris.compat.embeddium.impl.shader_overrides.ShaderChunkRendererExt;
import net.irisshaders.iris.gl.program.ProgramSamplers;
import net.irisshaders.iris.gl.program.ProgramUniforms;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import org.embeddedt.embeddium.impl.gl.device.RenderDevice;
import org.embeddedt.embeddium.impl.gl.shader.GlProgram;
import org.embeddedt.embeddium.impl.render.chunk.ShaderChunkRenderer;
import org.embeddedt.embeddium.impl.render.chunk.shader.ChunkShaderInterface;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.embeddedt.embeddium.impl.render.chunk.vertex.format.ChunkVertexType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Overrides shaders in {@link ShaderChunkRenderer} with our own as needed.
 */
@Mixin(ShaderChunkRenderer.class)
public class MixinShaderChunkRenderer implements ShaderChunkRendererExt {
	@Shadow(remap = false)
	@Final
	protected ChunkVertexType vertexType;
	@Unique
	private IrisChunkProgramOverrides irisChunkProgramOverrides;
	@Unique
	private GlProgram<IrisChunkShaderInterface> override;
	@Shadow(remap = false)
	private GlProgram<ChunkShaderInterface> activeProgram;

	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void iris$onInit(RenderDevice device, ChunkVertexType vertexType, CallbackInfo ci) {
		irisChunkProgramOverrides = new IrisChunkProgramOverrides();
	}

	@Inject(method = "begin", at = @At("HEAD"), cancellable = true, remap = false)
	private void iris$begin(TerrainRenderPass pass, CallbackInfo ci) {
		this.override = irisChunkProgramOverrides.getProgramOverride(pass, this.vertexType);

		if (this.override == null) {
			return;
		}

		// Override with our own behavior
		ci.cancel();

		// Set a sentinel value here, so we can catch it in RegionChunkRenderer and handle it appropriately.
		activeProgram = null;

		irisChunkProgramOverrides.bindFramebuffer(pass);

		if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) {
			// No back face culling during the shadow pass
			// TODO: Hopefully this won't be necessary in the future...
			RenderSystem.disableCull();
		}

		pass.startDrawing();

		override.bind();
		override.getInterface().setupState();
	}

	@Inject(method = "end", at = @At("HEAD"), remap = false, cancellable = true)
	private void iris$onEnd(TerrainRenderPass pass, CallbackInfo ci) {
		ProgramUniforms.clearActiveUniforms();
		ProgramSamplers.clearActiveSamplers();

		if (override != null) {
			irisChunkProgramOverrides.unbindFramebuffer();

			override.getInterface().restore();
			override.unbind();
			pass.endDrawing();

			override = null;
			ci.cancel();
		}
	}

	@Inject(method = "delete", at = @At("HEAD"), remap = false)
	private void iris$onDelete(CallbackInfo ci) {
		irisChunkProgramOverrides.deleteShaders();
	}

	@Override
	public GlProgram<IrisChunkShaderInterface> iris$getOverride() {
		return override;
	}
}
