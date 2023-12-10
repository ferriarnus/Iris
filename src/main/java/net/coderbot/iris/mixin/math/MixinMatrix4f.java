package net.coderbot.iris.mixin.math;

import org.joml.Matrix4f;
import net.coderbot.iris.shadows.Matrix4fAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author FoundationGames
 */
@Mixin(Matrix4f.class)
public class MixinMatrix4f implements Matrix4fAccess {
	@Shadow(remap = false)
	protected float m00;
	@Shadow(remap = false)
	protected float m01;
	@Shadow(remap = false)
	protected float m02;
	@Shadow(remap = false)
	protected float m03;
	@Shadow(remap = false)
	protected float m10;
	@Shadow(remap = false)
	protected float m11;
	@Shadow(remap = false)
	protected float m12;
	@Shadow(remap = false)
	protected float m13;
	@Shadow(remap = false)
	protected float m20;
	@Shadow(remap = false)
	protected float m21;
	@Shadow(remap = false)
	protected float m22;
	@Shadow(remap = false)
	protected float m23;
	@Shadow(remap = false)
	protected float m30;
	@Shadow(remap = false)
	protected float m31;
	@Shadow(remap = false)
	protected float m32;
	@Shadow(remap = false)
	protected float m33;

	@Override
	public void copyFromArray(float[] m) {
		if (m.length != 16) return;
		this.m00 = m[0];
		this.m10 = m[1];
		this.m20 = m[2];
		this.m30 = m[3];
		this.m01 = m[4];
		this.m11 = m[5];
		this.m21 = m[6];
		this.m31 = m[7];
		this.m02 = m[8];
		this.m12 = m[9];
		this.m22 = m[10];
		this.m32 = m[11];
		this.m03 = m[12];
		this.m13 = m[13];
		this.m23 = m[14];
		this.m33 = m[15];
	}

	@Override
	public float[] copyIntoArray() {
		return new float[] {
				m00, m10, m20, m30,
				m01, m11, m21, m31,
				m02, m12, m22, m32,
				m03, m13, m23, m33
		};
	}

	@Override
	public org.joml.Matrix4f convertToJOML() {
		return new org.joml.Matrix4f(
				m00, m10, m20, m30,
				m01, m11, m21, m31,
				m02, m12, m22, m32,
				m03, m13, m23, m33
		);
	}
}
