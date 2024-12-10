package me.fliqq.echoessential.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class FlySpeed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player p = (Player) sender;
        if(!p.hasPermission("echo.essential.speed")){
            p.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
        if (args.length < 1 || args.length > 2) {
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Usage incorrect: /speed <vitesse> (<player>)"));
            return true;
        }
        int speed;

        try {
            speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Usage incorrect: /speed <vitesse> (<player>)"));
            return true;
        }

        if (speed < -10 || speed > 10) {
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "La vitesse doit être comprise entre -10 et 10."));
            return true;
        }
        float flySpeed = speed / 10f;
        if (args.length == 1) {
            p.setFlySpeed(flySpeed);
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Votre vitesse de fly est fixée à &e" + speed));
            return true;
        }
        if(p.hasPermission("echo.essential.speedother")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                target.setFlySpeed(flySpeed);
                p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "La vitesse de fly de &a" + target.getName() + " &7est fixée à &e" + speed));
                target.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Votre vitesse de fly est fixée à &e" + speed));
            } else {
                p.sendMessage(ChatColor.RED + "Le joueur " + args[1] + " n'est pas en ligne.");
            }
            return  true;
        }
        return true;
    }
}
