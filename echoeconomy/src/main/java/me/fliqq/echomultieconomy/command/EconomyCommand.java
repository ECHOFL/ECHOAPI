package me.fliqq.echomultieconomy.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.EchoMultiEconomy;
import me.fliqq.echomultieconomy.hook.VaultHook;
import me.fliqq.echosql.EchoSql;

public final class EconomyCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    
        if (args.length < 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                args = new String[]{"view", player.getName()};
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /eco <view/take/give> <player> <devise> <montant>");
                return true;
            }
        }

        String param = args[0];
        String playerName = args.length >= 2 ? args[1] : sender.getName();
        String currency = args.length >= 3 ? args[2] : "";
        String amountRaw = args.length == 4 ? args[3] : "";

        new BukkitRunnable() {

            @Override
            public void run() {
                OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatM.formatMessage(ChatM.logoEcho())+  ChatColor.RED + "Le joueur '" + playerName + "' n'a jamais joué sur le serveur.");
                    return;
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!VaultHook.hasEconomy()) {
                            sender.sendMessage(ChatColor.RED + "Erreur de chargement de l'économie.");
                            return;
                        }

                        if ("view".equals(param)) {
                            viewBalance(sender, playerName);
                        } else if ("take".equals(param) || "give".equals(param)) {
                            if (!sender.hasPermission("echo.economy.op")) {
                                sender.sendMessage(ChatM.formatMessage(ChatM.noPermissionMessage()));
                                return;
                            }

                            double amount;
                            try {
                                amount = Double.parseDouble(amountRaw);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "Le montant doit être un nombre décimal valide. Reçu : '" + amountRaw + "'.");
                                return;
                            }

                            if ("echo".equalsIgnoreCase(currency)) {
                                handleEchoTransaction(sender, target, param, amount);
                            } else if ("token".equalsIgnoreCase(currency) || "tokens".equalsIgnoreCase(currency)) {
                                handleOtherCurrencyTransaction(sender, target, param, amount, 1, "Tokens");
                            } else if ("gemme".equalsIgnoreCase(currency) || "gemmes".equalsIgnoreCase(currency)) {
                                handleOtherCurrencyTransaction(sender, target, param, amount, 2, "Gemmes");
                            } else if ("xp".equalsIgnoreCase(currency)) {
                                handleOtherCurrencyTransaction(sender, target, param, amount, 3, "XP");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Devise inconnue '" + currency + "'. Utilisez: echo, token, gemme, xp.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Paramètre inconnu '" + param + "'. Usage: " + command.getUsage());
                        }
                    }
                }.runTask(EchoMultiEconomy.getInstance());
            }
        }.runTaskAsynchronously(EchoMultiEconomy.getInstance());

        return true;
    }

    private void viewBalance(CommandSender sender, String playerName) {
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        Double[] balances = EchoSql.getInstance().getPlayerBalance().get(uuid);
        double balance = VaultHook.getBalance(Bukkit.getOfflinePlayer(playerName));

        sender.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "Banque de &f&l" + playerName));
        sender.sendMessage(ChatM.formatMessage("&7&l◦ &e&lEchos: &f" + VaultHook.formatCurrencySymbol(balance)));
        sender.sendMessage(ChatM.formatMessage("&7&l◦ &d&lTokens: &f" + ChatM.formatDouble(balances[1])));
        sender.sendMessage(ChatM.formatMessage("&7&l◦ &b&lGemmes: &f" + ChatM.formatDouble(balances[2])));
        sender.sendMessage(ChatM.formatMessage("&7&l◦ &a&lXP: &f" + ChatM.formatDouble(balances[3])));
    }

    private void handleEchoTransaction(CommandSender sender, OfflinePlayer target, String param, double amount) {
        String errorMessage = "take".equals(param) ? VaultHook.withdraw(target, amount) : VaultHook.deposit(target, amount);

        if (errorMessage != null && !errorMessage.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Erreur : " + errorMessage);
        } else {
            String action = "take".equals(param) ? "Retiré" : "Donné";
            sender.sendMessage(ChatColor.GREEN + action + " " + VaultHook.formatCurrencySymbol(amount) + " à " + target.getName() + ".");
        }
    }

    private void handleOtherCurrencyTransaction(CommandSender sender, OfflinePlayer target, String param, double amount, int balanceIndex, String currencyName) {
        UUID uuid = target.getUniqueId();
        Double[] balances = EchoSql.getInstance().getPlayerBalance().get(uuid);

        if ("take".equals(param)) {
            if (balances[balanceIndex] < amount) {
                sender.sendMessage(ChatColor.RED + "Montant insuffisant, le joueur n'a pas assez de " + currencyName + ".");
                return;
            }
            balances[balanceIndex] -= amount;
        } else {
            balances[balanceIndex] += amount;
        }

        EchoSql.getInstance().getPlayerBalance().put(uuid, balances);
        String action = "take".equals(param) ? "Retiré" : "Donné";
        sender.sendMessage(ChatColor.GREEN + action + " " + amount + " " + currencyName + " à " + target.getName() + ".");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("view", "take", "give");
        } else if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        } else if (args.length == 3) {
            return Arrays.asList("echo", "token", "gemme", "xp");
        }
        return Collections.emptyList();
    }
}