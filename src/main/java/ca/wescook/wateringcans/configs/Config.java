package ca.wescook.wateringcans.configs;

import ca.wescook.wateringcans.WateringCans;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
	public static boolean enableGrowthSolution;
	public static Map<String, Boolean> enableWateringCans = new HashMap<String, Boolean>();
	public static float growthMultiplier;

	public static void registerConfigs(FMLPreInitializationEvent event) {
		// Create or load from file
		Configuration configFile = new Configuration(new File(event.getModConfigurationDirectory().getPath(), WateringCans.MODID + ".cfg"));
		configFile.load();

		// Get Values
		enableGrowthSolution = configFile.getBoolean("enableGrowthSolution", CATEGORY_GENERAL, true, "Allows crafting of the growth solution bucket");
		growthMultiplier = configFile.getFloat("growthMultiplier", CATEGORY_GENERAL, 1.0F, 0.0F, 10.0F, "Multiply growth ticks from watering cans by this value");

		// If watering cans are enabled
		for (String material : WateringCans.materials) { // Get bool from config file, add to map
			if (!material.equals("creative"))
				enableWateringCans.put(material, configFile.getBoolean("enableWateringCan" + WordUtils.capitalize(material), CATEGORY_GENERAL, true, "Allows crafting of the " + material + " watering can"));
		}

		// Update file
		if (configFile.hasChanged())
			configFile.save();
	}
}
