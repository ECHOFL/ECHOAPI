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
import me.fliqq.echopickaxe.objects.BackPacks;

public class BackPacksManager {
    private static BackPacksManager instance;
    private final Map<UUID, BackPacks> backpacks;
    public static NamespacedKey BP_KEY;

    private BackPacksManager() {
        backpacks = new HashMap<>();
    }

    public static BackPacksManager getInstance() {
        if (instance == null) {
            instance = new BackPacksManager();
            BP_KEY = new NamespacedKey(EchoPickaxe.getInstance(), "player_uuid_bp");
        }
        return instance;
    }

    public BackPacks getBackPack(UUID uuid) {
        return backpacks.get(uuid);
    }

    public void addBackPack(UUID uuid, BackPacks backpack) {
        backpacks.put(uuid, backpack);
    }

    public boolean hasBackPackInMap(UUID uuid) {
        return backpacks.containsKey(uuid);
    }

    public boolean hasBackPackInInv(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isBackPack(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBackPack(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                return data.has(BP_KEY, PersistentDataType.STRING);
            }
        }
        return false;
    }
}