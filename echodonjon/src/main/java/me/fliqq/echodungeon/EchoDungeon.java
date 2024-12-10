package me.fliqq.echodungeon;

import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

import me.fliqq.echodungeon.command.DungeonCommand;
import me.fliqq.echodungeon.command.MineWorldCommand;
import me.fliqq.echodungeon.manager.DungeonManager;
import me.fliqq.echodungeon.manager.WorldManager;
import me.fliqq.echodungeon.runnable.DungeonCreationTask;

public class EchoDungeon extends SimplePlugin{

	private WorldManager worldManager;
	private DungeonManager dungeonManager;

	@Override
	protected void onPluginStart() {
        worldManager = new WorldManager(this);
        dungeonManager = new DungeonManager(this, worldManager);
        
		registerCommand(new DungeonCommand("dungeon", worldManager));
		registerCommand(new MineWorldCommand("echoworld", worldManager));

        Bukkit.getScheduler().runTaskTimer(this, new DungeonCreationTask(this, worldManager, dungeonManager), 100L, 30 * 60 * 20L);


	}
	
	
	
	@Override
	protected void onPluginStop() {
       worldManager.cleanupWorlds();
	}
	
	public static EchoDungeon getInstance() {
		return (EchoDungeon) SimplePlugin.getInstance();
	}

}
