package me.fliqq.echoprisonrank.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fliqq.echoprisonrank.EchoPrisonRank;


public class ConnectionListener implements Listener {
	
	// IF RANK/MINE/ETC HAS CHANGED TO DB
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		EchoPrisonRank.getInstance().saveRankDb(event.getPlayer());
	}
}
