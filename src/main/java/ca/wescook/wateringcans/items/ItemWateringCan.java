package ca.wescook.wateringcans.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
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

import java.util.List;

import static ca.wescook.wateringcans.WateringCans.*;
import static java.util.Arrays.asList;
import static net.minecraft.block.BlockFarmland.MOISTURE;

public class ItemWateringCan extends Item {
	ItemWateringCan() {
		setRegistryName("watering_can");
		setUnlocalizedName(getRegistryName().toString());
		GameRegistry.register(this);
		setCreativeTab(CreativeTabs.TOOLS);
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	void render() {
		// Register all possible item model combinations (once at runtime)
		for (String material : materials) { // All materials
			// Register empty variant
			ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "material=" + material + ",petals=empty"));

			// Register filled variants
			for (int i=1; i<petalVariations; i++) { // Petal counts
				for (String fluid : fluids) { // All fluids
					ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "material=" + material + ",petals=" + fluid + "_" + i));
				}
			}
		}

		// Custom Mesh Definitions (swap on the fly)
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack itemStackIn) {

				// Set material from NBT data
				NBTTagCompound nbtCompound = itemStackIn.getTagCompound();
				if (nbtCompound != null)
				{
					// Get NBT data
					String material = nbtCompound.getString("material");
					Short amountRemaining = nbtCompound.getShort("amount");
					String fluid = nbtCompound.getString("fluid");

					// Calculate petals from amount
					// Behavior: 8 petals is completely full, 0 petals is completely empty.
					// 1-7 petals are in-between states, rounded up as a percentage of the max storage amount
					byte petals = (byte) Math.ceil(((double) amountRemaining / (fluidCapacity - 1)) * (petalVariations - 1 - 1));

					// Return dynamic texture location
					if (petals == 0)
						return new ModelResourceLocation(getRegistryName(), "material=" + material + ",petals=empty");
					else
						return new ModelResourceLocation(getRegistryName(), "material=" + material + ",petals=" + fluid + "_" + petals);
				}
				else {
					// NBT isn't set, may be spawned in
					System.out.println("Missing NBT data on watering can.  Setting default data.");
					itemStackIn.setTagCompound(getDefaultNBT());
				}

				// Rendering without assigned NBT data.  Return empty watering can.
				return new ModelResourceLocation(getRegistryName(), "material=iron,petals=empty");
			}
		});
	}

	// Add creative menu variants
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (String material : materials) { // Loop through materials
			ItemStack tempItem = new ItemStack(itemIn); // Create ItemStack
			NBTTagCompound nbtCompound = getDefaultNBT(); // Create compound from NBT defaults
			nbtCompound.setString("material", material); // Overwrite material tag
			tempItem.setTagCompound(nbtCompound); // Assign tag to ItemStack
			list.add(tempItem); // Add to creative menu
		}
	}

	// Apply some default NBT on item creation
	// From crafting, spawning in, or creative menu
	public static NBTTagCompound getDefaultNBT() {
		NBTTagCompound nbtCompound = new NBTTagCompound();
		nbtCompound.setString("material", "iron");
		nbtCompound.setString("fluid", "water");
		nbtCompound.setShort("amount", (short) 0);
		return nbtCompound;
	}

	// On right click
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		// List of valid fluid blocks
		String[] validBlocks = new String[]{"water", MODID + ":growth_solution_block"};

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
				if (nbtCompound == null) { // If not
					nbtCompound = new NBTTagCompound(); // Create new compound
					itemStackIn.setTagCompound(nbtCompound); // Attach to itemstack
				}

				// If found block is in fluid list, refill watering can
				if (asList(validBlocks).contains(blockName))
					refillWateringCan(worldIn, playerIn, nbtCompound, blockName, blockPos);
				else // Water that block
					commenceWatering(worldIn, nbtCompound, rayTraceVector, blockPos);
			}

			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
	}

	private void refillWateringCan(World worldIn, EntityPlayer playerIn, NBTTagCompound nbtCompound, String blockName, BlockPos blockPos) {
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F); // Play sound
		worldIn.destroyBlock(blockPos, false); // Destroy block

		// Create bubbles
		for (int i=0; i<10; i++)
			worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, blockPos.getX() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), blockPos.getY() + 1, blockPos.getZ() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), 0.0D, 0.0D, 0.0D);

		// Assign fluid based on block
		// TODO: Add tooltips for fluid/amount
		if (blockName.equals("water"))
			nbtCompound.setString("fluid", "water");
		else if (blockName.equals(MODID + ":growth_solution_block"))
			nbtCompound.setString("fluid", "growth_solution");

		// Refill watering can
		nbtCompound.setShort("amount", fluidCapacity);
	}

	private void commenceWatering(World worldIn, NBTTagCompound nbtCompound, Vec3d rayTraceVector, BlockPos blockPos) {
		// If water remains in can
		short amountRemaining = nbtCompound.getShort("amount");
		if (amountRemaining > 0) {

			// Create water particles
			for (int i=0; i<25; i++) {
				// TODO: Color according to fluid type
				worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, rayTraceVector.xCoord + (worldIn.rand.nextGaussian() * 0.18D), rayTraceVector.yCoord, rayTraceVector.zCoord + (worldIn.rand.nextGaussian() * 0.18D), 0.0D, 0.0D, 0.0D);
			}

			// Calculate watering can reach
			int reach;
			if (nbtCompound.getString("material").equals("obsidian")) // If obsidian, increase reach
				reach = 5;
			else
				reach = 3;

			int halfReach = (int) Math.floor(reach / 2); // Used to calculate offset in each direction

			// Iterate through total reach
			for (int i=0; i<reach; i++) {
				for (int j=0; j<reach; j++) {

					// Get new block objects to affect
					BlockPos tempBlockPos = blockPos.add(i - halfReach, 0, j - halfReach); // Offset to center grid on selected block
					Block tempBlockObj = worldIn.getBlockState(tempBlockPos).getBlock();

					// Put out fire
					for (int k=-1; k<2; k++) { // Go down one, and up two block layers
						if (worldIn.getBlockState(tempBlockPos.add(0, k, 0)).getBlock().getUnlocalizedName().equals("tile.fire")) { // If fire
							worldIn.setBlockToAir(tempBlockPos.add(0, k, 0)); // Extinguish it
						}
					}

					// Moisten soil
					for (int k=-1; k<0; k++) { // Go down one layer
						if (worldIn.getBlockState(tempBlockPos.add(0, k, 0)).getBlock().getUnlocalizedName().equals("tile.farmland")) // If block is farmland
							worldIn.setBlockState(tempBlockPos.add(0, k, 0), Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7)); // Moisten it
					}

					// Trigger tick updates
					if (!tempBlockObj.getUnlocalizedName().equals("tile.farmland")) { // To avoid immediately untilling farmland
						// Calculate growth speed
						float growthSpeed = 6; // Initial speed

						// Multipliers
						if (nbtCompound.getString("fluid").equals("growth_solution"))
							growthSpeed *= 2.5;
						if (nbtCompound.getString("material").equals("gold"))
							growthSpeed *= 1.5;

						// Lower is actually faster, so invert
						growthSpeed = 30 - growthSpeed;

						// Do tick updates
						worldIn.updateBlockTick(tempBlockPos, tempBlockObj, (int) growthSpeed, 0);
					}
				}
			}

			// Decrease fluid amount
			// TODO: See if NBT can be updated without resetting held item
			if (amountRemaining > 0) {
				if (nbtCompound.getString("material").equals("stone")) // If stone
					nbtCompound.setShort("amount", (short) (amountRemaining - 2)); // Drain quicker (simulate smaller tank)
				else
					nbtCompound.setShort("amount", (short) (amountRemaining - 1));
			}
		}
	}
}
