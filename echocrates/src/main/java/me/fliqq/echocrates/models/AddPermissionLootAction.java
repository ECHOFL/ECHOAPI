package me.fliqq.echocrates.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import me.fliqq.echocrates.EchoCrates;

public class AddPermissionLootAction implements LootAction, ConfigurationSerializable {

    private final String permission;

    public AddPermissionLootAction(String permission) {
        this.permission = permission;
    }

    @Override
    public void execute(Player player) {
        PermissionAttachment attachment = player.addAttachment(EchoCrates.getInstance());
        attachment.setPermission(permission, true);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "AddPermissionLootAction");
        map.put("permission", permission);
        return map;
    }

    public static AddPermissionLootAction deserialize(Map<String, Object> map) {
        return new AddPermissionLootAction((String) map.get("permission"));
    }
}