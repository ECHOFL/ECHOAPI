package me.fliqq.echoessential;

import org.bukkit.plugin.java.JavaPlugin;

import me.fliqq.echoessential.command.BlockPosCommand;
import me.fliqq.echoessential.command.ButcherCommand;
import me.fliqq.echoessential.command.FeedCommand;
import me.fliqq.echoessential.command.FlyCommand;
import me.fliqq.echoessential.command.FlySpeed;
import me.fliqq.echoessential.command.GlowCommand;
import me.fliqq.echoessential.command.GmCommand;
import me.fliqq.echoessential.command.GodCommand;
import me.fliqq.echoessential.command.RepairCommand;
import me.fliqq.echoessential.command.SetSpawnCommand;
import me.fliqq.echoessential.command.SpawnCommand;
import me.fliqq.echoessential.command.TimeCommand;
import me.fliqq.echoessential.command.TpCommand;
import me.fliqq.echoessential.command.TpHereCommand;
import me.fliqq.echoessential.config.EssentialSetting;
import me.fliqq.echoessential.listener.ConnectionL;
import me.fliqq.echoessential.listener.HideListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class EchoEssential extends JavaPlugin{
	


    @Override
    public void onEnable() {
    	getLogger().info("\u001B[34m ********  ********  **    **  ********");
    	getLogger().info("\u001B[38;5;69m ********  ********  **    **  ********");
    	getLogger().info("\u001B[38;5;75m **        **        **    **  **    **");
    	getLogger().info("\u001B[38;5;81m *****     **        ********  **    **");
    	getLogger().info("\u001B[38;5;87m *****     **        ********  **    **");
    	getLogger().info("\u001B[38;5;93m **        **        **    **  **    **");
    	getLogger().info("\u001B[38;5;99m ********  ********  **    **  ********");
    	getLogger().info("\u001B[38;5;105m ********  ********  **    **  ********"+"\u001B[0m");

        
        //CONFIG
        saveDefaultConfig();
        reloadConfig();
        EssentialSetting.getInstance().load();

        
        //EVENT//
        getServer().getPluginManager().registerEvents(new ConnectionL(), this);
        getServer().getPluginManager().registerEvents(new HideListener(), this);
        //COMMANDS
        this.getCommand("time").setExecutor(new TimeCommand());
        this.getCommand("time").setTabCompleter(new TimeCommand());
        this.getCommand("fly").setExecutor(new FlyCommand());
        this.getCommand("glow").setExecutor(new GlowCommand());

        this.getCommand("repair").setExecutor(new RepairCommand());
        this.getCommand("gm").setExecutor(new GmCommand());
        this.getCommand("god").setExecutor(new GodCommand());
        this.getCommand("tp").setExecutor(new TpCommand());
        this.getCommand("tphere").setExecutor(new TpHereCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("speed").setExecutor(new FlySpeed());
        this.getCommand("blockpos").setExecutor(new BlockPosCommand());
        this.getCommand("feed").setExecutor(new FeedCommand());
        this.getCommand("butch").setExecutor(new ButcherCommand());
        this.getCommand("butch").setTabCompleter(new ButcherCommand());
    }


    
    public static EchoEssential getInstance(){
        return getPlugin(EchoEssential.class);
    }
}

