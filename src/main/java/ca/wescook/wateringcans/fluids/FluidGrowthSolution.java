package ca.wescook.wateringcans.fluids;

import ca.wescook.wateringcans.WateringCans;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

class FluidGrowthSolution extends Fluid {
	FluidGrowthSolution() {
		super("growth_solution", new ResourceLocation(WateringCans.MODID, "fluids/growth_solution_still"), new ResourceLocation(WateringCans.MODID, "fluids/growth_solution_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
	}
}
