package ca.wescook.wateringcans.fluids;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

class BlockFluidGrowthSolution extends BlockFluidClassic {
	BlockFluidGrowthSolution(Fluid fluid) {
		super(fluid, Material.WATER);
		setRegistryName("growth_solution_block");
		setUnlocalizedName("wateringcans:growth_solution_block");
		GameRegistry.register(this);
	}
}
