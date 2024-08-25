package net.irisshaders.iris.compat.sodium.mixin;

import net.irisshaders.iris.shadows.ShadowRenderingState;
import org.embeddedt.embeddium.impl.gui.EmbeddiumOptions;
import org.embeddedt.embeddium.impl.render.chunk.DefaultChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultChunkRenderer.class)
public class MixinDefaultChunkRenderer {
	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lorg/embeddedt/embeddium/impl/gui/EmbeddiumOptions$PerformanceSettings;useBlockFaceCulling:Z"), remap = false)
	private boolean iris$disableBlockFaceCullingInShadowPass(EmbeddiumOptions.PerformanceSettings instance) {
		if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) return false;
		return instance.useBlockFaceCulling;
	}

	// TODO IMS: Something about this feels... wrong.
//	@ModifyArg(method = "prepareIndexedTessellation", index = 2, at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;createRegionTessellation(Lnet/caffeinemc/mods/sodium/client/gl/device/CommandList;Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion$DeviceResources;Z)Lnet/caffeinemc/mods/sodium/client/gl/tessellation/GlTessellation;"), remap = false)
//	private boolean doNotSortInShadow(boolean useSharedIndexBuffer) {
//		if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) return false;
//
//		return useSharedIndexBuffer;
//	}
}
