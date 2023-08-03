package net.coderbot.iris.mixin.shadows;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.coderbot.iris.pipeline.ShadowRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Prevent nearby chunks from being rebuilt on the main thread in the shadow pass. Aside from causing FPS to tank,
 * this also causes weird chunk corruption! It's critical to make sure that it's disabled as a result.
 *
 * This patch is not relevant with Sodium installed since Sodium has a completely different build path for terrain
 * setup.
 *
 * Uses a priority of 1010 to apply after Sodium's overwrite, to allow for the Group behavior to activate. Otherwise,
 * if we apply with the same priority, then we'll just get a Mixin error due to the injects conflicting with the
 * {@code @Overwrite}. Using {@code @Group} allows us to avoid a fragile Mixin plugin.
 */
@Mixin(value = SectionOcclusionGraph.class, priority = 1010)
public abstract class MixinPreventRebuildNearInShadowPass {

	@Inject(method = "update",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;runPartialUpdate(ZLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/List;Lnet/minecraft/world/phys/Vec3;)V"),
			cancellable = true,
			require = 0)
	private void iris$preventRebuildNearInShadowPass(boolean pSectionOcclusionGraph0, Camera pCamera1, Frustum pFrustum2, List<SectionRenderDispatcher.RenderSection> pList3, CallbackInfo ci) {
		if (ShadowRenderer.ACTIVE) {
			ci.cancel();
		}
	}
}
