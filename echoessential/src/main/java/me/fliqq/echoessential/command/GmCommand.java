package me.fliqq.echoessential.command;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class GmCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(args.length==0){
            if(player.hasPermission("echo.essential.gm")){
                changeGm(player);
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        if(args.length==1){
            if(player.hasPermission("echo.essential.gmother")){
                Player target = player.getServer().getPlayer(args[0]);
                if(target!=null) {
                    changeGm(target);
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "mode de jeu de &e"+target.getName()+" &7modifié pour &e"+target.getGameMode()+"."));
                }else
                    sender.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "ce joueur n'existe pas."));
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        return false;
    }

    private void changeGm(Player player) {
        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"mode de jeu changé pour &esurvie."));
            return;
        }
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"mode de jeu changé pour &acréatif."));
    }
}
