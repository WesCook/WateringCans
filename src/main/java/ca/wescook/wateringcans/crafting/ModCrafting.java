package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.fluids.ModFluids;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {
	public static void registerCrafting() {
		// Growth Solution Bucket
		// TODO: Fix returning duplicate bucket
		ItemStack growthBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.fluidGrowthSolution);
		GameRegistry.addShapelessRecipe(growthBucket, Items.WATER_BUCKET, new ItemStack(Items.DYE, 1, 15));

		//CraftingManager CraftingIn = CraftingManager.getInstance();
		//CraftingIn.addShapelessRecipe(growthBucket, Items.WATER_BUCKET, new ItemStack(Items.DYE, 1, 15));
	}
}
