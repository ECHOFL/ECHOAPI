package me.fliqq.echomines;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.fliqq.echomines.command.MineCommand;
import me.fliqq.echomines.command.MineReloadCommand;
import me.fliqq.echomines.command.RegionCommand;
import me.fliqq.echomines.listener.MineListener;
import me.fliqq.echomines.manager.Regions;
import me.fliqq.echomines.scheduler.MineScheduler;
public class EchoMines extends JavaPlugin {

    private static EchoMines instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        MineScheduler.getInstance().startScheduler();

        // Chargement des régions (assurez-vous que Regions.getInstance().load() est optimisé)
        Regions.getInstance().load();

        // Enregistrement des événements
        Bukkit.getServer().getPluginManager().registerEvents(new MineListener(), this);

        // Enregistrement des commandes
        getCommand("region").setExecutor(new RegionCommand());
        getCommand("mines").setExecutor(new MineCommand());
        getCommand("rgreload").setExecutor(new MineReloadCommand());
    }

    @Override
    public void onDisable() {



        Regions.getInstance().save();
    }

    public static EchoMines getInstance() {
        return instance;
    }
}