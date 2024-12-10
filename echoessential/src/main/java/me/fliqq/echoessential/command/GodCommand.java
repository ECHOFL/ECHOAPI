package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class GodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(args.length==0){
            if(player.hasPermission("echo.essential.god")){
                changeGod(player);
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        if(args.length==1){
            if(player.hasPermission("echo.essential.godother")){
                Player target = player.getServer().getPlayer(args[0]);
                if(target!=null) {
                    changeGod(target);
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "God mode de &e"+target.getName()+" &7modifié pour &e"+target.isInvulnerable()+"&7."));
                }else
                    sender.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "ce joueur n'existe pas."));
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        return false;
    }

    private void changeGod(Player player) {
        if(player.isInvulnerable()){
            player.setInvulnerable(false);
            player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"God mode &cdésactivé."));
            return;
        }
        player.setInvulnerable(true);
        player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"God mode &aactivé."));
    }
}
