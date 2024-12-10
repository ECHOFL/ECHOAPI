package me.fliqq.echochat.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fliqq.echochat.EchoChat;
import me.fliqq.echochat.manager.ChatManager;


public class ConnectionListener implements Listener {

	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        //player.sendMessage("Player joined: " + uuid.toString());
        if (!ChatManager.getInstance().hasPlayerChatInMap(uuid)) {
           // player.sendMessage("No chat data in map. Fetching from database...");
            EchoChat.getInstance().getChatFromDb(player);
        } else {
           // player.sendMessage("Chat data already in map.");
        }
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
    	EchoChat.getInstance().updateChatInDb(event.getPlayer());
    }
    
    
}
