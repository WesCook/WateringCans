package ca.wescook.wateringcans.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotions {
	public static PotionInvisible usingWateringCan;
	public static PotionInvisible slowPlayer;
	public static PotionInvisible inhibitFOV;

	public static void registerPotions() {
		// Using watering can
		usingWateringCan = new PotionInvisible(false, 0);
		usingWateringCan.setPotionName("usingWateringCan");
		usingWateringCan.setRegistryName("using_watering_can");
		GameRegistry.register(usingWateringCan);

		// Slow player
		slowPlayer = new PotionInvisible(true, 0);
		slowPlayer.setPotionName("slowPlayer");
		slowPlayer.setRegistryName("slow_player");
		slowPlayer.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "2a050f8c-07be-4e06-9d39-b6d299e0505f", -0.15D, 2);
		GameRegistry.register(slowPlayer);

		// Inhibit FOV changes
		inhibitFOV = new PotionInvisible(false, 0);
		inhibitFOV.setPotionName("inhibitFOV");
		inhibitFOV.setRegistryName("inhibit_fov");
		GameRegistry.register(inhibitFOV);
	}
}
