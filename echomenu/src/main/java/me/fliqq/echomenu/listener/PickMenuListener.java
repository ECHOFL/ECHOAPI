package me.fliqq.echomenu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.fliqq.echomenu.menu.Menus;
import me.fliqq.echopickaxe.manager.PPickaxeManager;

public class PickMenuListener implements Listener {

    private final Menus menusInstance;

    public PickMenuListener(Menus menusInstance) {
        this.menusInstance = menusInstance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            int pickSlot = PPickaxeManager.getInstance().getPick(player.getUniqueId()).getPickSlot();
            if (event.getPlayer().getInventory().getHeldItemSlot() == pickSlot) {
                menusInstance.new PiocheMenu(player).displayTo(player);
            }
        }
    }
}