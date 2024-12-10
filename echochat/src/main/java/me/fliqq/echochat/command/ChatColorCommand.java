package me.fliqq.echochat.command;

import java.awt.Color;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echochat.manager.ChatManager;
import me.fliqq.echochat.model.PlayerChat;
import me.fliqq.echoessential.manager.ChatM;

public class ChatColorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("echo.chat.chatcolor")) {
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /chatcolor <color>");
            return true;
        }

        try {
            int color = Integer.parseInt(args[0]);

            PlayerChat playerChat = ChatManager.getInstance().getPlayerChat(player.getUniqueId());
            playerChat.setChatColor(color); // Assuming setChatColor is the correct method to set the chat color

            player.sendMessage("Chat color updated successfully.");
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid color value. Please enter a valid integer.");
            return true;
        }

        return true;
    }
}