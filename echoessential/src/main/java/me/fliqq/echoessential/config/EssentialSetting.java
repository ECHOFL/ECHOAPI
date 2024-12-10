package me.fliqq.echoessential.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import me.fliqq.echoessential.EchoEssential;

import java.io.File;

public class EssentialSetting {

    private final static EssentialSetting instance = new EssentialSetting(); // SINGLETON

    private File file;
    private YamlConfiguration config;

    private Location spawnLocation;

    private EssentialSetting(){

    }

    public void load() {
        file = new File(EchoEssential.getInstance().getDataFolder(), "data.yml");
        if (!file.exists())
            EchoEssential.getInstance().saveResource("data.yml", false);
        config = new YamlConfiguration();
        config.options().parseComments(true);
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spawnLocation = (Location) config.get("spawn");

    }

    public void save(){
        try{
            config.save(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void set(String path, Object value){
        config.set(path, value);

        save();
    }

    public Location getSpawnLocation(){
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation){
        this.spawnLocation=spawnLocation;

        set("spawn", spawnLocation);
    }
    public static EssentialSetting getInstance() {
        return instance;
    }
}
