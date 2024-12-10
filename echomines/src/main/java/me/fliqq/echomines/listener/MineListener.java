package me.fliqq.echomines.listener;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomines.EchoMines;
import me.fliqq.echomines.manager.Mines;
import me.fliqq.echomines.manager.Regions;
import me.fliqq.echopackets.model.MinePackets;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echopickaxe.objects.BackPacks;
import me.fliqq.echopickaxe.objects.PPickaxes;
import me.fliqq.echosql.EchoSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;

public class MineListener implements Listener {

    private final Map<UUID, String> playerMine = EchoSql.getInstance().getPlayerMine();
    
    private final BackPacksManager bM = BackPacksManager.getInstance();
    private final PPickaxeManager pM = PPickaxeManager.getInstance();

    @EventHandler
    public void onMineBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PPickaxes pPickaxe = pM.getPick(uuid);
        ItemStack blockInitial = new ItemStack(event.getBlock().getType());

        Bukkit.getLogger().info("BlockBreakEvent triggered by player: " + player.getName());

        // Vérifie si le joueur a une mine définie
        if (!playerMine.containsKey(uuid)) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoMines() + "Vous n'avez pas de mine définie."));
            event.setCancelled(true);
            Bukkit.getLogger().info("Player " + player.getName() + " does not have a mine defined.");
            return;
        }

        String playerRankMine = playerMine.get(uuid);
        Location blockLocation = event.getBlock().getLocation();
        String mineName = Regions.getInstance().getMineNameFromLocation(blockLocation);

        // Vérifie si le joueur est dans une mine
        if (mineName == null || !isInMine(blockLocation, mineName)) {
            if (!player.hasPermission("echo.mines.breakout")) {
                player.sendMessage(ChatM.formatMessage(ChatM.logoMines() + "Vous ne pouvez pas casser de blocs hors mine."));
                event.setCancelled(true);
                Bukkit.getLogger().info("Player " + player.getName() + " tried to break a block outside of any mine.");
            }
            return;
        }

        // Vérifie si le joueur peut casser des blocs dans cette mine avec son rang
        if (!canBreakInMine(playerRankMine, mineName)) {
            event.setCancelled(true);
            player.sendMessage(ChatM.formatMessage(ChatM.logoMines() + "Vous ne pouvez pas casser de blocs dans la mine &e(" + mineName + ") &7avec le rang &e(" + playerRankMine + ")&7."));
            Bukkit.getLogger().info("Player " + player.getName() + " does not have the rank to break blocks in mine: " + mineName);
            return;
        }

        // Vérifie si le joueur utilise la pioche unique
        int pickSlot = pPickaxe.getPickSlot();
        if (player.getInventory().getHeldItemSlot() != pickSlot) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoMines() + "Seule votre pioche unique peut être utilisée dans les mines."));
            event.setCancelled(true);
            Bukkit.getLogger().info("Player " + player.getName() + " is not using their unique pickaxe.");
            return;
        }

        // Le joueur peut casser des blocs dans cette mine avec sa pioche unique
        World world = blockLocation.getWorld();
        Map<ItemStack, Integer> itemsDrop = new HashMap<>();
        itemsDrop.put(blockInitial, 1);

        // Supprime le bloc initial
        event.getBlock().setType(Material.AIR);
        Bukkit.getLogger().info("Block at location " + blockLocation + " broken by player " + player.getName());

        // Obtient les niveaux d'enchantement
        final int fortune = pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Fortune"));
        final int explosion = pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Explosion"));
        final int laser = pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Laser"));
        final int marteaupiqueur = pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("MarteauPiqueur"));
        final int atomique = pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Atomique"));

        Bukkit.getLogger().info("Player " + player.getName() + " has enchantments: Fortune=" + fortune + ", Explosion=" + explosion + ", Laser=" + laser + ", MarteauPiqueur=" + marteaupiqueur + ", Atomique=" + atomique);

        // Déclenche les effets des enchantements
        triggerExplosion(explosion, world, blockLocation, mineName, itemsDrop, player);
        triggerLaser(laser, world, blockLocation, mineName, itemsDrop);
        triggerMP(marteaupiqueur, world, blockLocation, mineName, itemsDrop);
        triggerAtomique(atomique, world, blockLocation, mineName, itemsDrop, player);

        // Ajoute les items au sac à dos du joueur
        int addedItems = addItemsToBp(uuid, itemsDrop, fortune);
        pPickaxe.addTotalBlockBreaked(addedItems);
        pPickaxe.addRealBlockBreaked();

        Bukkit.getLogger().info("Player " + player.getName() + " added " + addedItems + " items to backpack.");
    }

    
    
    private void triggerExplosion(int explosionChance, World world, Location blockLocation, String mineName, Map<ItemStack, Integer> itemsDrop, Player player) {
        Bukkit.getLogger().info("Triggering explosion enchant with chance: " + explosionChance);
        Random random = new Random();
        if (random.nextInt(5000) < explosionChance) {
            Bukkit.getLogger().info("Explosion enchant triggered.");
            
            MinePackets.explosionPacket(player, blockLocation);
            
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        Block targetBlock = world.getBlockAt(blockLocation.clone().add(x, y, z));
                        if (isInMine(targetBlock.getLocation(), mineName)) {
                            if (targetBlock.getType() == Material.AIR) {
                                continue;
                            }
                            ItemStack blockBreaked = new ItemStack(targetBlock.getType());
                            itemsDrop.merge(blockBreaked, 1, Integer::sum);
                            targetBlock.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    public void triggerLaser(int laserChance, World world, Location blockLocation, String mineName, Map<ItemStack, Integer> itemsDrop) {
        Bukkit.getLogger().info("Triggering laser enchant with chance: " + laserChance);
        Random random = new Random();
        if (random.nextInt(25000) > laserChance) {
            return;
        }

        Bukkit.getLogger().info("Laser enchant triggered.");

        // Les 14 directions : 6 principales et 8 diagonales
        int[][] directions = {
            {1, 0, 0}, // Est
            {-1, 0, 0}, // Ouest
            {0, 1, 0}, // Haut
            {0, -1, 0}, // Bas
            {0, 0, 1}, // Sud
            {0, 0, -1}, // Nord
            {1, 1, 0}, // Est-Haut
            {1, -1, 0}, // Est-Bas
            {-1, 1, 0}, // Ouest-Haut
            {-1, -1, 0}, // Ouest-Bas
            {1, 0, 1}, // Est-Sud
            {1, 0, -1}, // Est-Nord
            {-1, 0, 1}, // Ouest-Sud
            {-1, 0, -1} // Ouest-Nord
        };

        for (int[] direction : directions) {
            Location currentLocation = blockLocation.clone();

            while (true) {
                currentLocation.add(direction[0], direction[1], direction[2]);
                Block targetBlock = world.getBlockAt(currentLocation);

                if (!isInMine(targetBlock.getLocation(), mineName)) {
                    break;
                }

                if (targetBlock.getType() == Material.AIR) {
                    continue;
                }

                ItemStack blockBreaked = new ItemStack(targetBlock.getType());
                itemsDrop.merge(blockBreaked, 1, Integer::sum);
                targetBlock.setType(Material.AIR);
            }
        }
    }
    
    public void triggerMP(int mpChance, World world, Location blockLocation, String mineName, Map<ItemStack, Integer> itemsDrop) {
        Bukkit.getLogger().info("Triggering MarteauPiqueur enchant with chance: " + mpChance);
        Random random = new Random();

        // Vérifie si l'enchantement MarteauPiqueur est déclenché
        if (random.nextInt(80000) < mpChance) {
            Bukkit.getLogger().info("MarteauPiqueur enchant triggered.");

            // Récupère la région de la mine
            Mines mine = Regions.getInstance().findMine(mineName);
            if (mine != null) {
                int yCoord = blockLocation.getBlockY();

                int minX = mine.getLowX();
                int maxX = mine.getHighX();
                int minZ = mine.getLowZ();
                int maxZ = mine.getHighZ();

                // Parcourir tous les blocs de la couche y dans les limites de la mine
                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block targetBlock = world.getBlockAt(x, yCoord, z);

                        if (targetBlock.getType() == Material.AIR) {
                            continue;
                        }

                        ItemStack blockBreaked = new ItemStack(targetBlock.getType());
                        itemsDrop.merge(blockBreaked, 1, Integer::sum);
                        targetBlock.setType(Material.AIR);
                    }
                }
            } else {
                Bukkit.getLogger().warning("Mine " + mineName + " not found.");
            }
        }
    }
    public void triggerAtomique(int atomicChance, World world, Location blockLocation, String mineName, Map<ItemStack, Integer> itemsDrop,Player player) {
        Bukkit.getLogger().info("Triggering Atomique enchant with chance: " + atomicChance);
        Random random = new Random();

        // Vérifie si l'enchantement Atomique est déclenché
        if (random.nextInt(100000) < atomicChance) {
            Bukkit.getLogger().info("Atomique enchant triggered.");

            // Récupère la région de la mine
            Mines mine = Regions.getInstance().findMine(mineName);
            if (mine != null) {
                // Rayon de la zone circulaire
                int radius = 8;

                int minY = mine.getLowY();
                int maxY = mine.getHighY();

                // Parcourir tous les blocs dans un rayon autour du bloc initial
                for (int x = blockLocation.getBlockX() - radius; x <= blockLocation.getBlockX() + radius; x++) {
                    for (int z = blockLocation.getBlockZ() - radius; z <= blockLocation.getBlockZ() + radius; z++) {
                        // Vérifier si le bloc est à l'intérieur du rayon et dans les limites de la mine
                        if (Math.pow(x - blockLocation.getBlockX(), 2) + Math.pow(z - blockLocation.getBlockZ(), 2) <= radius * radius) {
                            for (int y = maxY; y >= minY; y--) {
                                Block targetBlock = world.getBlockAt(x, y, z);

                                // Vérifier si le bloc est dans la mine et n'est pas de l'air
                                if (isInMine(targetBlock.getLocation(), mineName) && targetBlock.getType() != Material.AIR) {
                                    ItemStack blockBreaked = new ItemStack(targetBlock.getType());
                                    itemsDrop.merge(blockBreaked, 1, Integer::sum);
                                    targetBlock.setType(Material.AIR);
                                }
                            }
                        }
                    }
                }
                Location strikeLocation = blockLocation;
                strikeLocation.setY(minY+1);
                world.strikeLightningEffect(strikeLocation.add(new Vector(5, 0, 5)));
                world.strikeLightningEffect(strikeLocation.add(new Vector(-5, 0, -5)));
                world.strikeLightningEffect(strikeLocation.add(new Vector(-5, 0, 5)));
                world.strikeLightningEffect(strikeLocation.add(new Vector(5, 0, -5)));
                world.strikeLightningEffect(strikeLocation);

                //player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);



            } else {
                Bukkit.getLogger().warning("Mine " + mineName + " not found.");
            }
        }
    }

    private int addItemsToBp(UUID uuid, Map<ItemStack, Integer> itemsDrop, int fortuneLevel) {

    	Player p = Bukkit.getPlayer(uuid);
        BackPacks playerBp = bM.getBackPack(uuid);
        int capacity = playerBp.getCapacity(); // 200
        int contenance = playerBp.getContenance(); // 195

        int totalQuantity = 0;
        int noFortTotalQuantity = 0;
        Builder contenanceMessageBuilder = Component.text(); // Utilisation de Builder

        for (Map.Entry<ItemStack, Integer> entry : itemsDrop.entrySet()) {
            ItemStack item = entry.getKey();
            int iQuantity = entry.getValue();
            int fQuantity = iQuantity * (1 + fortuneLevel / 20); // 10

            int remainingCapacity = capacity - (contenance + totalQuantity);
            noFortTotalQuantity += iQuantity;

            				 
            if (fQuantity > remainingCapacity) {
            	if(remainingCapacity!=0) {
	                contenanceMessageBuilder.append(Component.text("(+) ").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
	                .append(Component.text(ChatM.formatInt(remainingCapacity) + " ").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
	                .append(Component.text(item.getType().toString() + "  ").color(NamedTextColor.DARK_GRAY));
	                playerBp.addContenanceItem(item, remainingCapacity);
	                totalQuantity += remainingCapacity;

            	}
                final Title.Times times = Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(750));
                final Component mainTitle = Component.text(ChatM.formatMessage("&cSac à dos plein !"));
                final Component subTitle = Component.text(ChatM.formatMessage("&7Vend à ta mine")); // Subtitle
                final Title title = Title.title(mainTitle, subTitle,times);
                p.showTitle(title);

                break; // Exit the loop since the backpack is full
            } else {
                contenanceMessageBuilder.append(Component.text("(+) ").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
                .append(Component.text(ChatM.formatInt(fQuantity) + " ").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
                .append(Component.text(item.getType().toString() + "  ").color(NamedTextColor.DARK_GRAY));
                
                playerBp.addContenanceItem(item, fQuantity);
                totalQuantity += fQuantity;
            }
        }

        playerBp.setContenance(contenance + totalQuantity);
        p.sendActionBar(contenanceMessageBuilder.build());        
        return noFortTotalQuantity;
    }
    
    private boolean canBreakInMine(String playerStatus, String mineName) {
        int playerIndex = playerStatus.toLowerCase().charAt(0) - 'a';
        int mineIndex = mineName.toLowerCase().charAt(0) - 'a';
        return playerIndex >= mineIndex;
    }
    

    private boolean isInMine(Location location, String mineName) {
        Mines mine = Regions.getInstance().findMine(mineName);
        if (mine == null) {
            return false; // La mine n'existe pas, donc la location ne peut pas être à l'intérieur
        }
        return mine.isWithin(location); // Utiliser la méthode isWithin pour vérifier si la location est à l'intérieur de la mine
    }
}