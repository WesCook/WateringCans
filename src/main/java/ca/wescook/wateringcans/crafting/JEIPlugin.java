package ca.wescook.wateringcans.crafting;

import ca.wescook.wateringcans.WateringCans;
import ca.wescook.wateringcans.fluids.ModFluids;
import ca.wescook.wateringcans.items.ItemWateringCan;
import ca.wescook.wateringcans.items.ModItems;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@mezz.jei.api.JEIPlugin
public class JEIPlugin extends BlankModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		// Register NBT uniquely so JEI distinguishes between each watering can
		ISubtypeRegistry.ISubtypeInterpreter wateringCanInterpreter = new ISubtypeRegistry.ISubtypeInterpreter() {
			@Nullable
			@Override
			public String getSubtypeInfo(ItemStack itemStack) {
				if (itemStack.getTagCompound() != null)
					return itemStack.getTagCompound().getString("material");
				return null;
			}
		};
		subtypeRegistry.registerSubtypeInterpreter(ModItems.itemWateringCan, wateringCanInterpreter);
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		// Add description for growth solution bucket
		ItemStack growthBucket = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.fluidGrowthSolution); // Create instance of growth solution bucket
		registry.addDescription(growthBucket, "jei.wateringcans:growth_bucket"); // Create description page for it

		// Add description for watering cans
		for (String material : WateringCans.materials) {
			ItemStack tempItem = new ItemStack(ModItems.itemWateringCan); // Create ItemStack
			NBTTagCompound nbtCompound = ItemWateringCan.getDefaultNBT(); // Create compound from NBT defaults
			nbtCompound.setString("material", material); // Overwrite material tag
			tempItem.setTagCompound(nbtCompound); // Assign tag to ItemStack
			registry.addDescription(tempItem, "jei.wateringcans:watering_can_" + material); // Create description page
		}
	}
}
