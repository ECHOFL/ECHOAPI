package me.fliqq.echochat.command;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class NickNameCommand implements CommandExecutor, Listener {

    private final Set<UUID> nicknameSetters = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("echo.chat.nickname")) {
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }

        
        if (args.length != 1) {
            player.sendMessage("Usage: /nick <minimessage>");
            return true;
        } else {
            player.sendMessage("Create your MiniMessage nickname at https://webui.advntr.dev/ and then type it in the chat.");
            
            // Add player to the nickname setters list
            nicknameSetters.add(player.getUniqueId());
        }

        return true;
    }

    public Set<UUID> getNicknameSetters() {
        return nicknameSetters;
    }
}
