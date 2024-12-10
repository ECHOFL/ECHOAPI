package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("echo.essential.fly")){
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }

        if(args.length==0) {
            toggleFly(player);
            return true;
        }
        if(args.length==1){
            if(player.hasPermission("echo.essential.flyother")){
                Player target = player.getServer().getPlayer(args[0]);
                if(target != null){
                    toggleFly(target);
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Mode de fly de &e"+target.getName()+" &7modifié pour &e"+target.getAllowFlight()+"&7."));
                }else
                    sender.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "ce joueur n'existe pas."));
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        return false;
    }

    public void toggleFly(Player player){
        if(player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "fly &cdésactivé&7."));
            return;
        }
        player.setAllowFlight(true);
        player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "fly &aactivé&7."));
    }
}
