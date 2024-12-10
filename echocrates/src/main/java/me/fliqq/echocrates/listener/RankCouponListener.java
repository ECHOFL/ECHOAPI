package me.fliqq.echocrates.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echocrates.models.RankCoupon;

public class RankCouponListener implements Listener{
	
	@EventHandler
	public void onCouponInteract(PlayerInteractEvent event) {
	    ItemStack item = event.getItem();
	    Player player = event.getPlayer();
    	System.out.println("TESTé");
	    if (item != null && RankCoupon.isRankCoupon(item)) {
	    	

	    	String rankString = item.getItemMeta().getPersistentDataContainer().get(RankCoupon.RANK_KEY, PersistentDataType.STRING);
	    	System.out.println("TEST1"+rankString);
	        RankCoupon rankCoupon = new RankCoupon(rankString);
	        rankCoupon.useCoupon(player);		
	        event.setCancelled(true); 

	    }
	}
}
