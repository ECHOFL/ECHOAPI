package me.fliqq.echomines.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echomines.enom.SolidBlocks;
import me.fliqq.echomines.enom.TerracottaAndConcreteBlocks;

public class BlockResetTask extends BukkitRunnable {

    private final World world;
    private final int xMin, yMin, zMin, xMax, zMax;
    private int currentX, currentY, currentZ;
    private static final int BATCH_SIZE = 5000;
    private final BlockData mineMaterial;

    public BlockResetTask(World world, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.world = world;
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.zMax = zMax;
        this.currentX = xMin;
        this.currentY = yMax;
        this.currentZ = zMin;
        
        // Select a random block type for the entire task
        TerracottaAndConcreteBlocks randomBlock = TerracottaAndConcreteBlocks.getRandomBlock();
        this.mineMaterial = Bukkit.createBlockData(randomBlock.getMaterial());
    }

    @Override
    public void run() {
        int blockProcessed = 0;
        while (blockProcessed < BATCH_SIZE) {
            if (currentX > xMax) {
                currentX = xMin;
                currentZ++;
            }
            if (currentZ > zMax) {
                currentZ = zMin;
                currentY--;
            }
            if (currentY < yMin) {
                cancel();
                return;
            }

            Block block = world.getBlockAt(currentX, currentY, currentZ);
            if (block.getType() == Material.AIR) {
                block.setBlockData(mineMaterial);
                blockProcessed++;
            }
            currentX++;
        }
    }
}