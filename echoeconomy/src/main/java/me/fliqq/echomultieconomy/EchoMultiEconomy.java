package me.fliqq.echomultieconomy;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echomultieconomy.command.CheckCommand;
import me.fliqq.echomultieconomy.command.EconomyCommand;
import me.fliqq.echomultieconomy.hook.EchoEconomy;
import me.fliqq.echomultieconomy.listener.CheckListener;
import me.fliqq.echomultieconomy.listener.ConnectionListener;
import me.fliqq.echosql.DatabaseManager;
import me.fliqq.echosql.EchoSql;

public class EchoMultiEconomy extends JavaPlugin {
    private static EchoMultiEconomy instance;
    private HashMap<UUID, Double> echoBalances;
    @Override
    public void onEnable() {
        instance = this;
        
        getCommand("economy").setExecutor(new EconomyCommand());
        getCommand("check").setExecutor(new CheckCommand());

        
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new CheckListener(), this);

        
        
        echoBalances=new HashMap<>();
		if (getServer().getPluginManager().getPlugin("Vault") != null)
			EchoEconomy.register();
		
		
		
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(EchoMultiEconomy.this::updatePlayerEcoInDn);
            }
        }.runTaskTimer(this, 0L, 5000L); // 12000 ticks = 10 minutes
    }
    
    @Override
    public void onDisable() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::updatePlayerEcoInDn);

    }
    
    
    
    
    
    
    
    
    public static EchoMultiEconomy getInstance() {
        return instance;
    }
    public HashMap<UUID, Double> getEchoBalances() {
    	return echoBalances;
    }
    public void setEchoBalances(Player player, Double amount) {
    	this.echoBalances.put(player.getUniqueId(), amount);
    }



	public void updatePlayerEcoInDn(Player player) {
        UUID playerUniqueId = player.getUniqueId();
        final Connection connection;
        
        try {
            Double[] balances = EchoSql.getInstance().getPlayerBalance().get(playerUniqueId);

            connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();

            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_economy SET echo = ?, token = ?, gemme = ?, xp = ? WHERE uuid = ?");
            
            preparedStatement.setDouble(1, EchoMultiEconomy.getInstance().getEchoBalances().get(playerUniqueId)); 
            preparedStatement.setDouble(2, balances[1]);  
            preparedStatement.setDouble(3, balances[2]); 
            preparedStatement.setDouble(4, balances[3]); 
            preparedStatement.setString(5, playerUniqueId.toString()); // UUID

            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

}