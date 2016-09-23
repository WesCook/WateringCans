package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
	public static void registerCrafting() {
		// Watering Can
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemWateringCan), "t   ", " fl", " ii",
			't', Blocks.IRON_TRAPDOOR,
			'i', Items.IRON_INGOT,
			'f', Blocks.FLOWER_POT,
			'l', Items.LEATHER);
		// Growth Solution Bucket
		GameRegistry.addRecipe(new CraftGrowthSolution());
	}
}
