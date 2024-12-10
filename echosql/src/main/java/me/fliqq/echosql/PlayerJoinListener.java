package me.fliqq.echosql;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

public class PlayerJoinListener implements Listener{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        try {
            final Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT echo, token, gemme, xp FROM player_economy WHERE uuid = ?");
            final PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT uuid, rank, mine, prestige, rebirth FROM player_info WHERE uuid = ?");

            preparedStatement2.setString(1, uuid.toString());
            preparedStatement2.executeQuery();
            
            final ResultSet resultSet2 = preparedStatement2.getResultSet();
            if (resultSet2.next()) {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Lien avec la base de données (economie) effectué avec succès.");
               
                Double[] balanceArray = new Double[4];//nombre devises
                balanceArray[0]= resultSet2.getDouble("echo");
                balanceArray[1]= resultSet2.getDouble("token");
                balanceArray[2]= resultSet2.getDouble("gemme");
                balanceArray[3]= resultSet2.getDouble("xp");

                EchoSql.getInstance().getPlayerBalance().put(uuid, balanceArray);
               

            } else {
                createUserEconomy(connection, uuid, event.getPlayer());
                event.getPlayer().sendMessage(ChatColor.RED + "Aucune donnée trouvée pour ce joueur.(economy)");
            }
            
            resultSet2.close();
            preparedStatement2.close();
            
            /////////////////
            
            preparedStatement3.setString(1, uuid.toString());
            preparedStatement3.executeQuery();
            
            final ResultSet resultSet3 = preparedStatement3.getResultSet();
            if (resultSet3.next()) {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Lien avec la base de données (informations) effectué avec succès.");
               
                String rank = resultSet3.getString("rank");
                String mine = resultSet3.getString("mine");
                int prestige = resultSet3.getInt("prestige");
                int rebirth = resultSet3.getInt("rebirth");
                
                EchoSql.getInstance().getPlayerRank().put(uuid, rank);
                EchoSql.getInstance().getPlayerMine().put(uuid, mine);
                EchoSql.getInstance().getPlayerPrestige().put(uuid, prestige);
                EchoSql.getInstance().getPlayerRebirth().put(uuid, rebirth);

            } else {
                createUserInfo(connection, uuid, event.getPlayer());
                event.getPlayer().sendMessage(ChatColor.RED + "Aucune donnée trouvée pour ce joueur.(informations)");
            }
            resultSet3.close();
            preparedStatement3.close();
            
            connection.close();

            
        } catch (SQLException e) {
            e.printStackTrace();
            event.getPlayer().sendMessage(ChatColor.RED + "Erreur de connexion à la base de données.");
        }
    }
    
        private void createUserEconomy(Connection connection, UUID uuid, Player player) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_economy VALUES (?,?,?,?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setDouble(2, 0);
            preparedStatement.setDouble(3, 0);
            preparedStatement.setDouble(4, 0);
            preparedStatement.setDouble(5, 0);

            preparedStatement.executeUpdate();

            // Initialiser les soldes pour le joueur dans la hashmap echoBalances
            Double[] balanceArray = new Double[4];
            Arrays.fill(balanceArray, 0.0);
            EchoSql.getInstance().getPlayerBalance().put(uuid, balanceArray);
        }
        
        private void createUserInfo(Connection connection, UUID uuid, Player player) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_info VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, "default");
            preparedStatement.setString(3, "a");
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            
            EchoSql.getInstance().getPlayerRank().put(uuid, "default");
            EchoSql.getInstance().getPlayerMine().put(uuid, "a");
            EchoSql.getInstance().getPlayerPrestige().put(uuid, 0);
            EchoSql.getInstance().getPlayerRebirth().put(uuid, 0);
            
            preparedStatement.executeUpdate();
        }    

    }
