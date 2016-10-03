package ca.wescook.wateringcans.events;

import ca.wescook.wateringcans.potions.ModPotions;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EventFOV {
	@SubscribeEvent
	public void fovUpdates(FOVUpdateEvent event) {
		// Get player object
		EntityPlayer player = event.getEntity();

		if (player.getActivePotionEffect(ModPotions.inhibitFOV) != null) {
			// Get player data
			double playerSpeed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
			float capableSpeed = player.capabilities.getWalkSpeed();
			float fov = event.getFov();

			// Disable FOV change
			event.setNewfov((float) (fov / ((playerSpeed / capableSpeed + 1.0) / 2.0)));
		}
	}
}
