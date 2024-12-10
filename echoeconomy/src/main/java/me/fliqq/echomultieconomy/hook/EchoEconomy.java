package me.fliqq.echomultieconomy.hook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.EchoMultiEconomy;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EchoEconomy extends AbstractEconomy {
	

	private HashMap<UUID, Double> echoBalances= EchoMultiEconomy.getInstance().getEchoBalances();

	private EchoEconomy() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "EchoEchonomy";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return 0;
	}

	@Override
	public String format(double amount) {
		return ChatM.formatDouble(amount)+ ChatColor.RESET+ ""+ChatColor.BOLD+" 💰";
	}

	@Override
	public String currencyNamePlural() {
		return "Echos";
	}

	@Override
	public String currencyNameSingular() {
		return "Echo";
	}

	@Override
	public boolean hasAccount(String playerName) {
		return this.hasAccountByName(playerName);
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return this.hasAccountByName(playerName);
	}

	@Override
	public double getBalance(String playerName) {
		return this.getByName(playerName);
	}

	@Override
	public double getBalance(String playerName, String world) {
		return this.getByName(playerName);
	}

	@Override
	public boolean has(String playerName, double amount) {
		return this.hasByName(playerName, amount);
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return this.hasByName(playerName, amount);
	}

	private boolean hasAccountByName(String playerName) {
		System.out.println("hasAccount() detected! Map: " + this.echoBalances);

		return this.echoBalances.containsKey(Bukkit.getPlayer(playerName).getUniqueId());
	}

	private double getByName(String playerName) {
		return this.echoBalances.getOrDefault(Bukkit.getPlayer(playerName).getUniqueId(), (double) 0);
	}

	private boolean hasByName(String playerName, double amount) {
		System.out.println("has() detected! Map: " + this.echoBalances);

		return this.echoBalances.getOrDefault(Bukkit.getPlayer(playerName).getUniqueId(), (double) 0) >= amount;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		return this.withdrawPlayer(playerName, null, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		if (amount < 0)
			return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");

		if (!has(playerName, amount)) {
			return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
		}

		this.echoBalances.put(Bukkit.getPlayer(playerName).getUniqueId(), (double) (this.getByName(playerName) - amount));

		return new EconomyResponse(amount, this.getByName(playerName), EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		return this.depositPlayer(playerName, null, amount);
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		if (amount < 0)
			return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");

		this.echoBalances.put(Bukkit.getPlayer(playerName).getUniqueId(), (double) (this.getByName(playerName) + amount));

		return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		return new EconomyResponse(0, this.getBalance(player), EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<>();
	}

	@Override
	public boolean createPlayerAccount(String playerName) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return false;
	}

	public static void register() {
		Bukkit.getServicesManager().register(Economy.class, new EchoEconomy(), EchoMultiEconomy.getInstance(), ServicePriority.High);
	}
}