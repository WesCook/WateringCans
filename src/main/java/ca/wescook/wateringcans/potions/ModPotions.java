package ca.wescook.wateringcans.potions;

import net.minecraft.entity.SharedMonsterAttributes;

public class ModPotions {
	public static PotionInvisible potionInvSlow;
	public static PotionInvisible inhibitFOV;

	public static void registerPotions() {
		// Slow player
		potionInvSlow = new PotionInvisible(true, 0);
		potionInvSlow.setPotionName("invSlow");
		potionInvSlow.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "2a050f8c-07be-4e06-9d39-b6d299e0505f", -0.15D, 2);

		// Inhibit FOV changes
		inhibitFOV = new PotionInvisible(true, 0);
		inhibitFOV.setPotionName("inhibitFOV");
	}
}
