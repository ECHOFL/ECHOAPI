package me.fliqq.echodungeon.command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.command.SimpleCommand;

import me.fliqq.echodungeon.manager.WorldManager;

public class DungeonCommand extends SimpleCommand{

	private final WorldManager worldManager;

	
	public DungeonCommand(String label, WorldManager worldManager) {
		super("dungeon");
		this.worldManager = worldManager;
	}

	
	@Override
	protected void onCommand() {
        if (args.length == 0) {
    		worldManager.createWorld("dungeon_world_" + System.currentTimeMillis());
            return;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            List<World> worlds = worldManager.getActiveWorlds();
            sender.sendMessage("Active dungeon worlds:");
            for (World world : worlds) {
                sender.sendMessage("- " + world.getName());
            }
            return;
        }
               
		
		 if (args[0].equalsIgnoreCase("tp")) {
             if (!(sender instanceof Player)) {
                 sender.sendMessage("Only players can use this command.");
                 return;
             }

             if (args.length < 2) {
                 sender.sendMessage("Usage: /dungeon tp <worldName>");
                 return;
             }

             Player player = (Player) sender;
             World world = worldManager.getWorld(args[1]);

             if (world == null) {
                 sender.sendMessage("World not found: " + args[1]);
                 return;
             }

             player.teleport(world.getSpawnLocation());
             sender.sendMessage("Teleported to the spawn of world: " + args[1]);
             return;
         }
     
		
	}

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            return completeLastWord("tp");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
            return completeLastWordWorldNames();
        }

        return null;
    }

    @Override
    protected List<String> completeLastWordWorldNames() {
        return completeLastWord(worldManager.getDungeonWorldNames());
    }

}
