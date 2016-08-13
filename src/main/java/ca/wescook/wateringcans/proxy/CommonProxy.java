package ca.wescook.wateringcans.proxy;

import ca.wescook.wateringcans.fluids.ModFluids;
import ca.wescook.wateringcans.items.ModItems;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModItems.registerItems();
		ModFluids.registerFluids();
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}
}
