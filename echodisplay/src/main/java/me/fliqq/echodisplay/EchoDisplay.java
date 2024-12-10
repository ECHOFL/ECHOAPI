package me.fliqq.echodisplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.retrooper.packetevents.PacketEvents;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.fliqq.echodisplay.model.MainScoreboard;
import me.fliqq.echodisplay.task.TabHeadsAndFootersRunnable;

public class EchoDisplay extends JavaPlugin {

    private BukkitTask taskHeadsAndFooters;
    private BukkitTask task2;
	@Override
	public void onLoad() {

		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().getSettings()
			.reEncodeByDefault(false)
			.checkForUpdates(true)
			.bStats(false);
		PacketEvents.getAPI().load();
		
	}
    @Override
    public void onEnable() {
        // Check if ProtocolLib is enabled

        // Initialize configuration
        saveDefaultConfig();
        reloadConfig();

        // Start tasks
        taskHeadsAndFooters = getServer().getScheduler().runTaskTimer(this, TabHeadsAndFootersRunnable.getInstance(), 0, 100);
        task2 = getServer().getScheduler().runTaskTimer(this, MainScoreboard.getInstance(), 0, 60);
        // Register events
        
		//REGISTER PACKET LISTENER
		PacketEvents.getAPI().init();
		
        //PacketEvents.getAPI().getEventManager().registerListener(new TablistListener(), PacketListenerPriority.NORMAL);
        
        getLogger().info("EchoDisplay has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cancel tasks if they are running
        if (taskHeadsAndFooters != null && !taskHeadsAndFooters.isCancelled()) {
            taskHeadsAndFooters.cancel();
        }
        if (task2 != null && !task2.isCancelled()) {
            task2.cancel();
        }
        getLogger().info("EchoDisplay has been disabled!");

    	PacketEvents.getAPI().terminate();

    }

    // Optional: Method to get instance of EchoDisplay (if needed externally)
    public static EchoDisplay getInstance() {
        return getPlugin(EchoDisplay.class);
    }

}
