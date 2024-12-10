package me.fliqq.echocrates.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import me.fliqq.echocrates.manager.CrateManager;
import me.fliqq.echocrates.manager.LootManager;

public enum Crates {
    CRATE1(1,
    		"Crate Vote",
    		ItemCreator.of(CompMaterial.ENDER_CHEST, "&7&lBox Vote", " ", "&7Rareté: &fCommun").glow(true).make(),
            new HashMap<CrateLoots, Integer>() {/**
				 * 
				 */
				private static final long serialVersionUID = 8033978685539409330L;

			{
                put(LootManager.getInstance().findLoot(1), 75);
                put(LootManager.getInstance().findLoot(2), 25);
            }}
    ),
    
    CRATE2(2, 
    	"Crate Rare", 
			ItemCreator.of(CompMaterial.ENDER_CHEST, "&5&lBox Rare", " ", "&7Rareté: &Rare").glow(true).make(),
            new HashMap<CrateLoots, Integer>() {/**
				 * 
				 */
				private static final long serialVersionUID = 8033978685539409330L;

			{
                put(LootManager.getInstance().findLoot(1), 25);
                put(LootManager.getInstance().findLoot(2), 25);
                put(LootManager.getInstance().findLoot(3), 50);
            }});

	
    private int id;
    private String name;
    private ItemStack crateItem;
    private HashMap<CrateLoots, Integer> sortedLoots;

    Crates(int id, String name, ItemStack crateItem, HashMap<CrateLoots, Integer> sortedLoots) {
        this.id = id;
        this.name = name;
        this.crateItem = crateItem;
        this.sortedLoots = sortedLoots;
        
        markAsCrate(crateItem);
    }

    private void markAsCrate(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            CrateManager.getInstance();
			meta.getPersistentDataContainer().set(CrateManager.CRATE_KEY, PersistentDataType.STRING, "CRATE" + id);
            itemStack.setItemMeta(meta);
        }
    }

    public LinkedHashMap<CrateLoots, Integer> sortLootsByValue() {
        // Convertir la HashMap en une List d'entrées (clé, valeur)
        List<Map.Entry<CrateLoots, Integer>> list = new LinkedList<>(sortedLoots.entrySet());

        // Trier la liste en fonction de la valeur Integer
        list.sort(Comparator.comparing(Map.Entry::getValue));

        // Construire une nouvelle LinkedHashMap triée à partir de la liste triée
        LinkedHashMap<CrateLoots, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<CrateLoots, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<CrateLoots, Integer> getSortedLoots() {
        return sortedLoots;
    }

    public void setSortedLoots(HashMap<CrateLoots, Integer> sortedLoots) {
        this.sortedLoots = sortedLoots;
    }

    public ItemStack getCrateItem() {
        return crateItem;
    }

    public void setCrateItem(ItemStack crateItem) {
        this.crateItem = crateItem;
    }
}

//HARDCODE DES BOX
/*private static final List<Crates> CRATES_LIST = Arrays.asList(
		new Crates(1, "Box Vote", Component.text("Box Vote").color(NamedTextColor.GRAY),
				ItemCreator.of(CompMaterial.NOTE_BLOCK).make(),
				new TreeMap<Integer, CrateLoots>(Map.of(
						60, CrateLoots.getLootById(1),
						30, CrateLoots.getLootById(2),
						10, CrateLoots.getLootById(3)))),
		new Crates(2, "Box Vote2", Component.text("Box Vote+").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD),
				ItemCreator.of(CompMaterial.NOTE_BLOCK).make(),
				new TreeMap<Integer, CrateLoots>(Map.of(
						20, CrateLoots.getLootById(1),
						50, CrateLoots.getLootById(2),
						30, CrateLoots.getLootById(3))))
);

public static Crates getCratesById(int id) {
	for(Crates crates : CRATES_LIST) {
		if(crates.getId()==id){
			return crates;
		}
	}
	return null;
}*/

