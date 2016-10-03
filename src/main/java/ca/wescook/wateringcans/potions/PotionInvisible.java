package ca.wescook.wateringcans.potions;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

// A simple potion implementation to avoid visible rendering in the inventory screen
public class PotionInvisible extends Potion {
	PotionInvisible(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
	}

	@Override
	public boolean shouldRender(PotionEffect effect) {
		return false;
	}
}
