package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("echo.essential.time")) {
                player.sendMessage(ChatM.noPermissionMessage());
                return true;
            }
            if(args.length==1){
                if (args[0].equalsIgnoreCase("day"))
                    player.getWorld().setTime(1000);
                else if (args[0].equalsIgnoreCase("night"))
                    player.getWorld().setTime(13000);
                else if (args[0].equalsIgnoreCase("mid"))
                    player.getWorld().setTime(6000);
                else if (args[0].equalsIgnoreCase("clear"))
                    player.getWorld().setClearWeatherDuration(72000);
                else{
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Usage incorrect : /time <day/mid/night/clear>"));
                    return true;
                }
                player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"L'heure/météo a bien été changée pour : &a" + args[0]));
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            List<String> times = new ArrayList<>();
            times.add("day");
            times.add("mid");
            times.add("night");
            times.add("clear");
            return times;
        }

        return Collections.emptyList();
    }
}
