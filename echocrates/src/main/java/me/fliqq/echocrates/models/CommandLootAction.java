package me.fliqq.echocrates.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;


public class CommandLootAction implements LootAction, ConfigurationSerializable {

    private final String command;

    public CommandLootAction(String command) {
        this.command = command;
    }

    @Override
    public void execute(Player player) {
        String parsedCommand = command.replace("%player%", player.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "CommandLootAction");
        map.put("command", command);
        return map;
    }

    public static CommandLootAction deserialize(Map<String, Object> map) {
        return new CommandLootAction((String) map.get("command"));
    }
}