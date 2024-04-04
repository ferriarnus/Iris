package net.irisshaders.iris.compat.xycraft.mixin;

import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tv.soaryn.xycraft.machines.client.render.instanced.InstancedIcosphere;

@Pseudo
@Mixin(InstancedIcosphere.class)
public class InstancedIcosphereMixin {

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;clearRenderState()V", shift = At.Shift.BEFORE), method = "draw", locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
	private static void setShader(RenderLevelStageEvent event, CallbackInfo ci, ShaderInstance shader) {
		shader.clear();
	}
}
