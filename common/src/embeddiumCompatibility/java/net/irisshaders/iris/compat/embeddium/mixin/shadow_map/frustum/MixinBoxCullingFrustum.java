package net.irisshaders.iris.compat.embeddium.mixin.shadow_map.frustum;

import net.irisshaders.iris.shadows.frustum.BoxCuller;
import net.irisshaders.iris.shadows.frustum.fallback.BoxCullingFrustum;
import org.embeddedt.embeddium.impl.render.viewport.Viewport;
import org.embeddedt.embeddium.impl.render.viewport.ViewportProvider;
import org.embeddedt.embeddium.impl.render.viewport.frustum.Frustum;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoxCullingFrustum.class)
public class MixinBoxCullingFrustum implements Frustum, ViewportProvider {
	@Unique
	private final Vector3d position = new Vector3d();
	@Shadow(remap = false)
	@Final
	private BoxCuller boxCuller;
	@Shadow
	private double x, y, z;

	@Override
	public Viewport sodium$createViewport() {
		return new Viewport(this, position.set(x, y, z));
	}

	@Override
	public boolean testAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		return !boxCuller.isCulledSodium(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
