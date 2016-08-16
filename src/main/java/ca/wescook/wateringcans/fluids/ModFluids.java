package ca.wescook.wateringcans.fluids;

public class ModFluids {
	public static FluidGrowthSolution fluidGrowthSolution;
	public static BlockFluidGrowthSolution blockFluidGrowthSolution;

	public static void registerFluids() {
		fluidGrowthSolution = new FluidGrowthSolution();
		blockFluidGrowthSolution = new BlockFluidGrowthSolution();
	}
}
