package me.fliqq.echoessential.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.config.EssentialSetting;
import me.fliqq.echoessential.manager.ChatM;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player p = (Player) sender;
        Location spawn = EssentialSetting.getInstance().getSpawnLocation();
        if(spawn != null){
            p.teleport(spawn);
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Téléportation au spawn."));
        }else{
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() +"Spawn non défini."));
        }
        return true;
    }

}
