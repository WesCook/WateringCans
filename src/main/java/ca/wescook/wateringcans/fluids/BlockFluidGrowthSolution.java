package ca.wescook.wateringcans.fluids;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;

class BlockFluidGrowthSolution extends BlockFluidClassic {
	BlockFluidGrowthSolution() {
		super(ModFluids.fluidGrowthSolution, Material.WATER);
		setRegistryName("growth_solution_block");
		setUnlocalizedName(getRegistryName().toString());
		GameRegistry.register(this);
	}
}
