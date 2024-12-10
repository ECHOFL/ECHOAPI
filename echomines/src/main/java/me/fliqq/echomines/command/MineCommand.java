package me.fliqq.echomines.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.fliqq.echomines.EchoMines;
import me.fliqq.echomines.manager.Mines;
import me.fliqq.echomines.manager.Regions;
import me.fliqq.echomines.scheduler.BlockDestroyTask;
import me.fliqq.echomines.scheduler.BlockResetTask;

public final class MineCommand implements CommandExecutor {

    private final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /mines <pos1|pos2|save <name>|paste <name>>");
            return true;
        }

        Regions regions = Regions.getInstance();

        Player player = (Player) sender;
        String param = args[0].toLowerCase();

        Tuple<Location, Location> selection = selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        switch (param) {
            case "pos1":
                selection.setFirst(player.getLocation());
                sender.sendMessage("§8[§a✔§8] §7First location set!");
                selections.put(player.getUniqueId(), selection);
                break;
            case "pos2":
                selection.setSecond(player.getLocation());
                sender.sendMessage("§8[§a✔§8] §7Second location set!");
                selections.put(player.getUniqueId(), selection);
                break;
            case "save":
                if (selection.getFirst() == null || selection.getSecond() == null) {
                    sender.sendMessage("§8[§c❌§8] §7Please select both positions first using /mine pos1 and /mine pos2");
                    return true;
                }
                if (!sender.hasPermission("echo.mines.create")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to create mines.");
                    return true;
                }
                if (args.length != 2) {
                    sender.sendMessage("§8[§6🛑§8] §7Usage: /mines save <name>");
                    return true;
                }

                String name = args[1];

                if (regions.findRegion(name) != null) {
                    sender.sendMessage(ChatColor.RED + "Region or mine by this name already exists.");
                    return true;
                }

                regions.saveMine(name, selection.getFirst(), selection.getSecond(), new HashMap<>(), player.getLocation(), 50, 6000);
                sender.sendMessage("§8[§a✔§8] §7Mine saved!");
                break;
            case "list":
                sender.sendMessage(ChatColor.GOLD + "Installed mines: " + String.join(", ", regions.getMinesNames()));
                break;
            case "current":
                Mines standingIn = regions.findMine(player.getLocation());
                sender.sendMessage(ChatColor.GOLD + "You are standing in mine: "
                        + (standingIn == null ? "none" : standingIn.getName()));
                break;
            case "reset":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mines reset <name>");
                    return true;
                }

                String mineName = args[1];
                Mines mine = regions.findMine(mineName);

                if (mine == null) {
                    sender.sendMessage(ChatColor.RED + "Mine not found!");
                    return true;
                }

                if (!sender.hasPermission("echo.mines.reset")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reset mines.");
                    return true;
                }
                new BlockResetTask(mine.getPrimaryLocation().getWorld(), mine.getLowX(), mine.getLowY(), mine.getLowZ(), 
                		mine.getHighX(), mine.getHighY(), mine.getHighZ()).runTaskTimer(EchoMines.getInstance(), 0L, 1L);
                
                 Set<Player> playersInMine = Regions.getInstance().getPlayersInMine(mine);
                 mine.teleportPlayerInMine(playersInMine);

                //mine.transformParallelepipedInLayersAsync();
                sender.sendMessage(ChatColor.GREEN + "Mine blocks reset successfully!");
                break;
            
            case "clear":
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mines clear <name>");
                    return true;
                }
                String mineName2 = args[1];
                Mines mine2 = regions.findMine(mineName2);

                if (mine2 == null) {
                    sender.sendMessage(ChatColor.RED + "Mine not found!");
                    return true;
                }

                if (!sender.hasPermission("echo.mines.clear")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to clear mines.");
                    return true;
                }
                new BlockDestroyTask(mine2.getPrimaryLocation().getWorld(), mine2.getLowX(), mine2.getLowY(), mine2.getLowZ(), 
                		mine2.getHighX(), mine2.getHighY(), mine2.getHighZ()).runTaskTimer(EchoMines.getInstance(), 0, 1);
                
                sender.sendMessage(ChatColor.GREEN + "Mine blocks cleared successfully!");
                break;
            
            case "setspawn":    
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mines setspawn <name>");
                    return true;
                }
                String mineName3 = args[1];
                Mines mine3 = regions.findMine(mineName3);

                if (mine3 == null) {
                    sender.sendMessage(ChatColor.RED + "Mine not found!");
                    return true;
                }

                if (!sender.hasPermission("echo.mines.setspawn")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to set spawn of mines.");
                    return true;
                }
                
                mine3.setSpawn(player.getLocation());
                sender.sendMessage(ChatColor.GREEN + "Spawn changé successfully!");
                break;

            default:
                sender.sendMessage("§8[§6🛑§8] §7Usage: /mines <pos1|pos2|save|reset> <name>|paste <name>>");
                break;
        }

        return true;
    }

    private static class Tuple<A, B> {
        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }
}