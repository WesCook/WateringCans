package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
	public static void registerCrafting() {
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemWateringCan), "dt ", " il", " i ", 'd', Blocks.DISPENSER, 't', Blocks.IRON_TRAPDOOR, 'i', Items.IRON_INGOT, 'l', Items.LEATHER); // Watering Can
		GameRegistry.addRecipe(new CraftGrowthSolution()); // Growth Solution Bucket
	}
}
