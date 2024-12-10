package me.fliqq.echocrates.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

import me.fliqq.echocrates.util.ComponentUtil;
import net.kyori.adventure.text.Component;

public class CrateLoots implements ConfigurationSerializable {

    private int id;
    private String name;
    private Component rarity;
    private Component nameComponent;
    private ItemStack lootItem;
    private LootAction lootAction; // Ajouter le champ LootAction
    

    public CrateLoots(int id, String name, Component rarity, Component nameComponent, ItemStack lootItem, LootAction lootAction) {
        this.setId(id);
        this.setName(name);
        this.setRarity(rarity);
        this.setNameComponent(nameComponent);
        this.setLootItem(lootItem);
        this.setLootAction(lootAction);
    }
    
    public void executeLootAction(Player player) {
        if (lootAction != null) {
            lootAction.execute(player);
        }
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("rarity", ComponentUtil.serializeComponent(getRarity()));
        map.put("nameComponent", ComponentUtil.serializeComponent(getNameComponent()));
        map.put("item", lootItem.serialize());
        
        if (lootAction != null) {
            map.put("lootActionType", lootAction.getClass().getSimpleName()); // Store action type
            map.put("lootActionData", lootAction.serialize()); // Store serialized action data
        }

        return map;
    }

    public static CrateLoots deserialize(Map<String, Object> map) {
        int id = (int) map.get("id");
        String name = (String) map.get("name");
        Component rarity = (Component) map.get("rarity");
        Component nameComponent = (Component) map.get("nameComponent");
        ItemStack item = (ItemStack) map.get("item");
        LootAction lootAction = (LootAction) map.get("lootAction");
        
        return new CrateLoots(id, name, rarity, nameComponent, item, lootAction);
    }
    
    public String getLootActionType() {
        if (lootAction instanceof CommandLootAction) {
            return "Command";
        } else if (lootAction instanceof AddItemLootAction) {
            return "GiveObject";
        } else if (lootAction instanceof AddPermissionLootAction) {
            return "Permissions";
        } else {
            return "Unknown";
        }
    }
	
    
	/*public Map<String, Object> serialize(){
		Map<String, Object> map = new HashMap<>();
	
		map.put("Name", name);
		map.put("Primary", primaryLocation);
		map.put("Secondary", secondaryLocation);
		
		return map;
	}
	
	public static Region deserialize(Map<String, Object> map) {
		String name = (String) map.get("Name");
		Location primary = (Location) map.get("Primary");
		Location secondary = (Location) map.get("Secondary");
		
		return new Region(name, primary, secondary);
	}
	
	/*
   /* public static final List<CrateLoots> CRATE_LOOTS = Arrays.asList(
            new CrateLoots(
            		1,
                    "money1",
                    Component.text("Commun"),                    
                    Component.text("Echos équivalent à 2k blocs"),
                    ItemCreator.of(CompMaterial.GOLD_NUGGET).glow(true).make()),
            new CrateLoots(
            		1,
                    "money2",
                    Component.text("Commun"),                    
                    Component.text("Echos équivalent à 5k blocs"),
                    ItemCreator.of(CompMaterial.GOLD_INGOT).glow(true).make()),
            new CrateLoots(
            		1,
                    "money3",
                    Component.text("Peu Commun"),                    
                    Component.text("Echos équivalent à 10k blocs"),
                    ItemCreator.of(CompMaterial.GOLD_BLOCK).glow(true).make())
    );*/

    
    

	public Component getNameComponent() {
		return nameComponent;
	}

	public void setNameComponent(Component nameComponent) {
		this.nameComponent = nameComponent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Component getRarity() {
		return rarity;
	}

	public void setRarity(Component rarity) {
		this.rarity = rarity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemStack getLootItem() {
		return lootItem;
	}

	public void setLootItem(ItemStack lootItem) {
		this.lootItem = lootItem;
	}
	public CompMaterial getCompMaterial() {
		return CompMaterial.fromItem(lootItem);
	}


	public LootAction getLootAction() {
		return lootAction;
	}

	public void setLootAction(LootAction lootAction) {
		this.lootAction = lootAction;
	}
}
