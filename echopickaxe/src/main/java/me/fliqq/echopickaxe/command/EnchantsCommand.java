package me.fliqq.echopickaxe.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.fliqq.echoessential.manager.ChatM;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echopickaxe.objects.PPickaxes;

public class EnchantsCommand implements CommandExecutor, TabCompleter {
	
	private final PPickaxeManager pM = PPickaxeManager.getInstance();
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent exécuter cette commande !");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("echo.pickaxe.enchant")) {
            player.sendMessage(ChatM.noPermissionMessage());
            return true;
        }

        if (args.length != 3) {
            player.sendMessage("Utilisation : /enchant <joueur> <nomEnchantement> <niveau>");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
        	 player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Le joueur n'est pas en ligne ou n'existe pas."));
            return true;
        }

        String enchantName = args[1];
        int enchantLevel;
        
        if (!isValidEnchant(enchantName)) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"l'enchant spécifiée n'existe pas."));
            return true;
        }
        
        PlayerEnchant enchant = PlayerEnchant.getEnchant(enchantName);

        try {
            enchantLevel = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Le niveau d'enchantement spécifié n'est pas valide"));
            return true;
        }

        if (enchantLevel > enchant.getMaxLvl()) {
            player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Le niveau d'enchantement spécifié est supérieur au niveau maximal.&a("+enchant.getMaxLvl()+")"));
            return true;
        }
       
        final UUID uuid = targetPlayer.getUniqueId();      
        
        PPickaxes pPickaxe = pM.getPick(uuid);
        pPickaxe.setEnchantLevel(enchant, enchantLevel); //SET ENCH
        
        //UPDATE INV
        
        targetPlayer.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Votre niveau de &a"+enchant.getName()+"&7 est maintenant de &a"+enchantLevel));
        player.sendMessage(ChatM.formatMessage(ChatM.logoEnchant()+"Le niveau d'enchant "+enchant.getName()+" &7pour le joueur &a" +targetPlayer.getName()+" &7est maintenant de &a"+enchantLevel));
        
        if(enchant.getName().equalsIgnoreCase("Efficiency")) {
        	pPickaxe.replaceItemOrEff();
        }
        
        return true;
    }
    
    
    
    private boolean isValidEnchant(String enchantName) {
        for (PlayerEnchant enchant : PlayerEnchant.getEnchants()) {
            if (enchant.getName().equalsIgnoreCase(enchantName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            List<String> enchantments = new ArrayList<>();
            for (PlayerEnchant enchant : PlayerEnchant.getEnchants()) {
                enchantments.add(enchant.getName());
            }
            return enchantments;
        }
        return null;
    }

}
