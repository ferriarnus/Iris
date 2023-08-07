package net.irisshaders.iris.shadows.frustum.advanced;

import net.irisshaders.iris.shadows.frustum.BoxCuller;
import net.minecraft.world.phys.AABB;

public class ReversedAdvancedShadowCullingFrustum extends AdvancedShadowCullingFrustum {
	public ReversedAdvancedShadowCullingFrustum(Matrix4f playerView, Matrix4f playerProjection, Vector3f shadowLightVectorFromOrigin, BoxCuller boxCuller) {
		super(playerView, playerProjection, shadowLightVectorFromOrigin, boxCuller);
	}

	@Override
	public boolean isVisible(AABB aabb) {
		if (boxCuller != null && !boxCuller.isCulled(aabb)) {
			return true;
		}

		return this.isVisible(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ) != 0;
	}

	@Override
	public int fastAabbTest(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		if (boxCuller != null && !boxCuller.isCulled(minX, minY, minZ, maxX, maxY, maxZ)) {
			return 2;
		}

		return isVisible(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
