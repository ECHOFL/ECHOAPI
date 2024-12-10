package me.fliqq.echochat.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ComponentUtils {
	private final static GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();

	public static String componentToString(Component component) {
		return GSON_COMPONENT_SERIALIZER.serialize(component);
	}
	
	public static Component stringToComponent(String jsonString) {
		return GSON_COMPONENT_SERIALIZER.deserialize(jsonString);
	}
}
