package ca.wescook.wateringcans.proxy;

import ca.wescook.wateringcans.crafting.ModCrafting;
import ca.wescook.wateringcans.fluids.ModFluids;
import ca.wescook.wateringcans.items.ModItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModItems.registerItems();
		ModFluids.registerFluids();
	}

	public void init(FMLInitializationEvent event) {
		ModCrafting.registerCrafting();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}
}
