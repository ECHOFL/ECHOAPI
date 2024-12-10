package me.fliqq.echomines.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomines.manager.Regions;

public class MineReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if(!(sender instanceof Player)) 
			return true;
		
		Player player =(Player) sender;
		if(!player.hasPermission("echo.mines.reload")) {
			player.sendMessage(ChatM.noPermissionMessage());
			return true;
		}
		
		Regions.getInstance().load();
		player.sendMessage(ChatM.formatMessage(ChatM.logoMines()+"&aReload des regions effectuée avec succès."));
		return true;
	}

}
