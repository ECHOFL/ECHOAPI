package me.fliqq.echoprisonrank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echoprisonrank.command.RankCommand;
import me.fliqq.echoprisonrank.listener.ConnectionListener;
import me.fliqq.echoprisonrank.manager.RankManager;
import me.fliqq.echosql.DatabaseManager;
import me.fliqq.echosql.EchoSql;


public class EchoPrisonRank extends JavaPlugin{

	
	
    @Override
    public void onEnable() {
        // Register events and commands
        Bukkit.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        this.getCommand("rank").setExecutor(new RankCommand());

        // Schedule the task to run every 10 minutes (12000 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(EchoPrisonRank.this::saveRankDb);
            }
        }.runTaskTimer(this, 0L, 7000L); // 12000 ticks = 10 minutes
    }

    @Override
    public void onDisable() {
        // Save ranks for all online players when the plugin is disabled
        Bukkit.getServer().getOnlinePlayers().forEach(this::saveRankDb);
    }
	
	public void saveRankDb(Player player) {
        final Connection connection;
        final UUID uuid = player.getUniqueId();
        String playerRank = RankManager.getPlayerGroup(player);
        try {
            //RANK
            String mine = EchoSql.getInstance().getPlayerMine().get(uuid);
            Integer prestige = EchoSql.getInstance().getPlayerPrestige().get(uuid);
            Integer rebirth = EchoSql.getInstance().getPlayerRebirth().get(uuid);
            
            connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_info SET rank = ?, mine = ?, prestige = ?, rebirth = ? WHERE uuid = ?");
        
            preparedStatement.setString(1, playerRank); 
            preparedStatement.setString(2, mine); 
            preparedStatement.setInt(3, prestige);
            preparedStatement.setInt(4, rebirth); 
            preparedStatement.setString(5, uuid.toString()); // UUID

            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        EchoSql.getInstance().getPlayerRank().remove(uuid);
        EchoSql.getInstance().getPlayerMine().remove(uuid);
        EchoSql.getInstance().getPlayerPrestige().remove(uuid);
        EchoSql.getInstance().getPlayerRebirth().remove(uuid);
	
	}
	
    public static EchoPrisonRank getInstance(){
        return getPlugin(EchoPrisonRank.class);
    }


}
