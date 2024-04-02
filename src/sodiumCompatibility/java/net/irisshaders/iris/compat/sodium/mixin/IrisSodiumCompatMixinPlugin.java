package net.irisshaders.iris.compat.sodium.mixin;

import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Semi-critical mixin config plugin, disables mixins if Sodium isn't present,
 * since on 1.18+ we have mixins into Iris classes that crash the game instead of just
 * spamming the log if Sodium isn't present.
 */
public class IrisSodiumCompatMixinPlugin implements IMixinConfigPlugin {
	private boolean validSodiumVersion = false;

	@Override
	public void onLoad(String mixinPackage) {
		validSodiumVersion = FMLLoader.getLoadingModList().getModFileById("embeddium") != null;

		if (!validSodiumVersion) {
			// We can't use Iris' logger here due to classloading issues.
			System.err.println("[Iris] Invalid/missing version of Sodium detected, disabling compatibility mixins!");
		}

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return validSodiumVersion;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
