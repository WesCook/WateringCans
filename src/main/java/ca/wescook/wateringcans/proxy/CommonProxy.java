package ca.wescook.wateringcans.proxy;

import ca.wescook.wateringcans.configs.Config;
import ca.wescook.wateringcans.crafting.ModCrafting;
import ca.wescook.wateringcans.fluids.ModFluids;
import ca.wescook.wateringcans.items.ModItems;
import ca.wescook.wateringcans.potions.ModPotions;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		Config.registerConfigs(event);
		ModItems.registerItems();
		ModFluids.registerFluids();
		ModPotions.registerPotions();
	}

	public void init(FMLInitializationEvent event) {
		ModCrafting.registerCrafting();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}
}
