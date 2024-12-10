package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import me.fliqq.echoessential.manager.ChatM;

public class RepairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length!=0)
            return false;
        if(!(sender instanceof Player))
            return false;
        Player p = (Player) sender;
        if(p.hasPermission("echo.essential.repair")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            if(item != null && item.getType().isItem()){
                ItemMeta meta = item.getItemMeta();
                if(meta instanceof Damageable){
                    Damageable damageable = (Damageable) meta;
                    damageable.setDamage(0);
                    item.setItemMeta(meta);
                    p.sendMessage(ChatM.logoEcho()+"Item réparé.");
                    return true;
                }
                p.sendMessage(ChatM.logoEcho()+"l'item n'est pas réparable");
                return true;
            }
            p.sendMessage(ChatM.logoEcho()+"l'item n'est pas réparable");
            return true;
        }
        p.sendMessage(ChatM.noPermissionMessage());
        return true;
    }
}
