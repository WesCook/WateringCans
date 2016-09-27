package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
	public static void registerCrafting() {
		// Watering Can
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemWateringCan),
			"T  ",
			" FL",
			" II",
			'T', Blocks.IRON_TRAPDOOR, 'F', Items.FLOWER_POT, 'L', Items.LEATHER, 'I', Items.IRON_INGOT);
		// Growth Solution Bucket
		// TODO: Look into JEI handling
		GameRegistry.addRecipe(new CraftGrowthSolution());
	}
}
