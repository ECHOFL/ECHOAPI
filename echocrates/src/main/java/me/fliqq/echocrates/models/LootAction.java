package me.fliqq.echocrates.models;


import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public interface LootAction extends ConfigurationSerializable  {
    void execute(Player player);
    Map<String, Object> serialize();
}
