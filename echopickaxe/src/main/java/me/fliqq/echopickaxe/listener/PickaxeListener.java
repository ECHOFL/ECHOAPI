package me.fliqq.echopickaxe.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echopickaxe.objects.PPickaxes;

public class PickaxeListener implements Listener {
	
	//EMPECHE DROP SI PAS PVP
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(player.getWorld().getName().equalsIgnoreCase("pvp"))
			return;

        event.setKeepInventory(true); 
        event.setKeepLevel(true);  
        event.getDrops().clear(); 
	}
	
	
	//AJOUTE HASTE // SPEED // VISION NOC
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PPickaxes pPickaxe = PPickaxeManager.getInstance().getPick(player.getUniqueId());
        
        if(player.getWorld().getName().equalsIgnoreCase("plot")) 
        	return;
        
        if (pPickaxe.getPickSlot() == event.getNewSlot()) {
        	if(pPickaxe.hasEnchant(PlayerEnchant.getEnchant("Haste"))) {
	            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE,pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Haste"))-1 , false, false));
	        } 
        	if(pPickaxe.hasEnchant(PlayerEnchant.getEnchant("Speed"))) {
	            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Speed"))-1 , false, false));
        	}
        	if(pPickaxe.hasEnchant(PlayerEnchant.getEnchant("VisionNocturne"))) {
	            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE,pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("VisionNocturne"))-1 , false, false));
	        } 
        }
        else {
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }
    
    // CANCEL DROP PICKAXE
    @EventHandler
    public void onPickaxeDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (PPickaxeManager.getInstance().isPPick(item)) {
            event.setCancelled(true);
        }
    }

    // CANCEL SWAP PICKAXE
    @EventHandler
    public void onPickaxeSwap(PlayerSwapHandItemsEvent event) {
        ItemStack offHandItem = event.getOffHandItem();
        if (PPickaxeManager.getInstance().isPPick(offHandItem)) {
            event.setCancelled(true);
        }
    }

    // CANCEL THE REST
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        ItemStack hotbarItem = null;

        // DEFINE HOTBARITEM IF NECESSARY
        if (event.getClick() == ClickType.NUMBER_KEY) {
            int hotbarButton = event.getHotbarButton();
            hotbarItem = event.getWhoClicked().getInventory().getItem(hotbarButton);
        }

        // CURRENT ITEM
        if (currentItem != null && PPickaxeManager.getInstance().isPPick(currentItem)) {
            event.setCancelled(true);
            return;
        }

        // CURSOR ITEM
        if (cursorItem != null && PPickaxeManager.getInstance().isPPick(cursorItem)) {
            event.setCancelled(true);
            return;
        }

        // HOTBAR ITEM
        if (hotbarItem != null && PPickaxeManager.getInstance().isPPick(hotbarItem)) {
            event.setCancelled(true);
            return;
        }
    }
}