package me.fliqq.echomines.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.fliqq.echomines.EchoMines;



public class Regions {
	
	//SINGLETON 
	
    private static Regions instance;
    private final Set<Region> regions = new HashSet<>();
    private final Set<Mines> mines = new HashSet<>();
    
    private File file;
    private YamlConfiguration config;

    private Regions() {
        File dataFolder = EchoMines.getInstance().getDataFolder();
        if (!dataFolder.exists()) {
            EchoMines.logger.info("Data folder not found, creating...");
            dataFolder.mkdirs();
        }

        file = new File(dataFolder, "regions.yml");
        config = new YamlConfiguration();
    }
    
    public static Regions getInstance() {
        if (instance == null) {
            instance = new Regions();
        }
        return instance;
    }

    public void load() {
        try {
            if (!file.exists()) {
                EchoMines.logger.info("Regions file not found, creating a new one.");
                file.createNewFile();
            }

            config.load(file);
            EchoMines.logger.info("Regions file loaded successfully.");

            regions.clear();
            mines.clear();

            if (config.isSet("Regions")) {
                for (Map<?, ?> rawRegionMap : config.getMapList("Regions")) {
                    regions.add(Region.deserialize((Map<String, Object>) rawRegionMap)); 
                   // EchoMines.logger.info("Loaded region: " + rawRegionMap);
                }
            } else {
                EchoMines.logger.info("No regions found in the configuration.");
            }

            if (config.isSet("Mines")) {
                for (Map<?, ?> rawMineMap : config.getMapList("Mines")) {
                    mines.add(Mines.deserialize((Map<String, Object>) rawMineMap));
                    EchoMines.logger.info("Loaded mine: " + rawMineMap);
                }
            } else {
                EchoMines.logger.info("No mines found in the configuration.");
            }

        } catch (IOException e) {
            EchoMines.logger.log(Level.SEVERE, "Failed to create regions file.", e);
        } catch (Throwable t) {
            EchoMines.logger.log(Level.SEVERE, "Failed to load regions file.", t);
        }
    }

    public void save() {
        List<Map<String, Object>> serializedRegions = new ArrayList<>();

        for (Region region : regions) {
            serializedRegions.add(region.serialize());
            EchoMines.logger.info("Serialized region: " + region.getName());
        }

        config.set("Regions", serializedRegions);

        List<Map<String, Object>> serializedMines = new ArrayList<>();

        for (Mines mine : mines) {
            serializedMines.add(mine.serialize());
            EchoMines.logger.info("Serialized mine: " + mine.getName());
        }

        config.set("Mines", serializedMines);

        try {
            config.save(file);
            EchoMines.logger.info("Regions file saved successfully.");
        } catch (Exception ex) {
            EchoMines.logger.log(Level.SEVERE, "Failed to save regions file.", ex);
        }
    }

    public Region findRegion(Location location) {
        for (Region region : regions) {
            if (region.isWithin(location)) {
                return region;
            }
        }
        return null;
    }

    public Region findRegion(String name) {
        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return region;
            }
        }

        return null;
    }

    public Mines findMine(Location location) {
        for (Mines mine : mines) {
            if (mine.isWithin(location)) {
                return mine;
            }
        }
        return null;
    }

    public Mines findMine(String name) {
        for (Mines mine : mines) {
            if (mine.getName().equalsIgnoreCase(name)) {
                return mine;
            }
        }
        return null;
    }

    public Set<Region> getRegions() {
        return Collections.unmodifiableSet(regions);
    }

    public Set<Mines> getMines() {
        return Collections.unmodifiableSet(mines);
    }

    public Set<String> getRegionsNames() {
        Set<String> names = new HashSet<>();

        for (Region region : regions) {
            names.add(region.getName());
        }

        return Collections.unmodifiableSet(names);
    }

    public Set<String> getMinesNames() {
        Set<String> names = new HashSet<>();

        for (Mines mine : mines) {
            names.add(mine.getName());
        }

        return Collections.unmodifiableSet(names);
    }

    public void saveRegion(String name, Location primary, Location secondary) {
        regions.add(new Region(name, primary, secondary));
        save();
    }

    public void saveMine(String name, Mines mine) {
        mines.add(mine);
        save();
    }

    public void saveMine(String name, Location primary, Location secondary, 
    		Map<String, Integer> blockPercentages, Location spawnLocation,
    		int resetPourcent, int resetDelai) {
        mines.add(new Mines(name, primary, secondary, blockPercentages, spawnLocation,resetPourcent,resetDelai));
        save();
    }

    public String getMineNameFromLocation(Location location) {
        for (Mines mine : mines) {
            if (mine.isWithin(location)) {
                return mine.getName();
            }
        }
        return null; // Aucun nom de mine trouvé pour cette localisation
    }
    public Set<Player> getPlayersInRegion(Region region) {
        Set<Player> players = new HashSet<>();
        World world = region.getPrimaryLocation().getWorld();

        for (Player player : world.getPlayers()) {
            if (region.isWithin(player.getLocation())) {
                players.add(player);
            }
        }

        return players;
    }

    public Set<Player> getPlayersInMine(Mines mine) {
        Set<Player> players = new HashSet<>();
        World world = mine.getPrimaryLocation().getWorld();

        for (Player player : world.getPlayers()) {
            if (mine.isWithin(player.getLocation())) {
                players.add(player);
            }
        }

        return players;
    }
}