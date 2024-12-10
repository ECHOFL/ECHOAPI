package me.fliqq.echochat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echochat.command.ChatColorCommand;
import me.fliqq.echochat.command.NickNameCommand;
import me.fliqq.echochat.command.TagCommand;
import me.fliqq.echochat.listener.ChatListeners;
import me.fliqq.echochat.listener.ConnectionListener;
import me.fliqq.echochat.listener.NickNameChatListener;
import me.fliqq.echochat.manager.ChatManager;
import me.fliqq.echochat.manager.PlayerTag;
import me.fliqq.echochat.model.PlayerChat;
import me.fliqq.echochat.util.ComponentUtils;
import me.fliqq.echopickaxe.EchoPickaxe;
import me.fliqq.echosql.DatabaseManager;
import net.kyori.adventure.text.Component;



public class EchoChat extends JavaPlugin{

	ChatManager cM = ChatManager.getInstance();


	@Override
	public void onEnable() {
	    // Initialize an audiences instance for the plugin
        getServer().getPluginManager().registerEvents(new ChatListeners(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        getCommand("tag").setExecutor(new TagCommand());
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        
        NickNameCommand nickNameCommand = new NickNameCommand();

        // Register command
        this.getCommand("nick").setExecutor(nickNameCommand);

        // Register event listener
        getServer().getPluginManager().registerEvents(new NickNameChatListener(nickNameCommand), this);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(EchoChat.this::updateChatInDb);
            }
        }.runTaskTimer(this, 0L, 8000L); // 12000 ticks = 10 minutes
    }

	

	@Override
	public void onDisable() {
        getServer().getOnlinePlayers().forEach(this::updateChatInDb);
    
	}
	
	

	public static EchoChat getInstance() {
		return getPlugin(EchoChat.class);
	}
	
    public void updateChatInDb(Player player) {
        final UUID uuid = player.getUniqueId();
        PlayerChat playerChat = cM.getPlayerChat(uuid);
        try {
            Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_chat SET rawNickName = ?, nickNameComponent = ?, chatColor = ?, chatDecoration = ?, tag = ? WHERE uuid = ?");
            
            preparedStatement.setString(1, playerChat.getRawNickName());
            preparedStatement.setString(2, ComponentUtils.componentToString(playerChat.getNickName()));
            preparedStatement.setInt(3, playerChat.getChatColor());
            preparedStatement.setBoolean(4, playerChat.getChatDecoration());
            
            //ATTENTION
            preparedStatement.setString(5, playerChat.getTag().getName());  
            preparedStatement.setString(6, uuid.toString());

            preparedStatement.executeUpdate();
            connection.close();
            player.sendMessage("Chat data updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            //player.sendMessage("Failed to update chat data.");
        }
    }

    public void getChatFromDb(Player player) {
        final UUID uuid = player.getUniqueId();
        
        try {
            final Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement4 = connection.prepareStatement("SELECT uuid, rawNickName, nickNameComponent, chatColor, chatDecoration, tag FROM player_chat WHERE uuid = ?");
            
            preparedStatement4.setString(1, uuid.toString());
            ResultSet resultSet4 = preparedStatement4.executeQuery();
            
            if (resultSet4.next()) {
               // player.sendMessage("Database connection successful.");
               
                String rawNickName = resultSet4.getString("rawNickName");
                Component nickName = ComponentUtils.stringToComponent(resultSet4.getString("nickNameComponent")); //COMP
                
                int chatColor = resultSet4.getInt("chatColor"); //INT
                
                boolean chatDecoration = resultSet4.getBoolean("chatDecoration");
                String tag = resultSet4.getString("tag");
                
                PlayerTag tagObj = PlayerTag.getTag(tag);

                PlayerChat playerChat = new PlayerChat(uuid, rawNickName, nickName, chatColor, chatDecoration, tagObj);
                cM.addPlayerChat(uuid, playerChat);
                
                player.sendMessage("Chat data fetched successfully.");
                
            } else {
                createUserChat(connection, uuid, player);
            }
            
            resultSet4.close();
            preparedStatement4.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Failed to fetch chat data.");
        }
    }

    //1 SEUL FOIS SI PAS DB
    private void createUserChat(Connection connection, UUID uuid, Player player) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_chat VALUES (?,?,?,?,?,?)");
        
        Component nickNameComponent = Component.text(Bukkit.getPlayer(uuid).getName() + " TEST");
        
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, Bukkit.getPlayer(uuid).getName());
        preparedStatement.setString(3, ComponentUtils.componentToString(nickNameComponent));
        preparedStatement.setInt(4, 0x443344);
        preparedStatement.setBoolean(5, false);
        preparedStatement.setString(6, "Default"); //PlayerTag to String
        
        

        PlayerChat playerChat = new PlayerChat(uuid, Bukkit.getPlayer(uuid).getName(),nickNameComponent, 0x443344 , false , PlayerTag.getTag("Default"));
        cM.addPlayerChat(uuid, playerChat);
        
        preparedStatement.executeUpdate();
        preparedStatement.close();
        
        //player.sendMessage("Chat data created for new player.");
    }
 
}
