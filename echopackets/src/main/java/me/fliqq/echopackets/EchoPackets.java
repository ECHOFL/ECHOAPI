package me.fliqq.echopackets;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.retrooper.packetevents.PacketEvents;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class EchoPackets extends JavaPlugin{

	
	@Override
	public void onLoad() {

		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().getSettings()
			.reEncodeByDefault(false)
			.checkForUpdates(true)
			.bStats(false);
		PacketEvents.getAPI().load();
		
	}

	@Override
	public void onEnable() {
		
		//REGISTER PACKET LISTENER
		PacketEvents.getAPI().init();

	}
	@Override
	public void onDisable() {
		PacketEvents.getAPI().terminate();
	}
}
