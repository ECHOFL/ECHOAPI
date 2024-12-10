package me.fliqq.echodisplay.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fliqq.echochat.manager.ChatManager;
import me.fliqq.echodisplay.EchoDisplay;
import me.fliqq.echodisplay.model.MainScoreboard;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TabHeadsAndFootersRunnable implements Runnable{

	private static final TabHeadsAndFootersRunnable instance = new TabHeadsAndFootersRunnable(); //SINGLETON
	private final Map<UUID, Integer> headerPositions = new HashMap<>();
	private final Map<UUID, Integer> footerPositions = new HashMap<>();
	List<String> headerLines = EchoDisplay.getInstance().getConfig().getStringList("Tablist.Header");
	List<String> footerLines = EchoDisplay.getInstance().getConfig().getStringList("Tablist.Footer");
	
	private TabHeadsAndFootersRunnable() {
	}
	
	@Override
	public void run() {
		MiniMessage miniMessage = MiniMessage.miniMessage();
		for(Player player : Bukkit.getOnlinePlayers()) {
			int headerPosition = headerPositions.getOrDefault(player.getUniqueId(), 0);
			int footerPosition = footerPositions.getOrDefault(player.getUniqueId(), 0);
			
			if(headerPosition >= headerLines.size())
				headerPosition = 0;
			if(footerPosition >= footerLines.size())
				footerPosition = 0;
			player.sendPlayerListHeaderAndFooter(
					miniMessage.deserialize(headerLines.get(headerPosition).replace("%player_online%", PlaceholderAPI.setPlaceholders(player, "%server_online%"))), miniMessage.deserialize(footerLines.get(footerPosition).replace("%player%", player.getName())) );
			
			headerPositions.put(player.getUniqueId(), headerPosition+1);
			footerPositions.put(player.getUniqueId(), footerPosition+1);

		}

	}

	public static TabHeadsAndFootersRunnable getInstance() {
		return instance;
	}

}
