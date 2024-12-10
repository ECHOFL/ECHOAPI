package me.fliqq.echoessential.manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fliqq.echosql.EchoSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class ChatM {

    public static String formatMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String noPermissionMessage(){
        return formatMessage("&c&lECHO >>&7 Vous n'avez pas la permission d'exécuter cette commande.");
    }
    public static String logoEcho(){
        return formatMessage("&c&lECHO >>&7 ");
    }
    public static String logoEnchant(){
        return formatMessage("&5&lENCHANTS >>&7 ");
    }
    public static String logoEco(){
        return formatMessage("&6&lECHO&e&lNOMY >>&7 ");
    }
    public static String logoRegions(){
        return formatMessage("&2&lREGIONS >>&7 ");
    }
    public static String logoMines(){
        return formatMessage("&8&lMINES >>&7 ");
    }
    public static String logoPrison(){
        return formatMessage("&4&lPRISON >>&7 ");
    }
    public static String formatInt(long number) {
        if (number >= 1_000_000_000_000_000_000L) {
            return String.format("%.1fkQ", number / 1_000_000_000_000_000_000.0);
        }
        else if (number >= 1_000_000_000_000_000L) {
            return String.format("%.1fQ", number / 1_000_000_000_000_000.0);
        }
        else if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        }
        else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        }
        else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        }
        else if (number >= 10_000L) {
            return String.format("%.1fk", number / 1_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fk", Math.floor(number / 100.0) / 10.0);
        }
        return String.valueOf(number);  // Retourne tel quel si moins de 1000
    }
    public static String formatDouble(double number) {
        if (number >= 1_000_000_000_000_000_000L) {
            return String.format("%.1fkQ", number / 1_000_000_000_000_000_000.0);
        }
        if (number >= 1_000_000_000_000_000L) {
            return String.format("%.1fQ", number / 1_000_000_000_000_000.0);
        }
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        }
        if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        }
        if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        }
        if (number >= 1_000L) {
            return String.format("%.1fk", number / 1_000.0);
        }
        return String.format("%.2f", number);  // Retourne tel quel si moins de 1000
    }
}
