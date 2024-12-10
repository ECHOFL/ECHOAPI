package me.fliqq.echoessential.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.config.EssentialSetting;
import me.fliqq.echoessential.manager.ChatM;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(player.hasPermission("echo.essential.setspawn")){
            Location loc = player.getLocation();
            EssentialSetting.getInstance().setSpawnLocation(loc);
            player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"spawn défini avec succès."));
        }else{
            player.sendMessage(ChatM.noPermissionMessage());
        }
        return true;
    }
}
