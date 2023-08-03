package net.coderbot.iris.mixin;

import net.coderbot.iris.Iris;
import net.coderbot.iris.gl.shader.ShaderCompileException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends ClientCommonPacketListenerImpl {
	protected MixinClientPacketListener(Minecraft pClientCommonPacketListenerImpl0, Connection pConnection1, CommonListenerCookie pCommonListenerCookie2) {
		super(pClientCommonPacketListenerImpl0, pConnection1, pCommonListenerCookie2);
	}

	@Inject(method = "handleLogin", at = @At("TAIL"))
	private void iris$showUpdateMessage(ClientboundLoginPacket a, CallbackInfo ci) {
		if (this.minecraft.player == null) {
			return;
		}

		Iris.getUpdateChecker().getUpdateMessage().ifPresent(msg ->
			this.minecraft.player.displayClientMessage(msg, false));

		Iris.getStoredError().ifPresent(e ->
			this.minecraft.player.displayClientMessage(Component.translatable(e instanceof ShaderCompileException ? "iris.load.failure.shader" : "iris.load.failure.generic").append(Component.literal("Copy Info").withStyle(arg -> arg.withUnderlined(true).withColor(ChatFormatting.BLUE).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, e.getMessage())))), false));
	}
}
