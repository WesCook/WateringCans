package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.WateringCans;
import ca.wescook.wateringcans.configs.Config;
import ca.wescook.wateringcans.items.ItemWateringCan;
import ca.wescook.wateringcans.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;
import java.util.Map;

public class ModCrafting {
	public static void registerCrafting() {

		//////////////////
		// Watering Can //
		//////////////////

		// Set up materials list and hash map
		Map<String, ItemStack> wateringCanItem = new HashMap<String, ItemStack>();

		// Loop through materials to create ItemStacks for each
		for (String material : WateringCans.materials) {
			ItemStack tempItem = new ItemStack(ModItems.itemWateringCan); // Create ItemStack
			NBTTagCompound nbtCompound = ItemWateringCan.getDefaultNBT(); // Create compound from NBT defaults
			nbtCompound.setString("material", material); // Overwrite material tag
			tempItem.setTagCompound(nbtCompound); // Assign tag to ItemStack
			wateringCanItem.put(material, tempItem); // Add to map
		}

		// Stone Watering Can
		if (Config.enableWateringCans.get("stone")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(wateringCanItem.get("stone"),
				"T  ",
				" FL",
				" SS",
				'T', Blocks.TRAPDOOR, 'F', Items.FLOWER_POT, 'L', "leather", 'S', "cobblestone"
			));
		}

		// Iron Watering Can
		if (Config.enableWateringCans.get("iron")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(wateringCanItem.get("iron"),
				"T  ",
				" FL",
				" II",
				'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', "leather", 'I', "ingotIron"
			));
		}

		// Gold Watering Can
		if (Config.enableWateringCans.get("gold")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(wateringCanItem.get("gold"),
				"T  ",
				" FL",
				" GG",
				'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', "leather", 'G', "ingotGold"
			));
		}

		// Obsidian Watering Can
		if (Config.enableWateringCans.get("obsidian")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(wateringCanItem.get("obsidian"),
				"T  ",
				" FL",
				" OO",
				'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', "leather", 'O', Blocks.OBSIDIAN
			));
		}

		////////////////////////////
		// Growth Solution Bucket //
		////////////////////////////

		if (Config.enableGrowthSolution) // If recipe enabled
			GameRegistry.addRecipe(new CraftGrowthSolution());
	}
}
