package me.fliqq.echocrates.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import me.fliqq.echocrates.manager.CrateManager;

public class PreventRenameCrate implements Listener{
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Check if the clicked inventory is an anvil or any custom GUI where item renaming can occur
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            // Prevent renaming in the anvil
            if (event.getSlotType() == SlotType.RESULT) {
                ItemStack resultItem = event.getCurrentItem();
                if (resultItem != null && resultItem.getType() != Material.AIR) {
                    if (isItemUnrenamable(resultItem)) {
                        // Prevent renaming by cancelling the event
                        event.setCancelled(true);
                        player.sendMessage("Uwu.");
                    }
                }
            }
        }
    }

    private boolean isItemUnrenamable(ItemStack item) {
    	if(CrateManager.getInstance().isCrate(item))
    		return true;
    	return false;
    }
}

