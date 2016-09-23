package ca.wescook.wateringcans;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MeshDefinitions implements ItemMeshDefinition {
	@Override
	public ModelResourceLocation getModelLocation(ItemStack itemStackIn) {

		NBTTagCompound nbtCompound = itemStackIn.getTagCompound(); // Get NBT data
		if (nbtCompound != null)
		{
			// Iron
			if (nbtCompound.getString("material").equals("iron"))
				return new ModelResourceLocation(WateringCans.MODID + ":watering_can_iron", "inventory");

			// Gold
			if (nbtCompound.getString("material").equals("gold"))
				return new ModelResourceLocation(WateringCans.MODID + ":watering_can_gold", "inventory");
		}

		// No assigned material (eg. in creative menu), fall back to iron
		return new ModelResourceLocation(WateringCans.MODID + ":watering_can_iron", "inventory");
	}
}
