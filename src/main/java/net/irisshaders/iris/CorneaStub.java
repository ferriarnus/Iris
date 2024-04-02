package net.irisshaders.iris;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

//Seperate class from Iris due to early loading of Iris
@Mod(CorneaStub.MODID)
public class CorneaStub {
	public static final String MODID = "cornea";

	public CorneaStub(ModContainer container) {
		container.getEventBus().addListener(Iris::keyEvent);
	}
}
