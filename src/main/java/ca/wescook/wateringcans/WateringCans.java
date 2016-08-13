package ca.wescook.wateringcans;

import ca.wescook.wateringcans.proxy.CommonProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WateringCans.MODID, version = WateringCans.VERSION)
public class WateringCans {
	// Constants
	static final String MODID = "wateringcans";
	static final String MODNAME = "Watering Cans";
	static final String VERSION = "1.0";

	// Create instance of proxy
	// This will vary depending on if the client or server is running
	@SidedProxy(clientSide="ca.wescook.wateringcans.proxy.ClientProxy", serverSide="ca.wescook.wateringcans.proxy.ServerProxy")
	static private CommonProxy proxy;

	// Enable universal buckets
	// Needs to happen before pre-init
	static {
		FluidRegistry.enableUniversalBucket();
	}

	// Events
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WateringCans.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		WateringCans.proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		WateringCans.proxy.postInit(event);
	}
}
