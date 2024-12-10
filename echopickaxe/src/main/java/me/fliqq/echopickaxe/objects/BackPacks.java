package me.fliqq.echopickaxe.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echosql.EchoSql;

public class BackPacks {

    private int contenance;
    private int bpSlot;
    private Map<ItemStack, Integer> contenanceItem;
    
    private int lvl;
    private UUID uuid;
    

    // Initialization
    public BackPacks(UUID uuid, int lvl, int slot) {
        this.lvl = lvl;
        this.uuid = uuid;
        this.bpSlot = slot; 
        this.contenance = 0; // initialisé à 0 à chaque création de l'objet.(donc au reset)    
        this.contenanceItem = new HashMap<>();
    }

    public String getPlayerName() {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? player.getName() : "Unknown";
    }
    
    public int getCapacity() {
    	return this.lvl*250;
    }
    
    public Map<ItemStack, Integer> getContenanceItem(){
    	return this.contenanceItem;
    }
    public void clearContenanceItem(){
    	this.contenanceItem.clear();    }
    
    public void addContenanceItem(ItemStack key,int value){
	    this.contenanceItem.merge(key, value, Integer::sum);
    }


    public int getLvl() {
        return this.lvl;
    }
   
    public void setLvl(int lvl) {
        this.lvl = lvl;
        updateBpInInv();
    }
    public void addLvl(int lvlToAdd) {
    	this.lvl += lvlToAdd;
    	updateBpInInv();
    }
    
    public void addLvlIfGems(int lvlToAdd){
		int actualLvl = getLvl();
		Double bank = EchoSql.getInstance().getPlayerBalance().get(uuid)[2];
		if(actualLvl+lvlToAdd>100000) {
			Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Impossible ! Le niveau maximal du sac à dos est atteint."));
			return;
		}
			
		int multiplicator = actualLvl*2;
		int lvlPrice = 500;
		int totalPrice = ((lvlPrice*multiplicator*lvlToAdd)+500);
		if(bank>totalPrice) {
			payForBpLvl(lvlToAdd, totalPrice);
		}else {
			Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEco()+"Tu n'as pas assez de gemmes &8("+ChatM.formatInt(totalPrice)+")"));
		}
	}
    public void payForBpLvl(int lvlToAdd, int totalPrice) {
		addLvl(lvlToAdd);

		Double[] banks = EchoSql.getInstance().getPlayerBalance().get(uuid);
		banks[2] -= totalPrice;
		Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEco()+"&c&l(- "+ChatM.formatInt(totalPrice)+ ") &7Gemmes, &a"+ ChatM.formatDouble(banks[2])+" &7restants."));
		Bukkit.getPlayer(uuid).sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"&a&l(+ "+lvlToAdd+ ") &7Niveau de sac &8("+getLvl()+"), &7Nouvelle capacité: &a" +getCapacity()));
		
		EchoSql.getInstance().getPlayerBalance().replace(uuid, banks);
    }
    

    public UUID getUuid() {
        return this.uuid;
    }

    public int getContenance() {
        return this.contenance;
    }

    public void setContenance(int newContenance) {
        this.contenance = newContenance;
        updateBpInInv();
    }
    

    // Create an ItemStack representing the backpack
    public ItemStack getBpItemStack() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatM.formatMessage("&6&lSac à dos de &e&l" + getPlayerName()));

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(BackPacksManager.BP_KEY, PersistentDataType.STRING, uuid.toString());

        List<String> newLore = new ArrayList<>();
        newLore.add(ChatM.formatMessage("&7Niveau: &e&l" + getLvl()));
        newLore.add(ChatM.formatMessage("&7Capacité: &e" + ChatM.formatInt(getCapacity())));
        newLore.add(ChatM.formatMessage("&7Contient: &e" + ChatM.formatInt(getContenance())));
        newLore.add(ChatM.formatMessage("&7Détails: &f"));
        for (Map.Entry<ItemStack, Integer> entry : this.contenanceItem.entrySet()) {
            ItemStack itemStack = entry.getKey();
            Integer quantity = entry.getValue();
            Material material = itemStack.getType();
            newLore.add(ChatM.formatMessage("&7&l◦ &f"+material+" &7"+ChatM.formatInt(quantity)));
        }   
        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }
    
    public ItemMeta getBpItemMeta() {
    	return getBpItemStack().getItemMeta();
    }

    // Add a backpack to a player's inventory
    public void replaceBpInInv() {
    	Bukkit.getPlayer(this.uuid).getInventory().setItem(getBpSlot(), getBpItemStack());
    }
    public void updateBpInInv() {
    	Bukkit.getPlayer(this.uuid).getInventory().getItem(getBpSlot()).setItemMeta(getBpItemMeta());
    }

    public int getBpSlot() {
    	return this.bpSlot;
    }
    
    public void setBpSlot(int newSlot) {
    	Bukkit.getPlayer(this.uuid).getInventory().setItem(getBpSlot(), new ItemStack(Material.AIR));
    	this.bpSlot=newSlot;
    	replaceBpInInv();
    }
}
	

