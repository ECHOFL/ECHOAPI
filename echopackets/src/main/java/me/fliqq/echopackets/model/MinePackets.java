package me.fliqq.echopackets.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion.BlockInteraction;

public class MinePackets implements Listener {

	public static void explosionPacket(Player player, Location blockLocation) {
		Vector3d vector3d = new Vector3d(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
		Vector3f vector3f = new Vector3f((float)blockLocation.getX(), (float)blockLocation.getY(), (float)blockLocation.getZ());
		List<Vector3i> listVector3is = new ArrayList<>();
		//listVector3is.add(new Vector3i(1));
		//listVector3is.add(new Vector3i(2));
		ResourceLocation resourceLocation = new ResourceLocation("minecraft:block.grass.break");
		Particle<ParticleData> particle = new Particle<ParticleData>(ParticleTypes.EXPLOSION);
		WrapperPlayServerExplosion packetExplosion = new WrapperPlayServerExplosion(vector3d, 2, listVector3is ,new Vector3f(),particle, particle, BlockInteraction.KEEP_BLOCKS,resourceLocation, null);
		
		
		PacketEvents.getAPI().getPlayerManager().sendPacket(player, packetExplosion);
	
	}
}
