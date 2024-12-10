package me.fliqq.echoessential.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fliqq.echoessential.EchoEssential;
import me.fliqq.echoessential.config.EssentialSetting;
import me.fliqq.echoessential.manager.ChatM;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionL implements Listener {

    private final EchoEssential plugin = EchoEssential.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Location spawn = EssentialSetting.getInstance().getSpawnLocation();
        if(spawn!=null)
            p.teleport(spawn);
        if(p.hasPermission("group.mecene")){
            String welcom = plugin.getConfig().getString("messages.join");
            welcom = PlaceholderAPI.setPlaceholders(p, welcom);
            e.setJoinMessage(ChatM.formatMessage(welcom));
            return;
        }
        e.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("group.mecene")){
            String bye = plugin.getConfig().getString("messages.quit");
            bye = PlaceholderAPI.setPlaceholders(p, bye);
            e.setQuitMessage(ChatM.formatMessage(bye));
            return;
        }
        e.setQuitMessage("");
    }
}
