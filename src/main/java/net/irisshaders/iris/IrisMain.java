package net.irisshaders.iris;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

//Seperate class from Iris due to early loading of Iris
@Mod(Iris.MODID)
public class IrisMain {

	public IrisMain() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(Iris::keyEvent);
	}
}
