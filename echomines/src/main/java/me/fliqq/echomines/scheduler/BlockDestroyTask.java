package me.fliqq.echomines.scheduler;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;


public class BlockDestroyTask extends BukkitRunnable{

	private final World world;
	private final int xMin,yMin,zMin,xMax,yMax,zMax;
	private int currentX, currentY, currentZ;
	private static final int BATCH_SIZE = 10000;

	
	public BlockDestroyTask(World world, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
		this.world = world;
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.zMax = zMax;
		this.currentX = xMin;
		this.currentY = yMax;
		this.currentZ = zMin;
	}


	@Override
	public void run() {
		int blockProcessed = 0;
		while (blockProcessed < BATCH_SIZE) {
			if(currentX > xMax) {
				currentX = xMin;
				currentZ++;
			}
			if(currentZ > zMax) {
				currentZ = zMin;
				currentY--;
			}
			if(currentY < yMin) {
				cancel();
				return;
			}
		
			Block block = world.getBlockAt(currentX,currentY,currentZ);
			
			if(block.getType()!=Material.AIR)	
				block.setType(Material.AIR);
			blockProcessed++;
			currentX++;
		}
	}
}
