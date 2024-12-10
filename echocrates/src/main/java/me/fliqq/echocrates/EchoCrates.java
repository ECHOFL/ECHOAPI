package me.fliqq.echocrates;

import java.util.logging.Logger;

import org.mineacademy.fo.plugin.SimplePlugin;

import me.fliqq.echocrates.command.CrateCommand;
import me.fliqq.echocrates.listener.CrateInteraction;
import me.fliqq.echocrates.listener.PreventRenameCrate;
import me.fliqq.echocrates.listener.RankCouponListener;
import me.fliqq.echocrates.manager.LootManager;

public class EchoCrates extends SimplePlugin{
	
    public static Logger logger;

    
	@Override
	protected void onPluginStart() {
		logger=this.getLogger();

        LootManager lootManager = LootManager.getInstance();
		lootManager.load();
		
		registerCommand(new CrateCommand());
        getServer().getPluginManager().registerEvents(new CrateInteraction(), this);
        getServer().getPluginManager().registerEvents(new PreventRenameCrate(), this);
        getServer().getPluginManager().registerEvents(new RankCouponListener(), this);

	}

	@Override
	protected void onPluginStop() {
		LootManager.getInstance().save();
	}
	
	public static EchoCrates getInstance() {
		return (EchoCrates) SimplePlugin.getInstance();
	}
}
