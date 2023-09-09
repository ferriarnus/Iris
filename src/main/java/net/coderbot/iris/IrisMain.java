package net.coderbot.iris;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//Seperate class from Iris due to early loading of Iris
@Mod(Iris.MODID)
public class IrisMain {

	public IrisMain() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(Iris::keyEvent);
	}
}
