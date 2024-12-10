package me.fliqq.echocrates.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.fliqq.echocrates.EchoCrates;
import me.fliqq.echocrates.models.AddItemLootAction;
import me.fliqq.echocrates.models.AddPermissionLootAction;
import me.fliqq.echocrates.models.CommandLootAction;
import me.fliqq.echocrates.models.CrateLoots;
import me.fliqq.echocrates.models.LootAction;
import me.fliqq.echocrates.util.ComponentUtil;
import net.kyori.adventure.text.Component;

public class LootManager {

    private static LootManager INSTANCE;
    private final Set<CrateLoots> crateLoots = new HashSet<>();

    private File file;
    private YamlConfiguration config;

    private LootManager() {
        File dataFolder = EchoCrates.getInstance().getDataFolder();
        if (!dataFolder.exists()) {
            EchoCrates.logger.info("CrateLoots file introuvable, création...");
            dataFolder.mkdir();
        }

        file = new File(dataFolder, "crateLoots.yml");
        config = new YamlConfiguration();
    }

    public static LootManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LootManager();
        }
        return INSTANCE;
    }

    public void load() {
        try {
            if (!file.exists()) {
                EchoCrates.logger.info("CrateLoots fichier introuvable, création...");
                file.createNewFile();
            }
            config.load(file);
            EchoCrates.logger.info("CrateLoots fichier chargé avec succès.");

            crateLoots.clear();

            if (config.isSet("CrateLoots")) {
                for (Map<?, ?> rawLootsMap : config.getMapList("CrateLoots")) {
                    Map<String, Object> map = (Map<String, Object>) rawLootsMap;
                    int id = (Integer) map.get("id");
                    String name = (String) map.get("name");
                    Component rarity = ComponentUtil.deserializeComponent((String) map.get("rarity"));
                    Component nameComponent = ComponentUtil.deserializeComponent((String) map.get("nameComponent"));
                    
                    // Désérialisation de l'ItemStack
                    ItemStack item = ItemStack.deserialize((Map<String, Object>) map.get("item"));
                    
                    LootAction lootAction = deserializeLootAction((Map<String, Object>) map.get("lootActionData"));

                    crateLoots.add(new CrateLoots(id, name, rarity, nameComponent, item, lootAction));
                }
            } else {
                EchoCrates.logger.info("No crateLoots trouvés dans le fichier de configuration CrateLoots.");
            }

        } catch (IOException e) {
            EchoCrates.logger.log(Level.SEVERE, "Echec de la création du fichier CrateLoots.", e);
        } catch (Throwable t) {
            EchoCrates.logger.log(Level.SEVERE, "Echec du chargement du fichier CrateLoots.", t);
        }
    }

    public LootAction deserializeLootAction(Map<String, Object> map) {
        String type = (String) map.get("type");
        switch (type) {
            case "CommandLootAction":
                return new CommandLootAction((String) map.get("command"));
            case "AddPermissionLootAction":
                String permission = (String) map.get("permission");
                return new AddPermissionLootAction(permission); // Null player, will be set in execute
            case "AddItemLootAction":
                ItemStack item = ItemStack.deserialize((Map<String, Object>) map.get("item"));
                return new AddItemLootAction(item);
            default:
                throw new IllegalArgumentException("Unknown LootAction type: " + type);
        }
    }
    
    public void save() {
        List<Map<String, Object>> serializedCrateLoots = new ArrayList<>();

        for (CrateLoots loot : crateLoots) {
            Map<String, Object> serializedLoot = loot.serialize();

            serializedCrateLoots.add(serializedLoot);
            
            EchoCrates.logger.info("Loot serialisé: " + loot.getName());
        }

        config.set("CrateLoots", serializedCrateLoots);

        try {
            config.save(file);
            EchoCrates.logger.info("Fichier CrateLoots enregistré avec succès.");
        } catch (Exception e) {
            EchoCrates.logger.log(Level.SEVERE, "Echec de l'enregistrement du fichier CrateLoots.", e);
        }
    }

    public CrateLoots findLoot(int id) {
        for (CrateLoots crateLoots : crateLoots) {
            if (crateLoots.getId() == id) {
                return crateLoots;
            }
        }
        return null;
    }

    public CrateLoots findLoot(String name) {
        for (CrateLoots crateLoots : crateLoots) {
            if (crateLoots.getName().equalsIgnoreCase(name)) {
                return crateLoots;
            }
        }
        return null;
    }

    public void saveCrateLoot(int id, String name, Component rarity, Component nameComponent, ItemStack item, LootAction lootAction) {
        crateLoots.add(new CrateLoots(id, name, rarity, nameComponent, item, lootAction));
        save();
    }

    public Set<CrateLoots> getLootsTable() {
        return Collections.unmodifiableSet(crateLoots);
    }
}