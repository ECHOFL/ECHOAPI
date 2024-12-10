package me.fliqq.echopickaxe.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.objects.BackPacks;

public class BpCommand implements CommandExecutor{
	
	private final BackPacksManager bM = BackPacksManager.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		if(!player.hasPermission("echo.pickaxe.bplvl")) {
			player.sendMessage(ChatM.noPermissionMessage());
			return true;
		}
		
		if(args.length == 2) {
	        String playerName = args[0];
	        Player targetPlayer = Bukkit.getPlayer(playerName);
	        if (targetPlayer == null || !targetPlayer.isOnline()) {
	        	 player.sendMessage(ChatM.formatMessage(ChatM.logoPrison()+"Le joueur n'est pas en ligne ou n'existe pas."));
	            return true;
	        }
	        int bpLvl;
	        try {
	        	bpLvl = Integer.parseInt(args[1]);
	        } catch (NumberFormatException e) {
	            player.sendMessage(ChatM.formatMessage(ChatM.logoPrison()+"Le niveau de sac à dos spécifié n'est pas valide"));
	            return true;
	        }

	        final UUID uuid = targetPlayer.getUniqueId();
	        BackPacks backpacks = bM.getBackPack(uuid);
	        targetPlayer.sendMessage(ChatM.formatMessage(ChatM.logoPrison()+"Votre niveau de &asac à dos&7 est maintenant de &a"+bpLvl));
	        player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Le niveau de &asac à dos &7pour le joueur &a" +targetPlayer.getName()+" &7est maintenant de &a"+bpLvl));
	        backpacks.setLvl(bpLvl);
		}
		return true;
	}

}
