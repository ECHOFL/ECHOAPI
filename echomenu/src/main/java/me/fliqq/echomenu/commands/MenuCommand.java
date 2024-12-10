package me.fliqq.echomenu.commands;

import org.mineacademy.fo.command.SimpleCommand;

import me.fliqq.echomenu.menu.Menus;

public class MenuCommand extends SimpleCommand{

	public MenuCommand() {
		super("menu");
	}

	@Override
	protected void onCommand() {
		this.checkConsole();
		new Menus().displayTo(this.getPlayer());
	}
}
