package me.fliqq.echomultieconomy.manager;

import java.util.HashMap;
import java.util.UUID;

import me.fliqq.echosql.EchoSql;

public class EconomyManager {
	static HashMap<UUID, Double[]> playerBalances= EchoSql.getInstance().getPlayerBalance();
	
	public static void withdrawEco(UUID uuid, Double amount, int devise) {
		playerBalances.get(uuid)[devise]=playerBalances.get(uuid)[devise]-amount;
		
	}
}
