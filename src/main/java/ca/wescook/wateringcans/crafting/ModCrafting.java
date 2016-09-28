package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModCrafting {
	public static void registerCrafting() {

		//////////////////
		// Watering Can //
		//////////////////

		// Set up materials list and hash map
		String[] materials = new String[]{"iron", "gold"};
		Map<String, ItemStack> wateringCanItem = new HashMap<String, ItemStack>();

		// Loop through materials to create ItemStacks for each
		for (String material : materials) {
			NBTTagCompound nbtCompound = new NBTTagCompound(); // Create compound
			nbtCompound.setString("material", material); // Set tag material
			ItemStack tempItem = new ItemStack(ModItems.itemWateringCan); // Create ItemStack
			tempItem.setTagCompound(nbtCompound); // Assign tag to ItemStack
			wateringCanItem.put(material, tempItem); // Add to map
		}

		// Iron Watering Can
		GameRegistry.addShapedRecipe(wateringCanItem.get("iron"),
			"T  ",
			" FL",
			" II",
			'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', Items.LEATHER, 'I', Items.IRON_INGOT
		);

		// Gold Watering Can
		GameRegistry.addShapedRecipe(wateringCanItem.get("gold"),
			"T  ",
			" FL",
			" GG",
			'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', Items.LEATHER, 'G', Items.GOLD_INGOT
		);

		////////////////////////////
		// Growth Solution Bucket //
		////////////////////////////

		// TODO: Look into JEI handling
		GameRegistry.addRecipe(new CraftGrowthSolution());
	}
}
