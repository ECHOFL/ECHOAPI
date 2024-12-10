package me.fliqq.echodungeon.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;


public class DungeonManager {

    private final SimplePlugin plugin;
    private final WorldManager worldManager;
    private final File schematicsFolder;
    private final Random random;

    public DungeonManager(SimplePlugin plugin, WorldManager worldManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
        this.schematicsFolder = new File(plugin.getDataFolder(), "schematicsdungeon");
        this.random = new Random();
    }

    public void createDungeon() {
        worldManager.createWorld("dungeon_world_" + System.currentTimeMillis());
    }

    public void placeChunk(World world, int chunkX, int chunkZ, String schematicName) {
        File schematicFile = new File(schematicsFolder, schematicName);

        if (!schematicFile.exists()) {
            plugin.getLogger().warning("Schematic file not found: " + schematicFile.getPath());
            return;
        }

        plugin.getLogger().info("Selected schematic: " + schematicFile.getName());

        try (FileInputStream fis = new FileInputStream(schematicFile);
             ClipboardReader reader = ClipboardFormats.findByFile(schematicFile).getReader(fis)) {

            Clipboard clipboard = reader.read();

            // Schedule the operation with a delay
            Bukkit.getScheduler().runTask(plugin, () -> {
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    Operation operation = holder.createPaste(editSession)
                            .to(BlockVector3.at(chunkX << 4, 0, chunkZ << 4))
                            .build();

                    // Perform the operation and complete it
                    Operations.complete(operation);
                    plugin.getLogger().info("Pasted schematic: " + schematicFile.getName() + " at chunk (" + chunkX + ", " + chunkZ + ")");
                } catch (WorldEditException e) {
                    plugin.getLogger().severe("Failed to paste schematic: " + schematicFile.getName());
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load schematic: " + schematicFile.getName());
            e.printStackTrace();
        }
    }
    
    public String getRandomSchematic() {
        File[] schematics = schematicsFolder.listFiles((dir, name) -> name.endsWith(".schem") && !name.equals("dungeonspawn.schem"));
        if (schematics != null && schematics.length > 0) {
            return schematics[random.nextInt(schematics.length)].getName();
        } else {
            plugin.getLogger().warning("No valid dungeon schematics found in folder: " + schematicsFolder.getPath());
            return null;
        }
    }
}