package ca.wescook.wateringcans.items;

import ca.wescook.wateringcans.configs.Config;
import ca.wescook.wateringcans.particles.ParticleGrowthSolution;
import ca.wescook.wateringcans.potions.ModPotions;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
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
		setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	void render() {
		String[] wateringStates = new String[]{"true", "false"};

		// Register all possible item model combinations (once at runtime)
		for (String material : materials) { // All materials
			for (String wateringState : wateringStates) {
				// Register empty variant
				ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "currently_watering=" + wateringState + ",material=" + material + ",petals=empty"));

				// Register filled variants
				for (int i = 1; i < petalVariations; i++) { // Petal counts
					for (String fluid : fluids.keySet()) { // All fluids
						ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "currently_watering=" + wateringState + ",material=" + material + ",petals=" + fluid + "_" + i));
					}
				}
			}
		}

		// Custom Mesh Definitions (swap on the fly)
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack itemStackIn) {

				// Set material from NBT data
				NBTTagCompound nbtCompound = itemStackIn.getTagCompound();
				if (nbtCompound != null) {
					// Get NBT data
					String material = nbtCompound.getString("material");
					String fluid = nbtCompound.getString("fluid");

					// Get petal number
					byte petals = countPetals(itemStackIn);

					// Is player using watering can
					EntityPlayer player = Minecraft.getMinecraft().thePlayer;
					String currentlyWatering = "false";
					if (player.getActivePotionEffect(ModPotions.usingWateringCan) != null)
						currentlyWatering = "true";

					// Return dynamic texture location
					if (petals == 0)
						return new ModelResourceLocation(getRegistryName(), "currently_watering=" + currentlyWatering + ",material=" + material + ",petals=empty");
					else
						return new ModelResourceLocation(getRegistryName(), "currently_watering=" + currentlyWatering + ",material=" + material + ",petals=" + fluid + "_" + petals);
				}
				else {
					// NBT isn't set, may be spawned in
					System.out.println("Missing NBT data on watering can.  Setting default data.");
					itemStackIn.setTagCompound(getDefaultNBT());
				}

				// Rendering without assigned NBT data.  Return empty watering can.
				return new ModelResourceLocation(getRegistryName(), "currently_watering=false,material=iron,petals=empty");
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

	// Add material to name
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		// Assign based on material
		NBTTagCompound natCompound = stack.getTagCompound();
		if (natCompound != null)
			return "item." + getRegistryName().toString() + "_" + natCompound.getString("material");

		return "item." + getRegistryName().toString(); // Fall back to default
	}

	// Don't animate re-equipping item
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		if (oldStack.getItem() == newStack.getItem()) { // If item matches
			// Grab NBT data
			NBTTagCompound oldNBT = oldStack.getTagCompound();
			NBTTagCompound newNBT = newStack.getTagCompound();

			// If material, fluid type, and petal count match, don't reanimate
			if (oldNBT != null && newNBT != null) { // NBT exists
				if (oldNBT.getString("material").equals(newNBT.getString("material")) && oldNBT.getString("fluid") == newNBT.getString("fluid") && countPetals(oldStack) == countPetals(newStack))
					return false; // Only fluid amount changed, don't animate
			}
		}

		return true; // Something bigger changed, animate
	}

	// Add tooltips
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null) {
			if (compound.getShort("amount") == 0) { // If empty
				tooltip.add(I18n.format("tooltip.wateringcans:watering_can_" + compound.getString("material"), TextFormatting.DARK_GRAY)); // Display material tooltip
			} else {
				String fluid = compound.getString("fluid"); // Get fluid type
				tooltip.add(I18n.format("tooltip.wateringcans:contains") + ": " + I18n.format(fluids.get(fluid))); // Get localization string of fluid and add to tooltip
				tooltip.add(I18n.format("tooltip.wateringcans:remaining") + ": " + compound.getShort("amount"));
			}
		}
	}


	// On right click
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		// List of valid fluid blocks
		String[] validBlocks = new String[]{"water", MODID + ":growth_solution_block"};

		// Ray trace - find block we're looking at
		RayTraceResult rayTraceResult = this.rayTrace(worldIn, playerIn, true);

		// Check for/create NBT tag
		NBTTagCompound nbtCompound = itemStackIn.getTagCompound(); // Check if exists
		if (nbtCompound == null) { // If not
			nbtCompound = new NBTTagCompound(); // Create new compound
			itemStackIn.setTagCompound(nbtCompound); // Attach to itemstack
		}

		// If sky, ignore
		if (rayTraceResult != null) {
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
					refillWateringCan(worldIn, playerIn, nbtCompound, blockName, blockPos);
				else // Water that block
					commenceWatering(worldIn, playerIn, itemStackIn, nbtCompound, rayTraceVector, blockPos);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn); // PASS instead of SUCCESS so we can dual wield watering cans
	}

	private void refillWateringCan(World worldIn, EntityPlayer playerIn, NBTTagCompound nbtCompound, String blockName, BlockPos blockPos) {
		// If gold, grant one refill
		if (nbtCompound.getString("material").equals("gold")) {
			if (!nbtCompound.getBoolean("filledOnce"))
				nbtCompound.setBoolean("filledOnce", true); // Set flag once and continue
			else {
				worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F); // Tool break sound
				return; // Exit method
			}
		}

		// Water refill sound
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		// Destroy source block
		if (!playerIn.isCreative())
			worldIn.setBlockToAir(blockPos);

		// Create bubbles
		if (worldIn.isRemote) {
			for (int i = 0; i < 15; i++)
				worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, blockPos.getX() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), blockPos.getY() + 1, blockPos.getZ() + 0.5 + (worldIn.rand.nextGaussian() * 0.3D), 0.0D, 0.0D, 0.0D);
		}

		// Assign fluid based on block
		if (blockName.equals("water"))
			nbtCompound.setString("fluid", "water");
		else if (blockName.equals(MODID + ":growth_solution_block"))
			nbtCompound.setString("fluid", "growth_solution");

		// Refill watering can
		nbtCompound.setShort("amount", fluidCapacity);
	}

	private void commenceWatering(World worldIn, EntityPlayer playerIn, ItemStack itemStackIn, NBTTagCompound nbtCompound, Vec3d rayTraceVector, BlockPos rayTraceBlockPos) {
		// If water remains in can
		short amountRemaining = nbtCompound.getShort("amount");
		if (amountRemaining > 0) {

			// Set player as currently watering (via potions because onItemUseFinish is too limiting)
			playerIn.addPotionEffect(new PotionEffect(ModPotions.usingWateringCan, 6, 0, false, false)); // Set player to "using can"

			// Slow player using obsidian can
			if (nbtCompound.getString("material").equals("obsidian")) {
				playerIn.addPotionEffect(new PotionEffect(ModPotions.slowPlayer, 5, 5, false, false)); // Slow player
				playerIn.addPotionEffect(new PotionEffect(ModPotions.inhibitFOV, 10, 0, false, false)); // Apply secondary, slightly longer potion effect to inhibit FOV changes from slowness
			}

			// Play watering sound
			worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.WEATHER_RAIN, SoundCategory.BLOCKS, 0.12F, 1.85F);

			// Create water particles
			if (worldIn.isRemote) { // Client only
				for (int i = 0; i < 25; i++) {
					if (nbtCompound.getString("fluid").equals("water"))
						worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, rayTraceVector.xCoord + (worldIn.rand.nextGaussian() * 0.18D), rayTraceVector.yCoord, rayTraceVector.zCoord + (worldIn.rand.nextGaussian() * 0.18D), 0.0D, 0.0D, 0.0D);
					else if (nbtCompound.getString("fluid").equals("growth_solution"))
						ParticleGrowthSolution.spawn(worldIn, rayTraceVector.xCoord + (worldIn.rand.nextGaussian() * 0.18D), rayTraceVector.yCoord, rayTraceVector.zCoord + (worldIn.rand.nextGaussian() * 0.18D), 0.0D, 0.0D, 0.0D);
				}
			}

			// Calculate watering can reach
			int reach;
			if (nbtCompound.getString("material").equals("obsidian"))
				reach = 5; // If obsidian, increase reach
			else
				reach = 3;

			// Used to calculate offset in each direction
			int halfReach = (int) Math.floor(reach / 2);

			// Calculate growth speed
			float growthSpeed;

			if (Config.growthMultiplier != 0.0F) { // Avoid dividing by zero
				growthSpeed = 6; // Initial speed
				if (nbtCompound.getString("fluid").equals("growth_solution")) // Fluid multiplier
					growthSpeed *= 2.5F;
				if (nbtCompound.getString("material").equals("gold"))  // Gold can multiplier
					growthSpeed *= 1.5F;
				growthSpeed = 30F - growthSpeed; // Lower is actually faster, so invert
				growthSpeed = (float) Math.ceil(growthSpeed / Config.growthMultiplier); // Divide by config setting (0-10) as multiplier
			}
			else {
				growthSpeed = 0.0F;
			}

			// Put out entity fires
			List<EntityLivingBase> affectedMobs = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(rayTraceBlockPos.add(-halfReach, -1, -halfReach), rayTraceBlockPos.add(halfReach + 1, 2, halfReach + 1))); // Find mobs
			for (EntityLivingBase mob : affectedMobs) // Loop through found mobs/players
				mob.extinguish(); // Extinguish fire

			// Iterate through total reach
			for (int i=0; i<reach; i++) {
				for (int j=0; j<reach; j++) {
					for (int k=-1; k<2; k++) { // Go down one layer, up two layers

						// Calculate new block position from reach and current Y level
						BlockPos newBlockPos = rayTraceBlockPos.add(i - halfReach, k, j - halfReach);
						Block newBlockObj = worldIn.getBlockState(newBlockPos).getBlock();

						// Put out block fires
						if (newBlockObj.getRegistryName().toString().equals("minecraft:fire")) { // If fire
							worldIn.setBlockToAir(newBlockPos); // Remove it
							worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F); // Fire extinguish sound
						}

						// Moisten soil/Tick Updates
						if (newBlockObj.getRegistryName().toString().equals("minecraft:farmland")) // If farmland
							worldIn.setBlockState(newBlockPos, Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, 7)); // Moisten it
						else // If not farmland, to avoid immediately untilling
							worldIn.updateBlockTick(newBlockPos, newBlockObj, (int) growthSpeed, 0); // Do tick updates
					}
				}
			}

			// Decrease fluid amount
			if (amountRemaining > 0 && !playerIn.isCreative()) {
				if (nbtCompound.getString("material").equals("stone")) // If stone
					nbtCompound.setShort("amount", (short) (amountRemaining - 2)); // Drain quicker (simulate smaller tank)
				else
					nbtCompound.setShort("amount", (short) (amountRemaining - 1));
			}
		}
		else {
			// If gold can is empty, destroy it
			if (nbtCompound.getString("material").equals("gold") && nbtCompound.getBoolean("filledOnce")) {

				// Get slot of active watering can (hand or hotbar)
				int slot = playerIn.inventory.getSlotFor(itemStackIn); // Get ID from itemstack (returns -1 in offhand)
				final int handSlot = 40; // Offhand slot

				if (slot == -1) { // Probably in offhand, but let's verify
					if (playerIn.inventory.getStackInSlot(handSlot) == itemStackIn) // Checking offhand
						slot = handSlot; // It's in the offhand
				}

				playerIn.inventory.setInventorySlotContents(slot, null); // Delete item
				worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F); // Tool break sound
			}
		}
	}

	// Calculate petals from NBT "amount" in ItemStack
	private byte countPetals(ItemStack stack) {
		// Get NBT data
		NBTTagCompound nbtCompound = stack.getTagCompound();
		if (nbtCompound != null) {
			Short amountRemaining = nbtCompound.getShort("amount");

			// Behavior: 8 petals is completely full, 0 petals is completely empty.
			// 1-7 petals are in-between states, rounded up as a percentage of the max storage amount
			return (byte) Math.ceil(((double) amountRemaining / (fluidCapacity - 1)) * (petalVariations - 1 - 1));
		}

		return (byte) -1; // Error
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
}
