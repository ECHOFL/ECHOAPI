package me.fliqq.echodungeon.command;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import me.fliqq.echodungeon.manager.WorldManager;

public class MineWorldCommand extends SimpleCommand {

	private WorldManager worldManager;
	
	public MineWorldCommand(String label, WorldManager worldManager) {
		super("echoworld");
		this.worldManager=worldManager;
	}

    @Override
    protected void onCommand() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            createMinesWorld(player, args[0]);
        } else if (args.length == 1) {
            teleportToWorld(player, args[0]);
        } else {
            sender.sendMessage("Usage: /mineworldcreate [<world_name>]");
        }
    }

    private void createMinesWorld(Player player, String name) {
        World minesWorld = worldManager.createWorld(name);

        if (minesWorld == null) {
            sender.sendMessage("Failed to create Mines world. Please check server logs.");
        } else {
            player.teleport(minesWorld.getSpawnLocation());
            sender.sendMessage("Teleported to the spawn of Mines world.");
        }
    }

    private void teleportToWorld(Player player, String worldName) {
        World world = worldManager.getWorld(worldName);

        if (world == null) {
            sender.sendMessage("World not found: " + worldName);
        } else {
            player.teleport(world.getSpawnLocation());
            sender.sendMessage("Teleported to the spawn of world: " + worldName);
        }
    }
}
