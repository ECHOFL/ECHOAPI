package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;

public class FeedCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if(player.hasPermission("echo.essential.feed")) {
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.setExhaustion(0);
			player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Rassasié !"));
			return true;
		}
		player.sendMessage(ChatM.noPermissionMessage());
		return true;

	}

}
