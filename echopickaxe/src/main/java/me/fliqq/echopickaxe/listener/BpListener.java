package me.fliqq.echopickaxe.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import me.fliqq.echopickaxe.manager.BackPacksManager;

public class BpListener implements Listener{
	 
	//CANCEL PLACE BP
    @EventHandler
    public void onBpPlace(BlockPlaceEvent event) {
    	ItemStack item = event.getItemInHand();
    	if(BackPacksManager.getInstance().isBackPack(item)) {
    		event.setCancelled(true);
    	}
    }
    //CANCEL DROP
    @EventHandler
    public void onBpDrop(PlayerDropItemEvent event) {
    	ItemStack item = event.getItemDrop().getItemStack();
    	if(BackPacksManager.getInstance().isBackPack(item)) {
    		event.setCancelled(true);
    	}
    }
    //CANCEL SWAP
    @EventHandler
    public void onSecondHandEvent(PlayerSwapHandItemsEvent event){
        ItemStack offHandItem = event.getOffHandItem();
        if(BackPacksManager.getInstance().isBackPack(offHandItem))
            event.setCancelled(true);
    }
    
    //CANCEL LE RESTe
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	ItemStack currentItem = event.getCurrentItem();
    	ItemStack cursorItem = event.getCurrentItem();
    	ItemStack hotbarItem = null;
    	
    	//DEFINI HOTBARITEM SI CEST LE CAS
    	if(event.getClick() == ClickType.NUMBER_KEY) {
    		int hotbarButton = event.getHotbarButton();
    		hotbarItem = event.getWhoClicked().getInventory().getItem(hotbarButton);
    	}
    	//ELEMENT ACTUEL
    	if(currentItem != null && BackPacksManager.getInstance().isBackPack(currentItem)) {
    		event.setCancelled(true);
            return;
    	}
    	//ELEMENT CURSEUR
    	if(cursorItem != null && BackPacksManager.getInstance().isBackPack(cursorItem)) {
    		event.setCancelled(true);
            return;
    	}
    	//BAR ACCES RAPIDE
    	if(hotbarItem != null && BackPacksManager.getInstance().isBackPack(hotbarItem)) {
    		event.setCancelled(true);
            return;
    	}
    	
    }
}
