package net.coderbot.iris.mixin.shadows;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.coderbot.iris.shadows.CullingDataCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer implements CullingDataCache {
	@Shadow
	@Final
	@Mutable
	private ObjectArrayList visibleSections;

	@Unique
	private ObjectArrayList savedRenderChunks = new ObjectArrayList(69696);

	@Shadow
	@Final
	@Mutable
	private SectionOcclusionGraph sectionOcclusionGraph;

	@Unique
	private SectionOcclusionGraph savedSectionocclusionGraph = new SectionOcclusionGraph();

	@Shadow
	private double prevCamX;

	@Shadow
	private double prevCamY;

	@Shadow
	private double prevCamZ;

	@Shadow
	private double prevCamRotX;

	@Shadow
	private double prevCamRotY;

	@Unique
	private double savedLastCameraX;

	@Unique
	private double savedLastCameraY;

	@Unique
	private double savedLastCameraZ;

	@Unique
	private double savedLastCameraPitch;

	@Unique
	private double savedLastCameraYaw;

	@Override
	public void saveState() {
		swap();
	}

	@Override
	public void restoreState() {
		swap();
	}

	@Unique
	private void swap() {
		ObjectArrayList tmpList = visibleSections;
		visibleSections = savedRenderChunks;
		savedRenderChunks = tmpList;

		// TODO: If the normal chunks need a terrain update, these chunks probably do too...
		// We probably should copy it over
		SectionOcclusionGraph tmpBool = sectionOcclusionGraph;
		sectionOcclusionGraph = savedSectionocclusionGraph;
		savedSectionocclusionGraph = tmpBool;

		double tmp;

		tmp = prevCamX;
		prevCamX = savedLastCameraX;
		savedLastCameraX = tmp;

		tmp = prevCamY;
		prevCamY = savedLastCameraY;
		savedLastCameraY = tmp;

		tmp = prevCamZ;
		prevCamZ = savedLastCameraZ;
		savedLastCameraZ = tmp;

		tmp = prevCamRotX;
		prevCamRotX = savedLastCameraPitch;
		savedLastCameraPitch = tmp;

		tmp = prevCamRotY;
		prevCamRotY = savedLastCameraYaw;
		savedLastCameraYaw = tmp;
	}

	@Inject(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;waitAndReset(Lnet/minecraft/client/renderer/ViewArea;)V"))
	private void waitAndReset(ClientLevel pLevelRenderer0, CallbackInfo ci) {
		savedSectionocclusionGraph.waitAndReset(null);
	}

	@Redirect(method = "allChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;waitAndReset(Lnet/minecraft/client/renderer/ViewArea;)V"))
	private void waitAndReset2(SectionOcclusionGraph instance, ViewArea pSectionOcclusionGraph0) {
		instance.waitAndReset(pSectionOcclusionGraph0);
		savedSectionocclusionGraph.waitAndReset(pSectionOcclusionGraph0);
	}

	@Redirect(method = "addRecentlyCompiledSection", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;onSectionCompiled(Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSection;)V"))
	private void waitAndRese3(SectionOcclusionGraph instance, SectionRenderDispatcher.RenderSection pSectionOcclusionGraph0) {
		instance.onSectionCompiled(pSectionOcclusionGraph0);
		savedSectionocclusionGraph.onSectionCompiled(pSectionOcclusionGraph0);
	}

	@Redirect(method = "onChunkLoaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;onChunkLoaded(Lnet/minecraft/world/level/ChunkPos;)V"))
	private void waitAndRese4(SectionOcclusionGraph instance, ChunkPos pSectionOcclusionGraph0) {
		instance.onChunkLoaded(pSectionOcclusionGraph0);
		savedSectionocclusionGraph.onChunkLoaded(pSectionOcclusionGraph0);
	}

	@Inject(method = "needsUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SectionOcclusionGraph;invalidate()V"))
	private void waitAndRese5(CallbackInfo ci) {
		savedSectionocclusionGraph.invalidate();
	}
}
