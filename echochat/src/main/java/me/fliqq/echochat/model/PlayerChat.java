package me.fliqq.echochat.model;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fliqq.echochat.manager.PlayerTag;
import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echomultieconomy.EchoMultiEconomy;
import me.fliqq.echopickaxe.EchoPickaxe;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echosql.EchoSql;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

public class PlayerChat {
	
	private static final HashMap<UUID, String> mines = EchoSql.getInstance().getPlayerMine();

	private UUID uuid;
	private String rawNickName;
	private Component nickName;
	private int chatColor;
	private boolean chatDecoration;
	private PlayerTag tag;

	
	
	public PlayerChat(UUID uuid, String rawNickName, Component nickName, int chatColor, boolean chatDecoration,
			PlayerTag tag) {
		
		this.uuid = uuid; 
		this.rawNickName = rawNickName;
		this.setNickName(nickName);
		this.setChatColor(chatColor);
		this.setChatDecoration(chatDecoration);
		this.tag = tag;
	}

	public UUID getUuid() {
		return uuid;
	}
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public String getRawNickName() {
		return rawNickName;
	}

	public void setRawNickName(String rawNickName) {
		this.rawNickName = rawNickName;
	}


	public PlayerTag getTag() {
		return this.tag;
	}

	public void setTag(PlayerTag tag) {
		this.tag = tag;
	}
	
	public Component createChatComponent(Component message) {
	    String prefix = PlaceholderAPI.setPlaceholders(getPlayer(), "%luckperms_prefix%");
	    String mine = mines.get(getPlayer().getUniqueId());
	    MiniMessage miniMessage = MiniMessage.miniMessage();

        // [ITEM]
        /*if (message instanceof TextComponent) {
            TextComponent textComponent = (TextComponent) message;

            if (textComponent.content().contains("[item]")) {
                ItemStack mainHandItem = getPlayer().getInventory().getItemInMainHand();
                getPlayer().sendMessage("TESt");
                if (mainHandItem != null && mainHandItem.getType() != Material.AIR) {
                    getPlayer().sendMessage("TESt2");
                    String nameComponent = mainHandItem.getItemMeta().getDisplayName();
                	Component itemHover = miniMessage.deserialize("<hover:show_text:"+ChatM.formatMessage("&9&lPioche de &l" + getPlayer().getName())+">test</hover>");
                	
                	Component replacedMessage = textComponent.replaceFirstText("[item]", itemHover);
                    getPlayer().sendMessage(replacedMessage);

                	message = replacedMessage;
                }
            }
        }*/
	    
	    //ADD MINE
	    Component customMessages = miniMessage.deserialize(
	        "<color:#454545>[</color><green>" + mine.toUpperCase() + "</green><color:#454545>]</color> ");
	    
	    //ADD PRESTIGE IF ONE
	    if (EchoSql.getInstance().getPlayerPrestige().get(getPlayer().getUniqueId()) != 0) {
	        Component textComponent = miniMessage.deserialize("<color:#454545>[</color><red>" + EchoSql.getInstance().getPlayerPrestige().get(getPlayer().getUniqueId()) + "</red><color:#454545>]</color> ");
	        customMessages = customMessages.append(textComponent);
	    }
	    
	    //ADD PREFIX
	    Component prefixComponent = miniMessage.deserialize("<color:#454545>[</color>"+prefix +"<color:#454545>]</color> ");
	    customMessages = customMessages.append(prefixComponent);
	    
	    //ADD NICK NAME
	    //"<gradient:#913d54:#f79459:red><bold>"+getRawNickName()+"</bold></gradient>"
	    customMessages = customMessages.append(getNickName().color(NamedTextColor.RED)).append(Component.text(" "));
	    
	    //ADD TAG    
	    customMessages = customMessages.append(getTag().getTagComponent());

	    // ADD >
	    Component textComponent2 = Component.text("> ").color(NamedTextColor.DARK_GRAY);
	    customMessages = customMessages.append(textComponent2);
	    
	    //ADD HOVER TEXT
	    UUID uuid = getPlayer().getUniqueId();
	    Double[] balancesHashMap = EchoSql.getInstance().getPlayerBalance().get(uuid);

	    Component hoverComponent = miniMessage.deserialize("<red>Informations de</red> <white>"+ getPlayer().getName()+"</white>");
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Grade "+prefix));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Mine <b><gold>"+ mine.toUpperCase()+"</gold></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Prestige <b><gold>"+EchoSql.getInstance().getPlayerPrestige().get(uuid)+"</gold></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Renaissance <b><gold>"+EchoSql.getInstance().getPlayerRebirth().get(uuid)+"</gold></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Blocs minés <b><gray>"+ChatM.formatInt(PPickaxeManager.getInstance().getPick(uuid).getRealBlockMined())+"</gray></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Echos <b><yellow>"+ChatM.formatDouble(EchoMultiEconomy.getInstance().getEchoBalances().get(uuid))+"</yellow></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Tokens <b><light_purple>"+ChatM.formatDouble(balancesHashMap[1])+"</light_purple></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> Gemmes <b><aqua>"+ChatM.formatDouble(balancesHashMap[2])+"</aqua></b>"));	
	    hoverComponent = hoverComponent.appendNewline().append(miniMessage.deserialize("<red><b>◦</b></red> XP <b><green>"+ChatM.formatDouble(balancesHashMap[3]) +"</green></b>"));	
	    
	    customMessages = customMessages.hoverEvent(HoverEvent.showText(hoverComponent));
	    
	    //ADD CHAT COLOR + CHAT DECORATION + MESSAGE
	    Component messageComponent = message.color(TextColor.color(getChatColor())).decoration(TextDecoration.BOLD, getChatDecoration());// COLOR
	    customMessages = customMessages.append(messageComponent);
	    
	    return customMessages;

	}

	public boolean getChatDecoration() {
		return chatDecoration;
	}

	public void setChatDecoration(boolean chatDecoration) {
		this.chatDecoration = chatDecoration;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}


	public Component getNickName() {
		return nickName;
	}

	public void setNickName(Component nickName) {
		this.nickName = nickName;
	}

}
