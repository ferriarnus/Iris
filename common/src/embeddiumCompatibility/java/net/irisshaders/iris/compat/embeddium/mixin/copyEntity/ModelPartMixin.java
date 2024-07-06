package net.irisshaders.iris.compat.embeddium.mixin.copyEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import org.embeddedt.embeddium.api.math.MatrixHelper;
import org.embeddedt.embeddium.api.util.ColorARGB;
import org.embeddedt.embeddium.api.vertex.buffer.VertexBufferWriter;
import org.embeddedt.embeddium.impl.model.ModelCuboidAccessor;
import org.embeddedt.embeddium.impl.render.immediate.model.EntityRenderer;
import org.embeddedt.embeddium.impl.render.immediate.model.ModelCuboid;
import org.embeddedt.embeddium.impl.render.immediate.model.ModelPartData;
import org.embeddedt.embeddium.impl.render.vertex.VertexConsumerUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public class ModelPartMixin implements ModelPartData {
	@Shadow
	public float x;
	@Shadow
	public float y;
	@Shadow
	public float z;

	@Shadow
	public float xScale;
	@Shadow
	public float yScale;
	@Shadow
	public float zScale;

	@Shadow
	public float yRot;
	@Shadow
	public float xRot;
	@Shadow
	public float zRot;

	@Shadow
	public boolean visible;
	@Shadow
	public boolean skipDraw;

	@Mutable
	@Shadow
	@Final
	private List<ModelPart.Cube> cubes;

	@Mutable
	@Shadow
	@Final
	private Map<String, ModelPart> children;

	@Unique
	private ModelPart[] sodium$children;

	@Unique
	private ModelCuboid[] sodium$cuboids;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(List<ModelPart.Cube> cuboids, Map<String, ModelPart> children, CallbackInfo ci) {
		var copies = new ModelCuboid[cuboids.size()];

		for (int i = 0; i < cuboids.size(); i++) {
			var accessor = (ModelCuboidAccessor) cuboids.get(i);
			copies[i] = accessor.sodium$copy();
		}

		this.sodium$cuboids = copies;
		this.sodium$children = children.values()
			.toArray(ModelPart[]::new);

		// Try to catch errors caused by mods touching the collections after we've copied everything.
		this.cubes = Collections.unmodifiableList(this.cubes);
		this.children = Collections.unmodifiableMap(this.children);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", at = @At("HEAD"), cancellable = true)
	private void onRender(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
		VertexBufferWriter writer = VertexConsumerUtils.convertOrLog(vertices);

		if (writer == null) {
			return;
		}

		ci.cancel();

		EntityRenderer.render(matrices, writer, (ModelPart) (Object) this, light, overlay, ColorARGB.toABGR(color));
	}

	/**
	 * @author JellySquid
	 * @reason Apply transform more quickly
	 */
	@Overwrite
	public void translateAndRotate(PoseStack matrixStack) {
		if (this.x != 0.0F || this.y != 0.0F || this.z != 0.0F) {
			matrixStack.translate(this.x * (1.0f / 16.0f), this.y * (1.0f / 16.0f), this.z * (1.0f / 16.0f));
		}

		if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
			MatrixHelper.rotateZYX(matrixStack.last(), this.zRot, this.yRot, this.xRot);
		}

		if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
			matrixStack.scale(this.xScale, this.yScale, this.zScale);
		}
	}

	@Override
	public ModelCuboid[] getCuboids() {
		return this.sodium$cuboids;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public boolean isHidden() {
		return this.skipDraw;
	}

	@Override
	public ModelPart[] getChildren() {
		return this.sodium$children;
	}
}
