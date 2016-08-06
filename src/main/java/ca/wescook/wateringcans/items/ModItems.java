package ca.wescook.wateringcans.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	public static ItemWateringCan itemWateringCan;

	public static void createItems() {
		itemWateringCan = new ItemWateringCan();
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		itemWateringCan.initModel();
	}
}
