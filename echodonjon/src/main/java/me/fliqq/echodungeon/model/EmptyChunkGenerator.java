package me.fliqq.echodungeon.model;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world); // Creating an empty chunk data
        // You can customize the chunk data here if needed
        return chunkData;
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        // No terrain generation, keeping it empty
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }
}