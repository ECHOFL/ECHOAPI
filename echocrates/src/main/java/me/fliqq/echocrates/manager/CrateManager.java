package me.fliqq.echocrates.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echocrates.EchoCrates;
import me.fliqq.echocrates.models.Crates;



public class CrateManager {
    private static CrateManager instance;
    private final Map<Integer, Crates> crates;
    public static NamespacedKey CRATE_KEY;
    

    private CrateManager() {
    	crates = new HashMap<>();
    }

    public static CrateManager getInstance() {
        if (instance == null) {
            instance = new CrateManager();
            CRATE_KEY = new NamespacedKey(EchoCrates.getInstance(), "crates_echo");
        }
        return instance;
    }

    public Crates getCrate(int id) {
        return crates.get(id);
    }

    public void addCrate(int id, Crates crate) {
        crates.put(id, crate);
    }

    public Crates getCrate(ItemStack item) {
        if (isCrate(item)) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();

                if (data.has(CRATE_KEY, PersistentDataType.STRING)) {
                    String crateString = data.get(CRATE_KEY, PersistentDataType.STRING);
                    String idString = crateString.replace("CRATE", ""); // Cela donne "123"
                    int crateId = Integer.parseInt(idString);
                    
                    return getCrate(crateId);
                }
            }
        }
        return null;
    }
    
    public boolean isCrate(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                return data.has(CRATE_KEY, PersistentDataType.STRING);
            }
        }
        return false;
    }

}
