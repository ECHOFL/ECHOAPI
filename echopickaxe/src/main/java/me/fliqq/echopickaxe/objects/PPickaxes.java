package me.fliqq.echopickaxe.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echosql.EchoSql;

public class PPickaxes {
	
	private UUID uuid;
	private HashMap<PlayerEnchant, Integer> playerEnchants;
	private int totalBlockMined;
	private int realBlockMined;
	private int pickXp;
	private int pickLevel;
	private int pickSlot;
	//private PickSkin skin;	

	public PPickaxes(UUID uuid, HashMap<PlayerEnchant, Integer> playerEnchants, int totalBlockMined, int realBlockMined,
			int pickXp, int pickLevel, int pickSlot) {

		this.uuid = uuid;
		this.playerEnchants = playerEnchants;
		this.totalBlockMined = totalBlockMined;
		this.realBlockMined = realBlockMined;
		this.pickXp = pickXp;
		this.pickLevel = pickLevel;
		this.pickSlot = pickSlot;
	}
	public int getPickSlot() {
		return this.pickSlot;
	}
	public void setPickSlot(int newSlot) {
		Inventory inventory = Bukkit.getPlayer(uuid).getInventory();
		ItemStack previousItem = inventory.getItem(newSlot);
    	inventory.setItem(getPickSlot(), previousItem);
    	this.pickSlot= newSlot;
    	replaceItemOrEff();
	}
	public String getPlayerName() {
		return Bukkit.getPlayer(uuid).getName();
	}
	
	public HashMap<PlayerEnchant, Integer> getPlayerEnchants() {
		return playerEnchants;
	}
	
	public ItemStack getPickItemStack() {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatM.formatMessage("&9&lPioche de &l" + getPlayerName()));
		
		PersistentDataContainer data = meta.getPersistentDataContainer();
		data.set(PPickaxeManager.PPICK_KEY, PersistentDataType.STRING, uuid.toString());
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
          
		List<String> newLore = new ArrayList<>();
		
		for(PlayerEnchant enchant : PlayerEnchant.getEnchants()) {
			if(hasEnchant(enchant)) {
				if(getEnchantLevel(enchant) != 0) {
					newLore.add(ChatM.formatMessage(enchant.getColor()+enchant.getName()+" "+enchant.getColor()+"&l"+ChatM.formatInt(getEnchantLevel(enchant))));
				}
			}
		}
		
        newLore.add(ChatM.formatMessage("&4lié au compte"));
        newLore.add(" ");
        newLore.add(ChatM.formatMessage("&9Informations"));
        newLore.add(ChatM.formatMessage("&b&l◦ &fPropriétaire &b" + getPlayerName()));
        newLore.add(ChatM.formatMessage("&b&l◦ &fBlocs minés &b" + getRealBlockMined()));
        newLore.add(ChatM.formatMessage("&b&l◦ &fBlocs détruits &b" + ChatM.formatInt(getTotalBlockMined())));
        newLore.add(ChatM.formatMessage("&b&l◦ &fNiveaux de pioche &b" + getPickLevel()));
        newLore.add(ChatM.formatMessage("&2incassable"));
        
        meta.setLore(newLore);
        item.setItemMeta(meta);
        
        PlayerEnchant eff = PlayerEnchant.getEnchant("Efficiency");
        if(this.hasEnchant(eff)) {
        	item.addUnsafeEnchantment(Enchantment.DIG_SPEED, this.getEnchantLevel(eff));
        }
 
        return item;
	}
	
	
	public void updateItemMeta() {
		Bukkit.getPlayer(uuid).getInventory().getItem(pickSlot).setItemMeta(getPickItemMeta());
	}
	
	public void replaceItemOrEff() {
		Bukkit.getPlayer(uuid).getInventory().setItem(pickSlot, getPickItemStack());
	}
	
	public ItemMeta getPickItemMeta() {
		return getPickItemStack().getItemMeta();
	}
	
	public void setPlayerEnchants(HashMap<PlayerEnchant, Integer> playerEnchants) {
		this.playerEnchants = playerEnchants;
	}
	public int getTotalBlockMined() {
		return totalBlockMined;
	}
	public void setTotalBlockMined(int totalBlockMined) {
		this.totalBlockMined = totalBlockMined;
	}
	public int getRealBlockMined() {
		return realBlockMined;
	}
	public void setRealBlockMined(int realBlockMined) {
		this.realBlockMined = realBlockMined;
	}
	public int getPickXp() {
		return pickXp;
	}
	public void setPickXp(int pickXp) {
		this.pickXp = pickXp;
	}
	public int getPickLevel() {
		return pickLevel;
	}
	public void setPickLevel(int pickLevel) {
		this.pickLevel = pickLevel;
	}
	public UUID getUuid() {
		return uuid;
	}
	
	public boolean hasEnchant(PlayerEnchant enchant) {
		if(playerEnchants.containsKey(enchant) && playerEnchants.get(enchant) != 0) {
			return true;
		}
		return false;
	}
	
	public int getEnchantLevel(PlayerEnchant enchant) {
		if(hasEnchant(enchant))
			return this.playerEnchants.get(enchant);
		else
			return 0;
	}
	
	public void addEnchantLevel(PlayerEnchant enchant, int lvlToAdd) {
		this.playerEnchants.merge(enchant, lvlToAdd, Integer::sum);
		if(enchant.getName().equalsIgnoreCase("Efficiency"))
			replaceItemOrEff();
		else
			updateItemMeta();
		
	}
	public void setEnchantLevel(PlayerEnchant enchant, int newLvl) {
		if(hasEnchant(enchant))
			playerEnchants.replace(enchant, newLvl);
		else
			playerEnchants.put(enchant, newLvl);
		updateItemMeta();
	}
	
	public void payEnchIfEnoughToken(PlayerEnchant enchant, int lvlToAdd) {
		int actualLvl = getEnchantLevel(enchant);
		Double bank = EchoSql.getInstance().getPlayerBalance().get(uuid)[1];
		if(getEnchantLevel(enchant)+lvlToAdd>enchant.getMaxLvl()) {
			Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Impossible ! Le niveau maximal de &a"+enchant.getName()+" &7est &a"+enchant.getMaxLvl()));
			return;
		}
			
		Double multiplicator = actualLvl*1.1;
		int enchPrice = enchant.getPrice();
		int totalPrice = (int)((enchPrice*multiplicator*lvlToAdd)+enchant.getPrice());
		if(bank>totalPrice) {
			payForEnch(enchant, lvlToAdd, totalPrice);
		}else {
			Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEco()+"Tu n'as pas assez de tokens &8("+totalPrice+")"));
		}
	}
	
	public void payForEnch(PlayerEnchant enchant, int lvlToAdd,int totalPrice) {
		addEnchantLevel(enchant, lvlToAdd);

		Double[] banks = EchoSql.getInstance().getPlayerBalance().get(uuid);
		banks[1] -= totalPrice;
		Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEco()+"&c&l(- "+ChatM.formatInt(totalPrice)+ ") &7Tokens, &a"+ ChatM.formatDouble(banks[1])+" &7restants."));
		Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"&a&l(+ "+lvlToAdd+ ") "+enchant.getName()+" &7("+getEnchantLevel(enchant)+")"));
		
		EchoSql.getInstance().getPlayerBalance().replace(uuid, banks);
	}
	public void addRealBlockBreaked() {
		this.realBlockMined ++;	
		this.updateItemMeta();
	}
	public void addTotalBlockBreaked(int x) {
		this.totalBlockMined += x;	
	}
}	
