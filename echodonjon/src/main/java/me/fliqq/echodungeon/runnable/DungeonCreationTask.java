package me.fliqq.echodungeon.runnable;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import me.fliqq.echodungeon.manager.DungeonManager;
import me.fliqq.echodungeon.manager.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;



public class DungeonCreationTask implements Runnable {

    private final JavaPlugin plugin;
    private final WorldManager worldManager;
    private final DungeonManager dungeonManager;

    public DungeonCreationTask(JavaPlugin plugin, WorldManager worldManager, DungeonManager dungeonManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
        this.dungeonManager=dungeonManager;
    }

    @Override
    public void run() {
        String worldName = "dungeon_" + System.currentTimeMillis();
        org.bukkit.World dungeonWorld = worldManager.createWorld(worldName);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Define the area where chunks will be placed (e.g., a 7x7 area around the spawn)
            int startX = -4;
            int startZ = -4;
            int endX = 4;
            int endZ = 4;

            // Place central dungeon spawn schematic (2x2 chunks)
            dungeonManager.placeChunk(BukkitAdapter.adapt(dungeonWorld), 0, 0, "dungeonspawn.schem");

            // Batch placement with increased delay
            int delayTicks = 5; // Increase delay to reduce lag
            int taskCounter = 0;

            for (int x = startX; x <= endX; x += 2) {
                for (int z = startZ; z <= endZ; z += 2) {
                    if (x == 0 && z == 0) continue; // Skip the central schematic

                    // Skip coordinates where both x and z are odd
                    if (Math.abs(x) % 2 == 1 && Math.abs(z) % 2 == 1) {
                        continue;
                    }

                    String schematicName = dungeonManager.getRandomSchematic();
                    int finalX = x;
                    int finalZ = z;

                    // Schedule each placeChunk call with a delay
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        dungeonManager.placeChunk(BukkitAdapter.adapt(dungeonWorld), finalX, finalZ, schematicName);
                    }, delayTicks * taskCounter);

                    taskCounter++;
                }
            }

            // Schedule the dungeon world to be deleted after 15 minutes
            Bukkit.getScheduler().runTaskLater(plugin, () -> worldManager.deleteWorld(dungeonWorld), 15 * 60 * 20L);
        }, 20L); // Delay of 1 second (20 ticks) to ensure the world is loaded
    }
}