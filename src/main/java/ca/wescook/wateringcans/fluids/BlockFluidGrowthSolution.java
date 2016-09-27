package ca.wescook.wateringcans.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

class BlockFluidGrowthSolution extends BlockFluidClassic {
	BlockFluidGrowthSolution() {
		super(ModFluids.fluidGrowthSolution, Material.WATER);
		setRegistryName("growth_solution_block");
		setUnlocalizedName(getRegistryName().toString());
		GameRegistry.register(this);
	}

	@SideOnly(Side.CLIENT)
	void render() {
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(getRegistryName().toString());
			}
		});
	}
}
