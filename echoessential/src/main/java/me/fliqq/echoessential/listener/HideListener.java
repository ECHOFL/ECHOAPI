package me.fliqq.echoessential.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import me.fliqq.echoessential.manager.ChatM;

import java.util.Arrays;
import java.util.List;

public class HideListener implements Listener {
    private final List<String> allowedCommands = Arrays.asList("sell", "ench","butch","feed","rank","enchants","pl","economy","tphere","tp","time","spawn","setspawn",
    		"repair","invsee","god","gm","fly","speed","plugin","gamemode","bp", "backpack", "mines", "rgreload", "eco", "money","bal", "balance", "sellall", "nick", 
    		"nickname", "chatcolor","cc", "bp","check","withdraw","menu");


    @EventHandler
    public void onCommand(PlayerCommandSendEvent event) {
        if (!event.getPlayer().hasPermission("echo.essential.seecommands")) {
            event.getCommands().clear();

            event.getCommands().addAll(allowedCommands);
        }
    }

    @EventHandler
    public void onCommandReprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1).toLowerCase();
        Player p = event.getPlayer();
        if (!allowedCommands.contains(command) && !p.hasPermission("echo.essential.seecommands")) {
            event.setCancelled(true);
            p.sendMessage(ChatM.formatMessage(ChatM.logoEcho() + "Cette commande n'existe pas. /help"));
        }
    }
}
