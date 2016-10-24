package ca.wescook.wateringcans;

import ca.wescook.wateringcans.proxy.CommonProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static ca.wescook.wateringcans.WateringCans.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION, dependencies = DEPENDENCIES)
public class WateringCans {
	// Mod Info
	public static final String MODID = "wateringcans";
	public static final String MODNAME = "Watering Cans";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "after:JEI@[3.7.2.220,)"; // [] Inclusive, () Exclusive

	// Mod Constants
	public static final String[] materials = new String[]{"stone", "iron", "gold", "obsidian"};
	public static final String[] fluids = new String[]{"water", "growth_solution"};
	public static final byte petalVariations = 9;
	public static final short fluidCapacity = 500;

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
