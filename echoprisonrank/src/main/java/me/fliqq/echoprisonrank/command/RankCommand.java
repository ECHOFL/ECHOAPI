package me.fliqq.echoprisonrank.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echoprisonrank.manager.RankManager;
import me.fliqq.echosql.EchoSql;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player) sender;

        if(args.length !=0) {
        	if(!player.hasPermission("echo.prisonrank.setrank")) {
        		player.sendMessage(ChatM.noPermissionMessage());
        	}
        }
        if(args.length==0) {
            for (String mine : RankManager.getAllMines()) {
            	player.sendMessage(ChatColor.YELLOW+mine);
            	return true;
            }
        }
        
        if (args.length < 3) {
            player.sendMessage("Usage: /rank set <player> <rank>");
            return true;
        }

        if ("set".equalsIgnoreCase(args[0])) {
            Player target = Bukkit.getServer().getPlayer(args[1]);
            if (target != null) {
                for (String mine : RankManager.getAllMines()) {
                    if (mine.equalsIgnoreCase(args[2])) {
                        EchoSql.getInstance().getPlayerMine().put(target.getUniqueId(), mine);
                        player.sendMessage(ChatM.formatMessage(ChatM.logoPrison() + "Le rang du joueur &a" + target.getName() + " &7est maintenant &a" + mine));
                        target.sendMessage(ChatM.formatMessage(ChatM.logoPrison() + "&eFélicitations ! &a" + target.getName() + "&7, tu es passé au rang &a" + mine));
                        return true;
                    }
                }
                player.sendMessage(ChatM.formatMessage(ChatM.logoPrison() + "Cette mine/rank n'existe pas."));
                return true;
            }
            player.sendMessage(ChatM.formatMessage(ChatM.logoPrison() + "Ce joueur est introuvable."));
            return true;
        }
        return true;
    }
}
