package me.fliqq.echomines.enom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;

public enum TerracottaAndConcreteBlocks {
    CONCRETE_WHITE(Material.WHITE_CONCRETE),
    CONCRETE_ORANGE(Material.ORANGE_CONCRETE),
    CONCRETE_MAGENTA(Material.MAGENTA_CONCRETE),
    CONCRETE_LIGHT_BLUE(Material.LIGHT_BLUE_CONCRETE),
    CONCRETE_YELLOW(Material.YELLOW_CONCRETE),
    CONCRETE_LIME(Material.LIME_CONCRETE),
    CONCRETE_PINK(Material.PINK_CONCRETE),
    CONCRETE_GRAY(Material.GRAY_CONCRETE),
    CONCRETE_LIGHT_GRAY(Material.LIGHT_GRAY_CONCRETE),
    CONCRETE_CYAN(Material.CYAN_CONCRETE),
    CONCRETE_PURPLE(Material.PURPLE_CONCRETE),
    CONCRETE_BLUE(Material.BLUE_CONCRETE),
    CONCRETE_BROWN(Material.BROWN_CONCRETE),
    CONCRETE_GREEN(Material.GREEN_CONCRETE),
    CONCRETE_RED(Material.RED_CONCRETE),
    CONCRETE_BLACK(Material.BLACK_CONCRETE),
    TERRACOTTA(Material.TERRACOTTA),
    WHITE_TERRACOTTA(Material.WHITE_TERRACOTTA),
    ORANGE_TERRACOTTA(Material.ORANGE_TERRACOTTA),
    MAGENTA_TERRACOTTA(Material.MAGENTA_TERRACOTTA),
    LIGHT_BLUE_TERRACOTTA(Material.LIGHT_BLUE_TERRACOTTA),
    YELLOW_TERRACOTTA(Material.YELLOW_TERRACOTTA),
    LIME_TERRACOTTA(Material.LIME_TERRACOTTA),
    PINK_TERRACOTTA(Material.PINK_TERRACOTTA),
    GRAY_TERRACOTTA(Material.GRAY_TERRACOTTA),
    LIGHT_GRAY_TERRACOTTA(Material.LIGHT_GRAY_TERRACOTTA),
    CYAN_TERRACOTTA(Material.CYAN_TERRACOTTA),
    PURPLE_TERRACOTTA(Material.PURPLE_TERRACOTTA),
    BLUE_TERRACOTTA(Material.BLUE_TERRACOTTA),
    BROWN_TERRACOTTA(Material.BROWN_TERRACOTTA),
    GREEN_TERRACOTTA(Material.GREEN_TERRACOTTA),
    RED_TERRACOTTA(Material.RED_TERRACOTTA),
    BLACK_TERRACOTTA(Material.BLACK_TERRACOTTA);

    private final Material material;
    private static final List<TerracottaAndConcreteBlocks> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    TerracottaAndConcreteBlocks(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public static TerracottaAndConcreteBlocks getRandomBlock() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}