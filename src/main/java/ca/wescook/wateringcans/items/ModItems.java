package ca.wescook.wateringcans.items;

public class ModItems {
	public static ItemWateringCan itemWateringCan;

	public static void registerItems() {
		itemWateringCan = new ItemWateringCan();
	}

	public static void renderItems() {
		itemWateringCan.render();
	}
}
