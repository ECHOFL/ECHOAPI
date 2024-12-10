package me.fliqq.echocrates.listener;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import fr.xephi.authme.libs.org.postgresql.gss.MakeGSS;
import me.fliqq.echocrates.manager.CrateManager;
import me.fliqq.echocrates.models.CrateLoots;
import me.fliqq.echocrates.models.Crates;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CrateInteraction implements Listener {
	
	@EventHandler
	public void onCrateInteract(PlayerInteractEvent event) {
	    ItemStack item = event.getItem();
	    Player player = event.getPlayer();

	    if (item != null && CrateManager.getInstance().isCrate(item)) {
	        event.setCancelled(true); // Annuler l'interaction normale avec l'ItemStack

	        Crates crate = CrateManager.getInstance().getCrate(item);
	        if (crate != null) {
	            // Obtenir la LinkedHashMap triée des loots
	            LinkedHashMap<CrateLoots, Integer> sortedLoots = crate.sortLootsByValue();

	            // Calculer le total de tous les poids pour déterminer la probabilité
	            int totalWeight = sortedLoots.values().stream().mapToInt(Integer::intValue).sum();

	            // Générer un nombre aléatoire pour déterminer le loot
	            int randomNumber = (int) (Math.random() * totalWeight) + 1;

	            // Parcourir les loots triés pour trouver celui correspondant au nombre aléatoire
	            int cumulativeWeight = 0;
	            for (Map.Entry<CrateLoots, Integer> entry : sortedLoots.entrySet()) {
	                cumulativeWeight += entry.getValue();
	                if (randomNumber <= cumulativeWeight) {
	                    CrateLoots loot = entry.getKey();

	                    // Exécuter l'action associée au loot (par exemple, donner un objet, exécuter une commande, etc.)
	                    loot.executeLootAction(player);

	                    item.setAmount(item.getAmount()-1);
	                    player.getInventory().setItemInMainHand(item);
	                    player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);

	                    //ItemStack lootItemStack = ItemCreator.of(loot.getCompMaterial(), LegacyComponentSerializer.legacyAmpersand().serialize(loot.getNameComponent()), " ", LegacyComponentSerializer.legacyAmpersand().serialize(loot.getRarity())).make();
	                    //player.getInventory().addItem(lootItemStack);
	                    break;
	                }
	            }
	        }
	    }
	}
}
