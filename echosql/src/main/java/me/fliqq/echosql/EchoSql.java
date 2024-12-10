package me.fliqq.echosql;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class EchoSql extends JavaPlugin {
	
    private HashMap<UUID, Double[]> playerBalance;
    private HashMap<UUID, String> playerRank;
    private HashMap<UUID, String> playerMine;
    private HashMap<UUID, Integer> playerPrestige;
    private HashMap<UUID, Integer> playerRebirth;
    
    private HashMap<UUID, Integer> playerBpLvl;
    
    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

        DatabaseManager.initAllDatabaseConnections();

        playerBalance = new HashMap<>();
        playerRank = new HashMap<>();
        playerMine = new HashMap<>();
        playerPrestige = new HashMap<>();
        playerRebirth = new HashMap<>();
        playerBpLvl = new HashMap<>();
        
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }
    
    public static EchoSql getInstance(){
        return getPlugin(EchoSql.class);
    }

    public HashMap<UUID, Double[]> getPlayerBalance() {
        return playerBalance;
    }
    public HashMap<UUID, String> getPlayerRank(){
    	return playerRank;
    }
    public HashMap<UUID, String> getPlayerMine(){
    	return playerMine;
    }
    public HashMap<UUID, Integer> getPlayerPrestige(){
    	return playerPrestige;
    }
    public HashMap<UUID, Integer> getPlayerRebirth(){
    	return playerRebirth;
    }

	public HashMap<UUID, Integer>  getPlayerBpLvl() {
		return playerBpLvl;
		
	}
}
