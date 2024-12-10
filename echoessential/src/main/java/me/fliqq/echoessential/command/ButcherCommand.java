package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;


public class ButcherCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (player.hasPermission("echo.essential.butcher")) {
            int radius = 2000; // Default radius
            EntityType type = null;
            int removedEntitiesCount = 0;

            // Parse arguments
            if(args.length == 0) {
                for (Entity entity : player.getNearbyEntities(2000, 2000, 2000)) {
                    if (entity instanceof Monster) { // Remove only aggressive mobs if no type is specified
                        entity.remove();
                        removedEntitiesCount++;
                    }	
                }
            }
            if (args.length > 0) {
                try {
                    radius = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Rayon invalide."));
                    return true;
                }
            }

            if (args.length > 1) {
                try {
                    type = EntityType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Type invalide"));
                    return true;
                }
            }

            for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                if (type == null) {
                    if (entity instanceof Monster) { // Remove only aggressive mobs if no type is specified
                        entity.remove();
                        removedEntitiesCount++;
                    }
                } else if (entity.getType() == type) {
                    if (!(entity instanceof Player)) { // Prevent removing players
                        entity.remove();
                        removedEntitiesCount++;
                    }
                }
            }
            player.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "&7Entitées supprimées: &e"+removedEntitiesCount));
            return true;
        } else {
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 2) {
            for (EntityType type : EntityType.values()) {
                if (type.isAlive() && !type.toString().startsWith("PLAYER")) {
                    suggestions.add(type.toString().toLowerCase());
                }
            }
        }

        return suggestions;
    }

}