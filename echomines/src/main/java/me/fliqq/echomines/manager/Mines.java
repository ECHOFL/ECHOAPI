package me.fliqq.echomines.manager;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import me.fliqq.echomines.EchoMines;
import me.fliqq.echopickaxe.manager.PlayerEnchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;




public class Mines extends Region{
	
    private static final int CHUNK_SIZE = 16;
    private static final int LAYER_HIGH = 8;
    private static final long LAYER_DELAY_TICKS =1L;
    private static final long CHUNK_DELAY_TICKS = 1L; // Délai entre les tranches de blocs dans un chunk
 

	
    private Map<String, Integer> blockPercentages;
    private int resetPourcent;
    private int resetDelai;
    private Location spawnLocation;

    public Mines(String name, Location primaryLocation, Location secondaryLocation,
                       Map<String, Integer> blockPercentages, Location spawnLocation, int resetPourcent, int resetDelai) {
    	
        super(name, primaryLocation, secondaryLocation);
        this.blockPercentages = blockPercentages;
        this.spawnLocation = spawnLocation;
        this.setResetDelai(resetDelai);
        this.setResetPourcent(resetPourcent);
    }
    public Location getSpawn() {
		return this.spawnLocation;
	}

    public Map<String, Integer> getBlockPercentages() {
        return blockPercentages;
    }


    public void setBlockPercentages(Map<String, Integer> blockPercentages) {
        this.blockPercentages = blockPercentages;
    }

    public void updateBlockPercentage(String blockType, int percentage) {
        blockPercentages.put(blockType, percentage);
    }

    public int getResetPourcent() {
        return resetPourcent;
    }

    public void setResetPourcent(int resetPourcent) {
        this.resetPourcent = resetPourcent;
    }

    public int getResetDelai() {
        return resetDelai;
    }

    public void setResetDelai(int resetDelai) {
        this.resetDelai = resetDelai;
    }

    public void setSpawn(Location location) {
        this.spawnLocation = location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("BlockPercentages", blockPercentages);
        map.put("spawnLocation", spawnLocation);
        map.put("resetPourcent", resetPourcent);
        map.put("resetDelai", resetDelai);
        return map;
    }

    public static Mines deserialize(Map<String, Object> map) {
        String name = (String) map.get("Name");
        Location primary = (Location) map.get("Primary");
        Location secondary = (Location) map.get("Secondary");
        Location spawnLocation = (Location) map.get("spawnLocation");
        int resetPourcent = (int) map.get("resetPourcent");
        int resetDelai = (int) map.get("resetDelai");
        Map<String, Integer> blockPercentages = (Map<String, Integer>) map.get("BlockPercentages");
        return new Mines(name, primary, secondary, blockPercentages, spawnLocation, resetPourcent, resetDelai);
    }

    public void resetBlocksOnlyOne(Block block) {
        block.setType(Material.matchMaterial(getFirstKey(blockPercentages)));
    }

    public String getFirstKey(Map<String, Integer> map) {
        return map.keySet().stream().findFirst().orElse(null);
    }
    
    public int getWidthX() {
        return getHighX() - getLowX();
    }
    
    public int getHeightY() {
        return getHighY() - getLowY();
    }
    
    public int getDepthZ() {
        return getHighZ() - getLowZ();
    }
   
    public int getLowX() {
        return getCorrectedPoints()[0].getBlockX();
    }
    public int getHighX() {
        return getCorrectedPoints()[1].getBlockX();

    }
    public int getLowY() {
        return getCorrectedPoints()[0].getBlockY();
    }
    
    public int getHighY() {
        return getCorrectedPoints()[1].getBlockY();
    }
    public int getLowZ() {
        return getCorrectedPoints()[0].getBlockZ();
    }

    public int getHighZ() {
        return getCorrectedPoints()[1].getBlockZ();
    }


    public double getAirPercentage() {
        Location primaryLocation = getPrimaryLocation();
        if (primaryLocation == null) {
            EchoMines.logger.warning("Primary location is null for mine: " + getName());
            return 0;
        }

        World world = primaryLocation.getWorld();
        if (world == null) {
            EchoMines.logger.warning("World is null for primary location of mine: " + getName());
            return 0;
        }

        int totalBlocks = 0;
        int airBlocks = 0;
        int minX = getLowX();
        int maxX = getHighX();
        int minY = getLowY();
        int maxY = getHighY();
        int minZ = getLowZ();
        int maxZ = getHighZ();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) {
                        airBlocks++;
                    }
                    totalBlocks++;
                }
            }
        }

        if (totalBlocks == 0) {
            return 0; // Avoid division by zero
        }

        double airPercentage = ((double) airBlocks / totalBlocks) * 100;
        return (double) Math.round(airPercentage);
    }
   

    
    public void transformParallelepipedInLayersAsync() {

    	int heigth = getHeightY();
        int layers = (int) Math.ceil((double) getHeightY() / LAYER_HIGH); // Nombre de couches
    	int lowY = getLowY();
    	int highY = getHighY();
        for (int y = highY; y >= lowY; y--) {
            int currentLayer = y;
            int delai = Math.abs(currentLayer-(lowY+heigth));
            new BukkitRunnable() {
            	int lowX = getLowX();
            	int highX = getHighX();
            	int lowZ = getLowZ();
            	int highZ = getHighZ();
            	
            	World world = getPrimaryLocation().getWorld();
                @Override
                public void run() {
                    //List<Chunk> chunks = new ArrayList<>();
                    //int currentHighY = highY - currentLayer*LAYER_HIGH;
                    //int currentLowY = Math.max(highY-height, currentHighY-LAYER_HIGH);

                    for(int x = lowX; x <= highX; x++) {
                    	for(int z = lowZ; z <= highZ; z++) {
                            Block block = world.getBlockAt(x,currentLayer,z);
                            if (block.getType() == Material.AIR){
                                setBlockLater(block);
                            }
                    	}
                    }
                }
            }.runTaskLater(EchoMines.getInstance(), delai);
        }
    }


    private void setBlockLater(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                resetBlocksOnlyOne(block);
            }
        }.runTask(EchoMines.getInstance());
    }
    
	public void teleportPlayerInMine(Set<Player> playersInMine) {
		for(Player player : playersInMine) {
			player.teleport(getSpawn());
			int effectDuration = (getDepthZ() * getWidthX()) / 100;

			// Add potion effects to the player
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, effectDuration, 30, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effectDuration, 30, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, effectDuration, 30, false, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, effectDuration, -30, false, false)); // Prevent jumping

			if(player.getAllowFlight()) {
			    player.setAllowFlight(false);
			    player.setFlying(false);
			    
			    // Calculate the delay in ticks
			    int delay = effectDuration;
			    
			    // Schedule a task to re-enable flight after the delay
			    Bukkit.getScheduler().runTaskLater(EchoMines.getInstance(), new Runnable() {
			        @Override
			        public void run() {
			            // Re-enable flight
			            player.setAllowFlight(true);
			        }
			    }, delay);
			}
			
		}
		
	}
}