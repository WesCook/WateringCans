package ca.wescook.wateringcans.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	public static void registerFluids() {
		// Growth Solution
		Fluid growthSolution = new Fluid("growth_solution", new ResourceLocation("wateringcans:blocks/fluids/growth_solution_still"), new ResourceLocation("wateringcans:blocks/fluids/growth_solution_flow")); // Create fluid object
		FluidRegistry.registerFluid(growthSolution); // Register fluid
		FluidRegistry.addBucketForFluid(growthSolution); // Create universal bucket for fluid
		BlockFluidGrowthSolution blockFluidGrowthSolution = new BlockFluidGrowthSolution(growthSolution); // Create fluid block
	}
}
