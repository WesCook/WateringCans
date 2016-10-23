package ca.wescook.wateringcans.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.world.World;

public class ParticleGrowthSolution extends ParticleSplash {

	// Forwarding all the same arguments
	private ParticleGrowthSolution(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.setRBGColorF(0.1F, 1.0F, 0.1F); // Set green color
		this.particleAlpha = 0.7F; // Set alpha
	}

	// Method called directly to spawn particles
	public static void spawn(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGrowthSolution(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn));
	}
}