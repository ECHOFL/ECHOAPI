package me.fliqq.echopickaxe.manager;

import java.util.Arrays;
import java.util.List;

public class PlayerEnchant {
    private String name;
    private int maxLvl;
    private String description;
    private int price;
    private int num;
	public String color;
    
    public PlayerEnchant(String name, int maxLvl, String description, int price, int num, String color) {
        this.name = name;
        this.maxLvl =maxLvl;
        this.description = description;
        this.price = price;
        this.num = num;
        this.color = color;

    }

    public String getName() {
        return name;
    }
    public int getNum() {
    	return num;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public String getDescription(){
        return description;
    }

    public int getPrice() {
        return price;
    }


    public static final List<PlayerEnchant> ENCHANT_LIST = Arrays.asList(
            new PlayerEnchant("Efficiency", 30, "no desc",50,0,"&a"),
            new PlayerEnchant("Fortune", 25000, "no desc",50,1,"&b"),
            new PlayerEnchant("Explosion", 5000, "no desc",200,2, "&c"),
            new PlayerEnchant("Laser", 5000, "no desc",300,3,"&d"),
            new PlayerEnchant("VisionNocturne", 1, "no desc",10000,4,"&e"),
            new PlayerEnchant("Speed", 6, "no desc",10000,5,"&f"),
            new PlayerEnchant("TokenExtracteur", 10000, "no desc",300,6,"&2"),
            new PlayerEnchant("Haste", 10, "no desc",10000,7,"&3"),
            new PlayerEnchant("MarteauPiqueur", 10000, "no desc",500,8,"&6"),
            new PlayerEnchant("Atomique", 10000, "no desc",400,9,"&9")

    );
    public static List<PlayerEnchant> getEnchants() {
        return ENCHANT_LIST;
    
    }
    public static PlayerEnchant getEnchant(String enchantName) {
        for (PlayerEnchant enchant : PlayerEnchant.getEnchants()) {
            if (enchant.getName().equalsIgnoreCase(enchantName)) {
                return enchant;
            }
        }
        return null; // 
    }

	public String getColor() {
		return this.color;
	}
}