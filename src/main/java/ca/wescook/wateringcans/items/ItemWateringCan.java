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

import static java.lang.Math.floor;
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

				// If found block is in fluid list, refill watering can
				if (asList(validBlocks).contains(blockName))
					refillWateringCan(worldIn, playerIn, itemStackIn, blockName, blockPos);
				else // Water that block
					commenceWatering(worldIn, rayTraceVector, blockPos);
			}

			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
	}

	private void refillWateringCan(World worldIn, EntityPlayer playerIn, ItemStack itemStackIn, String blockName, BlockPos blockPos) {
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F); // Play sound
		worldIn.destroyBlock(blockPos, false); // Destroy block

		// Create bubbles
		for (int i=0; i<10; i++)
			worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, blockPos.getX() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), blockPos.getY() + 1, blockPos.getZ() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), 0.0D, 0.0D, 0.0D);

		// Check for/create NBT tag
		NBTTagCompound nbtCompound = itemStackIn.getTagCompound(); // Check if exists
		if (nbtCompound == null) // If not
			nbtCompound = new NBTTagCompound(); // Create new compound

		// Assign fluid based on block
		if (blockName.equals("water"))
			nbtCompound.setString("fluid", "water");
		else if (blockName.equals(WateringCans.MODID + ":growth_solution_block"))
			nbtCompound.setString("fluid", "growth_solution");

		nbtCompound.setByte("amount", (byte) 127); // Refill watering can
		itemStackIn.setTagCompound(nbtCompound); // Attach to itemstack
	}

	private void commenceWatering(World worldIn, Vec3d rayTraceVector, BlockPos blockPos) {
		// Create water particles
		// TODO: Color according to fluid type
		for (int i=0; i<25; i++)
			worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, rayTraceVector.xCoord + (worldIn.rand.nextGaussian() * 0.18D), rayTraceVector.yCoord, rayTraceVector.zCoord + (worldIn.rand.nextGaussian() * 0.18D), 0.0D, 0.0D, 0.0D);

		int reach = 3; // Total grid size to water
		int halfReach = (int) Math.floor(reach / 2); // Used to calculate offset in each direction

		for (int i=0; i<reach; i++) {
			for (int j=0; j<reach; j++) {

				// Create new block to affect
				BlockPos tempBlockPos = blockPos.add(i - halfReach, 0, j - halfReach); // Offset to center grid on selected block
				Block tempBlockObj = worldIn.getBlockState(tempBlockPos).getBlock(); // If block

				// Moisten soil
				if (tempBlockObj.getUnlocalizedName().equals("tile.farmland")) // Is farmland
					worldIn.setBlockState(tempBlockPos, Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7)); // Moisten it

				// Trigger tick updates
				if (!tempBlockObj.getUnlocalizedName().equals("tile.farmland")) // To avoid immediately untilling farmland
					worldIn.updateBlockTick(tempBlockPos, tempBlockObj, 10, 0);
			}
		}
	}
}
