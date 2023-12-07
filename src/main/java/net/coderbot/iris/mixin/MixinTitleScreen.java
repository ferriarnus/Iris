package net.coderbot.iris.mixin;

import net.coderbot.iris.Iris;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
	private static boolean iris$hasFirstInit;

	protected MixinTitleScreen(Component arg) {
		super(arg);
	}

	@Inject(method = "init", at = @At("RETURN"))
	public void iris$firstInit(CallbackInfo ci) {
		if (!iris$hasFirstInit) {
			Iris.onLoadingComplete();
		}
//	public void iris$showSodiumIncompatScreen(CallbackInfo ci) {
//		if (iris$hasFirstInit) return;
//
//		String reason;
//
//		if (!Iris.isSodiumInstalled() && FMLLoader.isProduction()) {
//			reason = "iris.sodium.failure.reason.notFound";
//		} else if (Iris.isSodiumInvalid()) {
//			reason = "iris.sodium.failure.reason.incompatible";
//		} else if (Iris.hasNotEnoughCrashes()) {
//			Minecraft.getInstance().setScreen(new ConfirmScreen(
//				bool -> {
//					if (bool) {
//						if (!iris$hasFirstInit) {
//							Iris.onLoadingComplete();
//						}
//
//						iris$hasFirstInit = true;
//
//						Minecraft.getInstance().setScreen(this);
//					} else {
//						Minecraft.getInstance().stop();
//					}
//				},
//				Component.translatable("iris.nec.failure.title", Iris.MODNAME).withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
//				Component.translatable("iris.nec.failure.description"),
//				Component.translatable("options.graphics.warning.accept").withStyle(ChatFormatting.RED),
//				Component.translatable("menu.quit").withStyle(ChatFormatting.BOLD)));
//			return;
//		} else {
//			if (!iris$hasFirstInit) {
//				Iris.onLoadingComplete();
//			}
//
//			iris$hasFirstInit = true;
//
//			return;
//		}

		iris$hasFirstInit = true;

	}
}
