package me.fliqq.echopickaxe.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echopickaxe.EchoPickaxe;
import me.fliqq.echopickaxe.objects.PPickaxes;

public class PPickaxeManager {
    private static PPickaxeManager instance;
    private final Map<UUID, PPickaxes> ppickaxes;
    public static NamespacedKey PPICK_KEY;

    private PPickaxeManager() {
        ppickaxes = new HashMap<>();
    }

    public static PPickaxeManager getInstance() {
        if (instance == null) {
            instance = new PPickaxeManager();
            PPICK_KEY = new NamespacedKey(EchoPickaxe.getInstance(), "player_uuid_pick");
        }
        return instance;
    }

    public PPickaxes getPick(UUID uuid) {
        return ppickaxes.get(uuid);
    }

    public void addPick(UUID uuid, PPickaxes ppickaxe) {
        ppickaxes.put(uuid, ppickaxe);
    }

    public boolean hasPickInMap(UUID uuid) {
        return ppickaxes.containsKey(uuid);
    }

    public boolean hasPPickInInv(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isPPick(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPPick(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                return data.has(PPICK_KEY, PersistentDataType.STRING);
            }
        }
        return false;
    }
}
