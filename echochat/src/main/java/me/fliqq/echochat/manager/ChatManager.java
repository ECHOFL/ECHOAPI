package me.fliqq.echochat.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.fliqq.echochat.model.PlayerChat;


public class ChatManager {              
	
    private static ChatManager instance;  		    //SINGLETON, GERE LES INSTANCE DE playerChat
    private final Map<UUID, PlayerChat> playerChats;

    private ChatManager() {
    	playerChats = new HashMap<>();
    }


    public PlayerChat getPlayerChat(UUID uuid) {
        return playerChats.get(uuid);
    }

    public void addPlayerChat(UUID uuid, PlayerChat playerChat) {
    	playerChats.put(uuid, playerChat);
    }

    public boolean hasPlayerChatInMap(UUID uuid) {
        return playerChats.containsKey(uuid);
    }
    
    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

}
