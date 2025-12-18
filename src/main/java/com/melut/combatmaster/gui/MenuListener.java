package com.melut.combatmaster.gui;

import com.melut.combatmaster.CombatMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {

    private final CombatMaster plugin;
    private final MenuManager menuManager;

    public MenuListener(CombatMaster plugin, MenuManager menuManager) {
        this.plugin = plugin;
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        BaseGUI menu = menuManager.getMenu(player);
        
        if (menu == null) return;
        if (!event.getInventory().equals(menu.getInventory())) return;
        
        event.setCancelled(true);
        
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(menu.getInventory())) return;
        
        int slot = event.getSlot();
        if (slot < 0 || slot >= menu.getSize()) return;
        
        try {
            menu.handleClick(slot);
        } catch (Exception e) {
            plugin.getLogger().warning("Menü click hatası: " + e.getMessage());
            e.printStackTrace();
            player.sendMessage("§cMenü işleminde hata oluştu!");
            menuManager.closeMenu(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        
        Player player = (Player) event.getPlayer();
        BaseGUI menu = menuManager.getMenu(player);
        
        if (menu != null && event.getInventory().equals(menu.getInventory())) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) {
                    menuManager.removePlayer(player);
                } else if (menuManager.getMenu(player) == menu) {
                    menuManager.removePlayer(player);
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        menuManager.removePlayer(event.getPlayer());
    }
}