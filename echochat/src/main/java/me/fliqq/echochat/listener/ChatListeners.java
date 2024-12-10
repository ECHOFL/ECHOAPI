package me.fliqq.echochat.listener;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.fliqq.echochat.manager.ChatManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;


public class ChatListeners implements Listener {
    
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        
        
        Component messageComponent = event.message();
        
        Component fullMessage = ChatManager.getInstance().getPlayerChat(player.getUniqueId()).createChatComponent(messageComponent);
        
        // Send the custom message to all audiences
        for (Audience audience : event.viewers()) {
            if (audience instanceof Player) {
                audience.sendMessage(fullMessage);
            }
        }
        event.setCancelled(true);
    }
}

//    NAME IN REACTION
/*        final String[] message = event.getMessage().split(" ");
final Set<Player> notify = new HashSet<>();

for (int i = 0; i < message.length; i++) {
    final String word = message[i];
    final Player mention;

    if (word.matches("^\\w{3,16}(\\p{Punct})*$")
            && (mention = Bukkit.getPlayerExact(word.replaceAll("\\p{Punct}", ""))) != null) {
        message[i] = "§9@" + mention.getName() + "§r" + word.substring(mention.getName().length());
        notify.add(mention);
    }
}

for (Player notified : notify)
    notified.playSound(notified.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
;
String newMessage = String.join(" ", message);*/