package me.fliqq.echomultieconomy.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.hook.VaultHook;
import me.fliqq.echomultieconomy.model.Check;
import me.fliqq.echosql.DatabaseManager;
import me.fliqq.echosql.EchoSql;

public class CheckListener implements Listener {
	 @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent event) {
	        if (event.getHand() != EquipmentSlot.HAND) return;

	        ItemStack item = event.getItem();
	        if (item != null && Check.isCheck(item)) {
	            Double valeur = Check.getCheckValue(item);
	            String devise = Check.getCheckCurrency(item);

	            // Débogage pour vérifier les valeurs récupérées
	            System.out.println("Chèque détecté avec valeur : " + ChatM.formatDouble(valeur) + ", devise : " + devise);

	            if (valeur > 0 && devise != null) {
	                // Ajouter la valeur à l'équilibre du joueur dans la devise appropriée
	                UUID uuid = event.getPlayer().getUniqueId();
	                Double[] playerBalances = EchoSql.getInstance().getPlayerBalance().get(uuid);
	                int index;

	                switch (devise.toLowerCase()) {
	                    case "token":
	                        index = 1;
	                        break;
	                    case "gemme":
	                        index = 2;
	                        break;
	                    case "xp":
	                        index = 3;
	                        break;
	                    case "echo":
	                        index = 0;
	                        break;
	                    default:
	                    	event.getPlayer().sendMessage(ChatM.formatMessage("Check corrupted, veuillez vous referez à une administrateur Erreur: &4" +devise));
	                        return;
	                }

	                if(index==0) {
	                	VaultHook.deposit(event.getPlayer(), valeur);
	                }else {
		                playerBalances[index] += valeur;
	                }
	                
	                EchoSql.getInstance().getPlayerBalance().put(uuid, playerBalances);

	                // Mise à jour de la base de données
	                updateDatabase(uuid, index, playerBalances[index]);

	                // Retirer l'item de l'inventaire du joueur
	                event.getPlayer().getInventory().remove(item);

	                // Message de confirmation pour le joueur
	                event.getPlayer().sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Vous avez encaissé un chèque de &f" + ChatM.formatDouble(valeur) + " " + devise + "&7. Nouveau solde: &f" + ChatM.formatDouble(playerBalances[index])));

	            } else {
	                // Message d'erreur si la valeur ou la devise du chèque est incorrecte
	                event.getPlayer().sendMessage(ChatM.formatMessage(ChatM.logoEco() + "&7Erreur lors de l'encaissement du chèque."));
	            }

	            event.setCancelled(true); // Annuler l'événement pour empêcher d'autres interactions avec l'item
	        }
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
}