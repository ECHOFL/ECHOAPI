package me.fliqq.echopickaxe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.fliqq.echopickaxe.command.BpCommand;
import me.fliqq.echopickaxe.command.EnchantsCommand;
import me.fliqq.echopickaxe.command.SellCommand;
import me.fliqq.echopickaxe.listener.BpListener;
import me.fliqq.echopickaxe.listener.FetchingListener;
import me.fliqq.echopickaxe.listener.PickaxeListener;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echopickaxe.objects.BackPacks;
import me.fliqq.echopickaxe.objects.PPickaxes;
import me.fliqq.echosql.DatabaseManager;

public final class EchoPickaxe extends JavaPlugin {

	private PPickaxeManager pM;
	private BackPacksManager bM;
	
    @Override
    public void onEnable() {

        pM = PPickaxeManager.getInstance();
        bM = BackPacksManager.getInstance();
    	
       // getServer().getPluginManager().registerEvents(new PickaxeListener(), this);
        getServer().getPluginManager().registerEvents(new FetchingListener(), this);
        getServer().getPluginManager().registerEvents(new BpListener(), this);
        getServer().getPluginManager().registerEvents(new PickaxeListener(), this);

        
        //COMMANDS
        this.getCommand("enchants").setExecutor(new EnchantsCommand());
        this.getCommand("bp").setExecutor(new BpCommand());
        this.getCommand("sell").setExecutor(new SellCommand());
        
        
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(EchoPickaxe.this::updatePickInfoInDb);
            }
        }.runTaskTimer(this, 0L, 6000L); // 12000 ticks = 10 minutes
    }

    
    
    @Override
    public void onDisable() {
        // Loop through all online players and update their enchant levels
        Bukkit.getServer().getOnlinePlayers().forEach(this::updatePickInfoInDb);
    }

    public static EchoPickaxe getInstance(){
        return getPlugin(EchoPickaxe.class);
    }
	
    
    
    public void updatePickInfoInDb(Player player) {
        final UUID uuid = player.getUniqueId();
        PPickaxes pPickaxe = pM.getPick(uuid);
        BackPacks backpack = bM.getBackPack(uuid);
        try {
            Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_pick SET totalBl = ?, realBl = ?, pickXp = ?, pickLvl = ?, "
            		+ "pickSlot = ?, bpLvl = ? , bpSlot = ?, eff = ?, fort = ?, exp = ?,"
            		+ " las = ?, noc = ?, spe = ?, tok = ?, has = ?, jac = ?, nuc = ? WHERE uuid = ?");
            
            //INFOS
            preparedStatement.setInt(1, pPickaxe.getTotalBlockMined());
            preparedStatement.setInt(2, pPickaxe.getRealBlockMined());
            preparedStatement.setInt(3, pPickaxe.getPickXp());
            preparedStatement.setInt(4, pPickaxe.getPickLevel());
            preparedStatement.setInt(5, pPickaxe.getPickSlot());
            
            //BP
            preparedStatement.setInt(6, backpack.getLvl());
            preparedStatement.setInt(7, backpack.getBpSlot());
                        
            //ENCH
            preparedStatement.setInt(8, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Efficiency")));   
            preparedStatement.setInt(9, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Fortune")));  
            preparedStatement.setInt(10, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Explosion")));  
            preparedStatement.setInt(11, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Laser")));   
            preparedStatement.setInt(12, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("VisionNocturne")));  
            preparedStatement.setInt(13, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Speed")));  
            preparedStatement.setInt(14, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("TokenExtracteur")));  
            preparedStatement.setInt(15, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Haste")));  
            preparedStatement.setInt(16, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("MarteauPiqueur")));  
            preparedStatement.setInt(17, pPickaxe.getEnchantLevel(PlayerEnchant.getEnchant("Atomique")));  

            //
            preparedStatement.setString(18, uuid.toString());

            preparedStatement.executeUpdate();
            connection.close();
            player.sendMessage("Pick data updated successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Failed to update Pick data.");
        }
    }

    public void getPickInfoFromDb(Player player) {
        final UUID uuid = player.getUniqueId();

        try {
            final Connection connection = DatabaseManager.ECHO_PRISON.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT uuid, totalBl, realBl, pickXp, pickLvl, pickSlot, bpLvl, bpSlot, eff, fort, exp, las, noc, spe, tok, has, jac, nuc FROM player_pick WHERE uuid = ?"
            );
            
            // Set UUID parameter without quotes
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                player.sendMessage("Database connection successful.");
                
                int totalBl = resultSet.getInt("totalBl");
                int realBl = resultSet.getInt("realBl");
                int pickXp = resultSet.getInt("pickXp");
                int pickLvl = resultSet.getInt("pickLvl");
                int pickSlot = resultSet.getInt("pickSlot");
                
                int bpLvl = resultSet.getInt("bpLvl");
                int bpSlot = resultSet.getInt("bpSlot");
                
                int efficiency = resultSet.getInt("eff");
                int fortune = resultSet.getInt("fort");
                int explosion = resultSet.getInt("exp");
                int laser = resultSet.getInt("las");
                int visionNocturne = resultSet.getInt("noc");
                int speed = resultSet.getInt("spe");
                int tokenextracteur = resultSet.getInt("tok");
                int haste = resultSet.getInt("has");
                int marteaupiqueur = resultSet.getInt("jac");
                int atomique = resultSet.getInt("nuc");

                HashMap<PlayerEnchant, Integer> playerEnchantsMap = new HashMap<>();
                playerEnchantsMap.put(PlayerEnchant.getEnchant("efficiency"), efficiency);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("fortune"), fortune);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("explosion"), explosion);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("laser"), laser);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("visionNocturne"), visionNocturne);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("speed"), speed);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("tokenextracteur"), tokenextracteur);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("haste"), haste);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("marteaupiqueur"), marteaupiqueur);
                playerEnchantsMap.put(PlayerEnchant.getEnchant("atomique"), atomique);

                // Création de l'objet PPickaxe
                PPickaxes pPickaxe = new PPickaxes(uuid, playerEnchantsMap, totalBl, realBl, pickXp, pickLvl, pickSlot);
                PPickaxeManager.getInstance().addPick(uuid, pPickaxe);
                pPickaxe.replaceItemOrEff();;

                // Création de l'objet BackPack
                BackPacks backpack = new BackPacks(uuid, bpLvl, bpSlot);
                BackPacksManager.getInstance().addBackPack(uuid, backpack);
                backpack.replaceBpInInv(); // Mettre à jour l'inventaire du joueur avec le sac à dos
                
                // ????? playerEnchantsMap.clear();
                player.sendMessage("Pick data fetched successfully.");
                
            } else {
                createUserPickInfo(connection, uuid, player);
            }
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Failed to fetch pick data.");
        }
    }


	//1 SEUL FOIS SI PAS DB
    private void createUserPickInfo(Connection connection, UUID uuid, Player player) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO player_pick (uuid, totalBl, realBl, pickXp, pickLvl, pickSlot, bpLvl, bpSlot, eff, fort, exp, las, noc, spe, tok, has, jac, nuc) " +
            "VALUES (?, 0, 0, 0, 1, 0, 1, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)"
        );

        // Set UUID parameter without quotes
        preparedStatement.setString(1, uuid.toString());

        // Ajouter l'objet pickaxe à la gestion des pickaxes
        HashMap<PlayerEnchant, Integer> playerEnchantsMap = new HashMap<>();
        PPickaxes pPickaxe = new PPickaxes(uuid, playerEnchantsMap, 0, 0, 0, 1, 0);
        PPickaxeManager.getInstance().addPick(uuid, pPickaxe);
        pPickaxe.replaceItemOrEff();

        // Ajouter l'objet backpack à la gestion des backpacks
        BackPacks backpack = new BackPacks(uuid, 1, 8);
        BackPacksManager.getInstance().addBackPack(uuid, backpack);
        backpack.replaceBpInInv();  // Mettre à jour l'inventaire du joueur avec le sac à dos

        // Exécuter l'insertion
        preparedStatement.executeUpdate();  // Use executeUpdate() for INSERT, UPDATE, DELETE queries
        preparedStatement.close();

        player.sendMessage("Pick data created for new player.");
    }
}
