package me.fliqq.echocrates.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemLootAction implements LootAction, ConfigurationSerializable {

    private final ItemStack item;

    public AddItemLootAction(ItemStack item) {
        this.item = item;
    }

    @Override
    public void execute(Player player) {
        player.getInventory().addItem(item);
    }



    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "AddItemLootAction");
        map.put("item", item.serialize());
        return map;
    }

    public static AddItemLootAction deserialize(Map<String, Object> map) {
        ItemStack item = ItemStack.deserialize((Map<String, Object>) map.get("item"));
        return new AddItemLootAction(item);
    }


}
