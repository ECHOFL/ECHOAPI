package me.fliqq.echomenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;

import me.fliqq.echomenu.commands.MenuCommand;
import me.fliqq.echomenu.listener.PickMenuListener;
import me.fliqq.echomenu.menu.Menus;


public class EchoMenu extends SimplePlugin{

	
	
	@Override
	protected void onPluginStart() {
        Menus menusInstance = new Menus();

		registerCommand(new MenuCommand());

        getServer().getPluginManager().registerEvents(new PickMenuListener(menusInstance), this);

		ButtonReturnBack.setItemStack(ItemCreator.of(CompMaterial.ENDER_EYE, "&e&lRetour", "", "&e<-----").make());
		
	}

	public static EchoMenu getInstance() {
		return (EchoMenu) SimplePlugin.getInstance();
	}
	
}
