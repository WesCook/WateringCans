package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.fluids.ModFluids;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;

import javax.annotation.Nullable;

class CraftGrowthSolution implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting craftMatrix, World worldIn) {
		// TODO: Refactor crafting handler to abstract out recipe/eliminate hardcoding

		// Init
		int waterFound = 0;
		int bonemealFound = 0;
		boolean somethingElseFound = false;

		// Iterate through number of slots in crafting grid
		for (int i=0; i<craftMatrix.getSizeInventory(); i++)
		{
			ItemStack item = craftMatrix.getStackInSlot(i); // Get current item
			if (item != null) {
				// Verify only one stack of each item
				if (item.getItem() == Items.WATER_BUCKET) // Water
					waterFound += 1;
				else if (item.getItem() == Items.DYE && item.getItemDamage() == 15) // Bonemeal
					bonemealFound += 1;
				else
					somethingElseFound = true;
			}
		}
		// Recipe matches
		if (waterFound == 1 && bonemealFound == 1 && !somethingElseFound)
			return true;

		return false;
	}

	@Nullable
	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftMatrix) {
		ItemStack growthBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.fluidGrowthSolution);
		return growthBucket;
	}

	@Override
	public int getRecipeSize() {
		return 0;
	}

	@Nullable
	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting craftMatrix) {
		// Remove crafting ingredients (necessary for buckets)
		return new ItemStack[craftMatrix.getSizeInventory()];
	}
}
