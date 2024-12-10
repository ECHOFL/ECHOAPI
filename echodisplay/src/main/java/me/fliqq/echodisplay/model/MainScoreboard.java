package me.fliqq.echodisplay.model;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fliqq.echodisplay.EchoDisplay;
import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.EchoMultiEconomy;
import me.fliqq.echomultieconomy.hook.VaultHook;
import me.fliqq.echosql.EchoSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MainScoreboard implements Runnable{
	
	private HashMap<UUID, Double> echoBalances= EchoMultiEconomy.getInstance().getEchoBalances();
    private final static HashMap<UUID, Double[]> playerBalance = EchoSql.getInstance().getPlayerBalance();
    private final HashMap<UUID, String> playerMine = EchoSql.getInstance().getPlayerMine();
    private final HashMap<UUID, Integer> playerPrestige = EchoSql.getInstance().getPlayerPrestige();
    private final HashMap<UUID, Integer> playerRebirth= EchoSql.getInstance().getPlayerRebirth();
	private final static MainScoreboard INSTANCE_MAIN_SCOREBOARD = new MainScoreboard();  //SINGLETON
	
	private MainScoreboard() {
	}

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != null && player.getScoreboard().getObjective(EchoDisplay.getInstance().getName()) != null) {
                updateSb(player);
            } else {
                createNewSb(player);
            }
        }
    }

    private void createNewSb(Player player) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Component titleComponent = miniMessage.deserialize("<gradient:#913d54:#f79459:red><bold>ECHO-PRISON</gradient>");


        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(
                EchoDisplay.getInstance().getName(),  // Nom de l'objectif
                Criteria.DUMMY,                       // Utiliser l'énumération Criteria au lieu de la chaîne "dummy"
                titleComponent
        );

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Adding scores using plain strings, while creating the strings with Adventure components
        objective.getScore(textToLegacy("      (16/6/2024)", NamedTextColor.DARK_GRAY)).setScore(12);
        objective.getScore(textToLegacy("     7 joueurs", NamedTextColor.GRAY, TextDecoration.BOLD)).setScore(11);
        objective.getScore(textToLegacy("Informations", NamedTextColor.RED, TextDecoration.BOLD)).setScore(10);

        // Teams to avoid flickering
        addTeamEntry(scoreboard, objective, "team1", "Grade ", getPlayerRank(player), ChatColor.DARK_AQUA, NamedTextColor.WHITE, 8);
        addTeamEntry(scoreboard, objective, "team2", "Mine ", getPlayerMine(player), ChatColor.YELLOW, NamedTextColor.GOLD, 7);
        addTeamEntry(scoreboard, objective, "team3", "Prestige ", getPlayerPrestige(player), ChatColor.DARK_GREEN, NamedTextColor.GOLD, 6);
        addTeamEntry(scoreboard, objective, "team4", "Renaissance ", getPlayerRebirth(player), ChatColor.DARK_RED, NamedTextColor.GOLD, 5);

        objective.getScore(textToLegacy(" ", NamedTextColor.GRAY)).setScore(4);  // Empty line for spacing

        addTeamEntry(scoreboard, objective, "team5", "Echos ", getPlayerEchos(player), ChatColor.DARK_PURPLE, NamedTextColor.YELLOW, 3);
        addTeamEntry(scoreboard, objective, "team6", "Tokens ", getPlayerTokens(player), ChatColor.LIGHT_PURPLE, NamedTextColor.LIGHT_PURPLE, 2);
        addTeamEntry(scoreboard, objective, "team7", "Gemmes ", getPlayerGemmes(player), ChatColor.BLACK, NamedTextColor.AQUA, 1);
        addTeamEntry(scoreboard, objective, "team8", "XP ", getPlayerXp(player), ChatColor.GOLD, NamedTextColor.GREEN, 0);

        // Set the player's scoreboard
        player.setScoreboard(scoreboard);
    }

    private void addTeamEntry(Scoreboard scoreboard, Objective objective, String teamName, String prefix, String suffix, ChatColor prefixColor, NamedTextColor suffixColor, int score) {
        Team team = scoreboard.registerNewTeam(teamName);
        String entry = prefixColor+""; // Unique entry for each team based on prefixColor
        team.addEntry(entry);
        team.prefix(formatPrefix(prefix));
        team.suffix(Component.text(suffix).color(suffixColor));
        objective.getScore(entry).setScore(score);

    }

    private void updateSb(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        // Update team suffixes with current player information
        updateTeamEntry(scoreboard, "team1", getPlayerRank(player), NamedTextColor.WHITE);
        updateTeamEntry(scoreboard, "team2", getPlayerMine(player), NamedTextColor.GOLD);
        updateTeamEntry(scoreboard, "team3", getPlayerPrestige(player), NamedTextColor.GOLD);
        updateTeamEntry(scoreboard, "team4", getPlayerRebirth(player), NamedTextColor.GOLD);
        updateTeamEntry(scoreboard, "team5", getPlayerEchos(player), NamedTextColor.YELLOW);
        updateTeamEntry(scoreboard, "team6", getPlayerTokens(player), NamedTextColor.LIGHT_PURPLE);
        updateTeamEntry(scoreboard, "team7", getPlayerGemmes(player), NamedTextColor.AQUA);
        updateTeamEntry(scoreboard, "team8", getPlayerXp(player), NamedTextColor.GREEN);
    }

    private void updateTeamEntry(Scoreboard scoreboard, String teamName, String value, NamedTextColor suffixColor) {
        Team team = scoreboard.getTeam(teamName);
        if (team != null) {

        	team.suffix(Component.text(value).color(suffixColor).decoration(TextDecoration.BOLD, true));
        }
    }


    private String textToLegacy(String text, NamedTextColor color) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(Component.text(text).color(color));
    }

    private String textToLegacy(String text, NamedTextColor color, TextDecoration decoration) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(Component.text(text).color(color).decorate(decoration));
    }


    // Corrected method to format the prefix correctly
    private Component formatPrefix(String text) {
        return Component.text("◦ ").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true)
                .append(Component.text(text).color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false));
    }
	private String getPlayerEchos(Player player) {
		double balance = echoBalances.get(player.getUniqueId());
		return ChatM.formatDouble(balance);
	}
	private String getPlayerTokens(Player player) {
		UUID uuid = player.getUniqueId();
		return ChatM.formatDouble(playerBalance.get(uuid)[1]);
	}
	private String getPlayerGemmes(Player player) {
		UUID uuid = player.getUniqueId();
		return ChatM.formatDouble(playerBalance.get(uuid)[2]);
	}
	private String getPlayerXp(Player player) {
		UUID uuid = player.getUniqueId();
		return ChatM.formatDouble(playerBalance.get(uuid)[3]);
	}
	public static String getPlayerRank(Player player) {
		return PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
	}
	private String getPlayerMine(Player player) {
		UUID uuid = player.getUniqueId();
		return playerMine.get(uuid).toUpperCase();
	}
	private String getPlayerPrestige(Player player) {
		return playerPrestige.get(player.getUniqueId()).toString();
	}
	private String getPlayerRebirth(Player player) {
		return playerRebirth.get(player.getUniqueId()).toString();
	}
	public static MainScoreboard getInstance() {
		return INSTANCE_MAIN_SCOREBOARD;
	}
}
