package me.fliqq.echomultieconomy.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.fliqq.echomultieconomy.EchoMultiEconomy;
import me.fliqq.echosql.EchoSql;

public class ConnectionListener implements Listener {

	@EventHandler void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Double amount = EchoSql.getInstance().getPlayerBalance().get(uuid)[0];
		EchoMultiEconomy.getInstance().setEchoBalances(player, amount);
	}
		
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        EchoMultiEconomy.getInstance().updatePlayerEcoInDn(player);
    }
}