package ca.wescook.wateringcans.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.minecraft.block.BlockFarmland.MOISTURE;

class BlockFluidGrowthSolution extends BlockFluidClassic {
	BlockFluidGrowthSolution() {
		super(ModFluids.fluidGrowthSolution, Material.WATER);
		setRegistryName("growth_solution_block");
		setUnlocalizedName(getRegistryName().toString());
		GameRegistry.register(this);
	}

	@SideOnly(Side.CLIENT)
	void render() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}

	@Override
	public void updateTick(World worldIn, BlockPos fluidPos, IBlockState state, Random rand) {
		// Still run parent update (eg. fluid updates)
		super.updateTick(worldIn, fluidPos, state, rand);

		// Check for and moisten farmland
		int range = 8; // Extended range (normally 4)
		for (BlockPos farmPos : BlockPos.getAllInBoxMutable(fluidPos.add(-range, 0, -range), fluidPos.add(range, 1, range))) { // Loop through all blocks within range
			if (worldIn.getBlockState(farmPos).getBlock() == Blocks.FARMLAND) { // If farm block
				if (rand.nextInt(8) < 1) // Some randomness to prevent all tiles from being moistened at once
					worldIn.setBlockState(farmPos, Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7)); // Moisten it
			}
		}

		// Restart
		worldIn.scheduleUpdate(fluidPos, this, 20);
	}
}
