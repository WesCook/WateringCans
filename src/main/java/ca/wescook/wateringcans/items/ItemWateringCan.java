package ca.wescook.wateringcans.items;

import ca.wescook.wateringcans.WateringCans;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static java.util.Arrays.asList;
import static net.minecraft.block.BlockFarmland.MOISTURE;

class ItemWateringCan extends Item {

	ItemWateringCan() {
		setRegistryName("watering_can");
		setUnlocalizedName(WateringCans.MODID + ".watering_can");
		GameRegistry.register(this);
		setCreativeTab(CreativeTabs.TOOLS);
		setMaxStackSize(1);
		renderModel();
	}

	@SideOnly(Side.CLIENT)
	private void renderModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	// On right click
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		// List of valid fluid blocks
		String[] validBlocks = new String[]{"water", WateringCans.MODID + ":growth_solution_block"};

		// Ray trace - find block we're looking at
		RayTraceResult rayTraceResult = this.rayTrace(worldIn, playerIn, true);

		// If sky, ignore
		if (rayTraceResult == null) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		else {
			// If a block is found (includes fluids)
			if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {

				// Get exact vector to later spawn particles there
				Vec3d rayTraceVector = rayTraceResult.hitVec;

				// Collect information about block
				BlockPos blockPos = rayTraceResult.getBlockPos(); // Get block position from ray trace
				Block blockObj = worldIn.getBlockState(blockPos).getBlock(); // Get block object
				String blockNameRaw = blockObj.getUnlocalizedName(); // Get block name
				String blockName = blockNameRaw.substring(5); // Clean .tile prefix

				// Check for/create NBT tag
				NBTTagCompound nbtCompound = itemStackIn.getTagCompound(); // Check if exists
				if (nbtCompound == null) // If not
					nbtCompound = new NBTTagCompound(); // Create new compound
				itemStackIn.setTagCompound(nbtCompound); // Attach to itemstack

				// If found block is in fluid list, refill watering can
				if (asList(validBlocks).contains(blockName))
					refillWateringCan(worldIn, playerIn, itemStackIn, nbtCompound, blockName, blockPos);
				else // Water that block
					commenceWatering(worldIn, nbtCompound, rayTraceVector, blockPos);
			}

			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
	}

	private void refillWateringCan(World worldIn, EntityPlayer playerIn, ItemStack itemStackIn, NBTTagCompound nbtCompound, String blockName, BlockPos blockPos) {
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F); // Play sound
		worldIn.destroyBlock(blockPos, false); // Destroy block

		// Create bubbles
		for (int i=0; i<10; i++)
			worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, blockPos.getX() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), blockPos.getY() + 1, blockPos.getZ() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), 0.0D, 0.0D, 0.0D);

		// Assign fluid based on block
		// TODO: Add tooltips for fluid/amount
		if (blockName.equals("water"))
			nbtCompound.setString("fluid", "water");
		else if (blockName.equals(WateringCans.MODID + ":growth_solution_block"))
			nbtCompound.setString("fluid", "growth_solution");

		// Refill watering can
		nbtCompound.setShort("amount", (short) 500);
	}

	private void commenceWatering(World worldIn, NBTTagCompound nbtCompound, Vec3d rayTraceVector, BlockPos blockPos) {
		short amountRemaining = nbtCompound.getShort("amount");

		// If water remains in can
		if (amountRemaining > 0) {

			// Create water particles
			for (int i=0; i<25; i++)
				// TODO: Color according to fluid type
				worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, rayTraceVector.xCoord + (worldIn.rand.nextGaussian() * 0.18D), rayTraceVector.yCoord, rayTraceVector.zCoord + (worldIn.rand.nextGaussian() * 0.18D), 0.0D, 0.0D, 0.0D);

			// Iterate through total reach
			int reach = 3; // Total grid size to water
			int halfReach = (int) Math.floor(reach / 2); // Used to calculate offset in each direction

			for (int i=0; i<reach; i++) {
				for (int j=0; j<reach; j++) {

					// Create new block to affect
					BlockPos tempBlockPos = blockPos.add(i - halfReach, 0, j - halfReach); // Offset to center grid on selected block
					Block tempBlockObj = worldIn.getBlockState(tempBlockPos).getBlock();

					// Moisten soil
					if (tempBlockObj.getUnlocalizedName().equals("tile.farmland")) // If block is farmland
						worldIn.setBlockState(tempBlockPos, Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7)); // Moisten it

					// Trigger tick updates
					if (!tempBlockObj.getUnlocalizedName().equals("tile.farmland")) { // To avoid immediately untilling farmland
						// Update tick speed based on fluid used
						if (nbtCompound.getString("fluid").equals("water")) // Water
							worldIn.updateBlockTick(tempBlockPos, tempBlockObj, 23, 0);
						else if (nbtCompound.getString("fluid").equals("growth_solution")) // Growth Solution
							worldIn.updateBlockTick(tempBlockPos, tempBlockObj, 4, 0);
					}

					// Decrease fluid amount
					// TODO: See if NBT can be updated without resetting held item
					//if (amountRemaining > 0)
						//nbtCompound.setShort("amount", (short) (amountRemaining - 1));
				}
			}
		}
	}
}
