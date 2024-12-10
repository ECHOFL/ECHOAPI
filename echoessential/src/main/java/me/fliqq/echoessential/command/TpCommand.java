package me.fliqq.echoessential.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class TpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(args.length==1){
            if(player.hasPermission("echo.essential.tp")){
                Player target = player.getServer().getPlayer(args[0]);
                if(target!=null){
                    Location loc = target.getLocation();
                    player.teleport(loc);
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Téléportation au joueur &e"+target.getName()));

                }else
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Joueur introuvable."));
                return true;
            }
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Usage incorrect : /tp <player>"));
        return args.length == 0;
    }
}
