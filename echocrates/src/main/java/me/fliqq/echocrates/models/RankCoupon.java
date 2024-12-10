package me.fliqq.echocrates.models;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import me.fliqq.echocrates.EchoCrates;



public class RankCoupon {

	private String rank;
	private CommandLootAction commandLootAction;
	private ItemStack item;
	public final static NamespacedKey RANK_KEY = new NamespacedKey(EchoCrates.getInstance(), "rank_key");

	public RankCoupon(String rank) {
		this.rank = rank.toLowerCase();
		this.commandLootAction=new CommandLootAction("lp user %player% parent set "+rank);
		initItemStack();
	}
	
	public static boolean isRankCoupon(ItemStack itemStack) {
		if(itemStack.getItemMeta().getPersistentDataContainer().has(RANK_KEY)) {
			System.out.println("TEST3");
			return true;
		}
		return false;
	}
	
	public void useCoupon(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		item.setAmount(item.getAmount()-1);
		commandLootAction.execute(player);
	}
	
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public ItemStack getItem() {
		return item;
	}

	public void initItemStack() {
		ItemStack coupon = ItemCreator.of(CompMaterial.PAPER, rank.toUpperCase(), "&7Click droit pour utilisé"). glow(true).make();
		ItemMeta meta = coupon.getItemMeta();
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		dataContainer.set(RANK_KEY, PersistentDataType.STRING, this.rank);
		coupon.setItemMeta(meta);
		this.item = coupon;
	}
	
}
