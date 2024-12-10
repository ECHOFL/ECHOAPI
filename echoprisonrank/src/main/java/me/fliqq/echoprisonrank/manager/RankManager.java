package me.fliqq.echoprisonrank.manager;

import org.bukkit.entity.Player;

import me.fliqq.echoprisonrank.enom.MineRankEnum;
import me.fliqq.echoprisonrank.enom.RankEnum;
import me.fliqq.echosql.EchoSql;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class RankManager {

    public static String getPlayerGroup(Player player) {
        Collection<String> possibleGroups = Arrays.stream(RankEnum.values())
                                                  .map(RankEnum::getLowerCaseName)
                                                  .collect(Collectors.toList());
        return getPlayerGroup(player, possibleGroups);
    }

    public static String getPlayerGroup(Player player, Collection<String> possibleGroups) {
        for (String group : possibleGroups) {
            if (player.hasPermission("group." + group)) {
                return group;
            }
        }
        return null;
    }
    
    
    public static Collection<String> getAllMines(){
        Collection<String> possibleMines = Arrays.stream(MineRankEnum.values())
                .map(MineRankEnum::getLowerCaseName)
                .collect(Collectors.toList());
    	return possibleMines;
    }
    
    
    public static String getPlayerMineRank(Player player) {
        Collection<String> possibleMines = Arrays.stream(MineRankEnum.values())
                                                  .map(MineRankEnum::getLowerCaseName)
                                                  .collect(Collectors.toList());
        return getPlayerMineRank(player, possibleMines);
    }
    public static String getPlayerMineRank(Player player, Collection<String> possibleMines) {
    	final String playerMines = EchoSql.getInstance().getPlayerMine().get(player.getUniqueId());
    	for(String mine : possibleMines) {
    		if(mine.equalsIgnoreCase(playerMines))
    			return mine;
    	}
    	return null;
    }
}