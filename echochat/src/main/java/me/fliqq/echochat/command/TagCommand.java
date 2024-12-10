package me.fliqq.echochat.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echochat.manager.ChatManager;
import me.fliqq.echochat.manager.PlayerTag;
import me.fliqq.echoessential.manager.ChatM;

public class TagCommand implements CommandExecutor{

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		
		if(!(sender instanceof final Player player))
			return true;
		
		if(args.length==0) {
			return true;
		}
		if(args.length==1) {
			if(player.hasPermission("echo.chat.alltags")) {
				List<PlayerTag> tags = PlayerTag.getTags();
				for(PlayerTag tag : tags) {
					if(tag.getName().equalsIgnoreCase(args[0])) {
						ChatManager.getInstance().getPlayerChat(player.getUniqueId()).setTag(tag);
						player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Nouveau tag :&r"+tag.getName()));

						return true;
					}
				}
				player.sendMessage(ChatM.formatMessage(ChatM.logoEcho()+"Ce tag n'existe pas."));
					
			}else {
				player.sendMessage(ChatM.noPermissionMessage());
			}

			
		}

		
		return true;
	}

}
