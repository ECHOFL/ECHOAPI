package me.fliqq.echochat.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.fliqq.echochat.command.NickNameCommand;
import me.fliqq.echochat.manager.ChatManager;
import me.fliqq.echochat.model.PlayerChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class NickNameChatListener implements Listener {

    private final NickNameCommand nickNameCommand;

    public NickNameChatListener(NickNameCommand nickNameCommand) {
        this.nickNameCommand = nickNameCommand;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (nickNameCommand.getNicknameSetters().contains(playerUUID)) {
            event.setCancelled(true); // Cancel the chat message

            String input = event.getMessage();
            Component nickComponent = MiniMessage.miniMessage().deserialize(input);

            PlayerChat playerChat = ChatManager.getInstance().getPlayerChat(playerUUID);
            playerChat.setNickName(nickComponent);

            player.sendMessage("Your nickname has been updated successfully.");

            // Remove player from the nickname setters list
            nickNameCommand.getNicknameSetters().remove(playerUUID);
        }
    }
}