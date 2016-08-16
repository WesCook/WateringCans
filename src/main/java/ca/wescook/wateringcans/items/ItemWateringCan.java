package ca.wescook.wateringcans.items;

import ca.wescook.wateringcans.WateringCans;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static java.util.Arrays.asList;

class ItemWateringCan extends Item {
	ItemWateringCan() {
		setRegistryName("watering_can");
		setUnlocalizedName(WateringCans.MODID + ".watering_can");
		GameRegistry.register(this);
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
		// Ray trace - find block we're looking at
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

		// If sky, ignore
		if (raytraceresult == null) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		else {
			// If a block is found (includes fluids)
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {

				// Collect information about block
				BlockPos blockpos = raytraceresult.getBlockPos(); // Get block position from ray trace
				Block selectedBlock = worldIn.getBlockState(blockpos).getBlock(); // Get block object
				String blockNameRaw = selectedBlock.getUnlocalizedName(); // Get block name
				String blockName = blockNameRaw.substring(5); // Clean .tile prefix

				// Make a list of valid blocks
				String[] validBlocks = new String[]{"water", WateringCans.MODID + ":growth_solution_block"};

				// If found block is in list
				if (asList(validBlocks).contains(blockName)) {
					worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F); // Play sound
					worldIn.destroyBlock(blockpos, false); // Destroy block

					// If water
					if (blockName.equals("water")) {
						System.out.println("Water");
					}
					// If growth solution
					else if (blockName.equals(WateringCans.MODID + ":growth_solution_block")) {
						System.out.println("Growth solution");
					}
				}
			}

			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
	}
}
