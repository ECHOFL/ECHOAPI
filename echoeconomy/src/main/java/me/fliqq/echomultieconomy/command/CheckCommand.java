package me.fliqq.echomultieconomy.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.hook.VaultHook;
import me.fliqq.echomultieconomy.model.Check;
import me.fliqq.echosql.DatabaseManager;
import me.fliqq.echosql.EchoSql;

public class CheckCommand implements CommandExecutor,TabCompleter {

    private HashMap<UUID, Double[]> balances = EchoSql.getInstance().getPlayerBalance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        
        if (args.length != 2) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Usage: &f/check <echo/token/gemme/xp> <montant>"));
            return true;
        }
        

        UUID uuid = player.getUniqueId();
        
        Double amount;
        
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Le montant doit être un entier positif."));
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Le montant doit être un entier valide."));
            return true;
        }

        Double[] playerBalances = balances.get(uuid);
        if (playerBalances == null) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Erreur: Solde du joueur introuvable."));
            return true;
        }

        String currency = args[0].toLowerCase();
        double balance = 0;
        int index = -1;
        
        switch (currency) {
            case "echo":
                balance = playerBalances[0];
                index = 0;
                break;
            case "token":
                balance = playerBalances[1];
                index = 1;
                break;
            case "gemme":
                balance = playerBalances[2];
                index = 2;
                break;
            case "xp":
                balance = playerBalances[3];
                index = 3;
                break;
            default:
                player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Devise inconnue. Utilisez: echo, token, gemme, xp."));
                return true;
        }

        if (balance < amount) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Montant indisponible, tu n'as pas assez de &e" + currency + "."));
            return true;
        }
        
        //nouveau check
        player.getInventory().addItem(new Check(player, currency, amount).getItemStack());
        
        // Déduire le montant du solde du joueur
        if(index==0) {
        	VaultHook.withdraw(player, amount);
        }else {
            playerBalances[index] -= amount;
        }
        
        balances.put(uuid, playerBalances);

        // Mise à jour de la base de données
        updateDatabase(uuid, index, playerBalances[index]);

        player.sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Tu as retiré &f" + ChatM.formatDouble(amount) + " " + currency + "&7. Nouveau solde: &f" +  ChatM.formatDouble(playerBalances[index])));

        return true;
    }

    private void updateDatabase(UUID uuid, int index, double newBalance) {
        String[] columns = {"echo", "token", "gemme", "xp"};
        String sql = "UPDATE player_economy SET " + columns[index] + " = ? WHERE uuid = ?";

        try (Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             preparedStatement.setDouble(1, newBalance);
             preparedStatement.setString(2, uuid.toString());
             preparedStatement.executeUpdate();
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Suggestion des devises disponibles
            completions.addAll(Arrays.asList("echo", "token", "gemme", "xp"));
        } else if (args.length == 2) {
        }

        return completions;
    }
}
