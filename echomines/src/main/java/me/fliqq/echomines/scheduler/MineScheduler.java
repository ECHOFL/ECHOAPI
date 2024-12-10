package me.fliqq.echomines.scheduler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echomines.EchoMines;
import me.fliqq.echomines.manager.Mines;
import me.fliqq.echomines.manager.Regions;

import java.util.*;

public class MineScheduler extends BukkitRunnable {

    private static MineScheduler instance;
    private int currentIndex = 0;
    private final Set<Mines> mines;
   

    private MineScheduler() {
        this.mines = Regions.getInstance().getMines();
    }

    public static MineScheduler getInstance() {
        if (instance == null) {
            instance = new MineScheduler();
        }
        return instance;
    }

    @Override
    public void run() {
        if (mines.isEmpty()) {
            return;
        }

        Mines currentMine = (Mines) mines.toArray()[currentIndex];
        currentIndex = (currentIndex + 1) % mines.size();

        double airPercentage = currentMine.getAirPercentage();
        int resetPourcent = currentMine.getResetPourcent();

        EchoMines.logger.info("Checking mine: " + currentMine.getName() + " - Air percentage: " + airPercentage + "%");
    

        if (100 - airPercentage < resetPourcent) {
            Set<Player> playersInMine = Regions.getInstance().getPlayersInMine(currentMine);

            if (!playersInMine.isEmpty()) {
                new BlockResetTask(currentMine.getPrimaryLocation().getWorld(), currentMine.getLowX(), currentMine.getLowY(), currentMine.getLowZ(), 
                		currentMine.getHighX(), currentMine.getHighY(), currentMine.getHighZ()).runTaskTimer(EchoMines.getInstance(), 0L, 1L);
                
                currentMine.teleportPlayerInMine(playersInMine);
            }
        }
        else if(100 - airPercentage <20) {
        	new BlockResetTask(currentMine.getPrimaryLocation().getWorld(), currentMine.getLowX(), currentMine.getLowY(), currentMine.getLowZ(), 
            		currentMine.getHighX(), currentMine.getHighY(), currentMine.getHighZ()).runTaskTimer(EchoMines.getInstance(), 0L, 1L);
        }
        
    }

    public void startScheduler() {
        this.runTaskTimer(EchoMines.getInstance(), 0, 60); // Runs every 5 seconds (100 ticks = 5 seconds)
    }
}