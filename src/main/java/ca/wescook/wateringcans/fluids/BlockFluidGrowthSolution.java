package ca.wescook.wateringcans.fluids;

import ca.wescook.wateringcans.WateringCans;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;

class BlockFluidGrowthSolution extends BlockFluidClassic {
	BlockFluidGrowthSolution() {
		super(ModFluids.fluidGrowthSolution, Material.WATER);
		setRegistryName("growth_solution_block");
		setUnlocalizedName(WateringCans.MODID + ":growth_solution_block");
		GameRegistry.register(this);
		//GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
}
