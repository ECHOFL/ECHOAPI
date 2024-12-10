package me.fliqq.echocrates.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ComponentUtil {

    public static String serializeComponent(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static Component deserializeComponent(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }
}