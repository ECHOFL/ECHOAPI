package me.fliqq.echoessential.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InvseeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(args.length==1){
            Player target = player.getServer().getPlayer(args[0]);
            if(target!=null){
                Inventory inventory = target.getInventory();
                player.openInventory(inventory);
                /*
                A FINIR !!!!
                 */
            }
        }
        return true;
    }
}
