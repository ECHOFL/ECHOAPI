package me.fliqq.echodungeon.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.mineacademy.fo.plugin.SimplePlugin;

import me.fliqq.echodungeon.model.EmptyChunkGenerator;


public class WorldManager {

	private final SimplePlugin plugin;
	private final List<World> activeWorlds;
	
	public WorldManager(SimplePlugin plugin) {
		this.plugin=plugin;
		this.activeWorlds = new ArrayList<>();
	}
	
	public World createWorld(String worldName) {
		WorldCreator creator = new WorldCreator(worldName);
		creator.generator(new EmptyChunkGenerator());
		World world = creator.createWorld();
		activeWorlds.add(world);
		return world;
	}
	
	public void deleteWorld(World world) {
		Bukkit.unloadWorld(world, false);
		File worldFolder = world.getWorldFolder();
		try {
			FileUtils.deleteDirectory(worldFolder);
		}catch (IOException e) {
			e.printStackTrace();
		}
		activeWorlds.remove(world);
	}
	
	public void cleanupWorlds() {
		for(World world : new ArrayList<>(activeWorlds)) {
			deleteWorld(world);
		}
	}
    public List<World> getActiveWorlds() {
        return activeWorlds;
    }

    public World getWorld(String name) {
        for (World world : activeWorlds) {
            if (world.getName().equalsIgnoreCase(name)) {
                return world;
            }
        }
        return null;
    }
    public List<String> getDungeonWorldNames() {
        return Bukkit.getWorlds().stream()
                .filter(world -> world.getName().startsWith("dungeon_"))
                .map(World::getName)
                .collect(Collectors.toList());
    }
}
