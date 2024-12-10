package me.fliqq.echomultieconomy.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.EchoMultiEconomy;

public class Check {

    private Player player;
    private Double valeur;
    private String devise;
    private ItemStack itemStack;
    private final static NamespacedKey CHECK_KEY = new NamespacedKey(EchoMultiEconomy.getInstance(), "check_key");
    private final static NamespacedKey VALUE_KEY = new NamespacedKey(EchoMultiEconomy.getInstance(), "check_value");
    private final static NamespacedKey CURRENCY_KEY = new NamespacedKey(EchoMultiEconomy.getInstance(), "check_currency");

    public Check(Player player, String devise, Double valeur) {
        this.player = player;
        this.valeur = valeur;
        this.devise = devise;
        this.itemStack = setItemStack();
    }

    private ItemStack setItemStack() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta meta = itemStack.getItemMeta();
        
        if(devise.equalsIgnoreCase("echo"))
        	meta.setDisplayName(ChatM.formatMessage("&7{&e&l" + devise.toUpperCase() + "&7} &fCheck de &a" + player.getName()));
        else if(devise.equalsIgnoreCase("token"))
        	meta.setDisplayName(ChatM.formatMessage("&7{&d&l" + devise.toUpperCase() + "&7} &fCheck de &a" + player.getName()));
        else if(devise.equalsIgnoreCase("gemme"))
        	meta.setDisplayName(ChatM.formatMessage("&7{&b&l" + devise.toUpperCase() + "&7} &fCheck de &a" + player.getName()));
        else if(devise.equalsIgnoreCase("xp"))
        	meta.setDisplayName(ChatM.formatMessage("&7{&a&l" + devise.toUpperCase() + "&7} &fCheck de &a" + player.getName()));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatM.formatMessage("&fMontant: &e" + ChatM.formatDouble(valeur)));

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);
        loreList.add(ChatM.formatMessage("&fSigné le: &7" + formattedDateTime));
        meta.setLore(loreList);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(CHECK_KEY, PersistentDataType.STRING, "CHECK");
        data.set(VALUE_KEY, PersistentDataType.DOUBLE, valeur);
        data.set(CURRENCY_KEY, PersistentDataType.STRING, devise);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public static boolean isCheck(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                return data.has(CHECK_KEY, PersistentDataType.STRING);
            }
        }
        return false;
    }

    public static Double getCheckValue(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                if (data.has(VALUE_KEY, PersistentDataType.DOUBLE)) {
                    return data.get(VALUE_KEY, PersistentDataType.DOUBLE);
                }
            }
        }
        return -1.0;
    }

    public static String getCheckCurrency(ItemStack item) {
        if (isCheck(item)) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                String currencyWithColor = data.getOrDefault(CURRENCY_KEY, PersistentDataType.STRING, "");
                
                // Strip color codes from the currency string
                String currency = ChatColor.stripColor(currencyWithColor);
                
                return currency;
            }
        }
        return null;
    }

}
