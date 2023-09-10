package net.coderbot.iris.mixin.texture.pbr;

import net.coderbot.iris.texture.pbr.PBRType;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.checkerframework.checker.units.qual.K;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.BiConsumer;

@Mixin(DirectoryLister.class)
public class MixinDirectoryLister {

	@Unique
	private ResourceManager resourceManager;

	@ModifyArg(method = "run(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/atlas/SpriteSource$Output;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", remap = false, ordinal = 0))
	private BiConsumer<? super ResourceLocation, ? super Resource> iris$modifyForEachAction(BiConsumer<? super ResourceLocation, ? super Resource> action) {
		BiConsumer<? super ResourceLocation, ? super Resource> finalAction = action;
		BiConsumer<? super ResourceLocation, ? super Resource> wrappedAction = (location, resource) -> {
			String basePath = PBRType.removeSuffix(location.getPath());
			if (basePath != null) {
				ResourceLocation baseLocation = location.withPath(basePath);
				if (resourceManager.getResource(baseLocation).isPresent()) {
					return;
				}
			}
			finalAction.accept(location, resource);
		};
		return wrappedAction;
	}

	@Inject(method = "run", at = @At("HEAD"))
	public void iris$capture(ResourceManager pResourceManager, SpriteSource.Output pOutput, CallbackInfo ci) {
		this.resourceManager = pResourceManager;
	}
}
