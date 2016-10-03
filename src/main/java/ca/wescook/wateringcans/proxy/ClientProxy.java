package ca.wescook.wateringcans.proxy;

import ca.wescook.wateringcans.events.EventFOV;
import ca.wescook.wateringcans.fluids.ModFluids;
import ca.wescook.wateringcans.items.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		// Render Items
		ModItems.renderItems();

		// Render fluid
		ModFluids.renderFluids();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);

		// Register FOV Event Handler
		MinecraftForge.EVENT_BUS.register(new EventFOV());
	}
}
