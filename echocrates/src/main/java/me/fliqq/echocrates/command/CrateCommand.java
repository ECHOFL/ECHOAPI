package me.fliqq.echocrates.command;


import org.mineacademy.fo.command.SimpleCommand;


import me.fliqq.echocrates.manager.CrateManager;

import me.fliqq.echocrates.models.Crates;


public class CrateCommand extends SimpleCommand{

	public CrateCommand() {
		super("crate");
	}

	@Override
	protected void onCommand() {
		this.checkConsole();
		
        Crates crate = Crates.CRATE1;
        Crates crate2 = Crates.CRATE2;
        getPlayer().getInventory().addItem(crate.getCrateItem());
        getPlayer().getInventory().addItem(crate2.getCrateItem());

	}
}
