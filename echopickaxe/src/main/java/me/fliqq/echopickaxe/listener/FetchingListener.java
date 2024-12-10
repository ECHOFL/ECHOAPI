package me.fliqq.echopickaxe.listener;


import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fliqq.echopickaxe.EchoPickaxe;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.manager.PPickaxeManager;

public class FetchingListener implements Listener{
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        if (!BackPacksManager.getInstance().hasBackPackInMap(uuid) || !PPickaxeManager.getInstance().hasPickInMap(uuid)) {
            player.sendMessage("No Pick data in map. Fetching from database...");
            EchoPickaxe.getInstance().getPickInfoFromDb(player);
        } else {
            player.sendMessage("Pick data already in map.");
            if (!BackPacksManager.getInstance().hasBackPackInInv(player))
            	BackPacksManager.getInstance().getBackPack(uuid).replaceBpInInv();   
            if(!PPickaxeManager.getInstance().hasPPickInInv(player))
            	PPickaxeManager.getInstance().getPick(uuid).replaceItemOrEff();;
        }
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
    	EchoPickaxe.getInstance().updatePickInfoInDb(event.getPlayer());;
    }
   
}


