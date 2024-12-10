package me.fliqq.echochat.manager;

import java.util.Arrays;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


public class PlayerTag {
	
	private String name;
	private String rarity;
    private Component tagComponent;
	
	public PlayerTag(String name, String rarity, Component tagComponent) {
		this.name=name;
		this.rarity=rarity;
		this.setTagComponent(tagComponent);

	}


	public String getName() {
		return name;
	}

	public String getRarity() {
		return rarity;
	}
	

	

    public static List<PlayerTag> TAG_LIST = Arrays.asList(
            new PlayerTag("Default", "Default", MiniMessage.miniMessage().deserialize("<color:#454545>[</color><gray>Nouveau</gray><color:#454545>]</color>")),
            new PlayerTag("Voteur", "Commun", MiniMessage.miniMessage().deserialize("<color:#454545>[</color><white>Voteur</white><color:#454545>]</color>")),
            new PlayerTag("Fou", "Rare", MiniMessage.miniMessage().deserialize("<color:#454545>[</color><blue>Fou</blue><color:#454545>]</color>")),
            new PlayerTag("Noob", "Legendaire", MiniMessage.miniMessage().deserialize("<color:#454545>[</color><color:#3dff2b><b>NOOB</b></color><color:#454545>]</color>"))

    );
    public static List<PlayerTag> getTags() {
        return TAG_LIST;
    
    }
    public static PlayerTag getTag(String tagName) {
        for (PlayerTag tag : PlayerTag.getTags()) {
            if (tag.getName().equalsIgnoreCase(tagName)) {
                return tag;
            }
        }
        return null; // 
    }


	public Component getTagComponent() {
		return tagComponent;
	}


	public void setTagComponent(Component tagComponent) {
		this.tagComponent = tagComponent;
	}
    
    

}
