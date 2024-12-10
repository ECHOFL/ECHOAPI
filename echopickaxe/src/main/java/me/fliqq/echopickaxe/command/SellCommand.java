package me.fliqq.echopickaxe.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.hook.VaultHook;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.objects.BackPacks;

public class SellCommand implements CommandExecutor {
	
    private final BackPacksManager bM = BackPacksManager.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		if(!player.hasPermission("echo.prison.sell")) {
			player.sendMessage(ChatM.noPermissionMessage());
			return true;
		}
		
		int price = 1;
		BackPacks backpack=bM.getBackPack(player.getUniqueId());
		int earned = backpack.getContenance()*price;
	
		VaultHook.deposit(player, earned);
		player.sendMessage(ChatM.formatMessage(ChatM.logoEco()+"&a&l(+ "+ChatM.formatInt(earned)+ ") Echos."));
		backpack.clearContenanceItem();
		backpack.setContenance(0);
		backpack.clearContenanceItem();
		
		return true;
	}

}
