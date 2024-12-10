package me.fliqq.echoessential.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockPosCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Block targetBlock = player.getTargetBlockExact(100); // 100 is the maximum distance to scan for blocks

            if (targetBlock != null && targetBlock.getType() != Material.AIR) {
                Location blockLocation = targetBlock.getLocation();
                player.sendMessage("Block at X: " + blockLocation.getBlockX() +
                                   " Y: " + blockLocation.getBlockY() +
                                   " Z: " + blockLocation.getBlockZ());
            } else {
                player.sendMessage("No block in sight within range.");
            }
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }


}
