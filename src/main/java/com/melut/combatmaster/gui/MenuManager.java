package com.melut.combatmaster.gui;

import com.melut.combatmaster.CombatMaster;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private final CombatMaster plugin;
    private final Map<UUID, BaseGUI> openMenus;

    public MenuManager(CombatMaster plugin) {
        this.plugin = plugin;
        this.openMenus = new HashMap<>();
    }

    public void openMenu(Player player, BaseGUI menu) {
        if (menu == null) return;
        
        closeMenu(player);
        openMenus.put(player.getUniqueId(), menu);
        menu.open();
    }

    public void closeMenu(Player player) {
        BaseGUI menu = openMenus.remove(player.getUniqueId());
        if (menu != null) {
            player.closeInventory();
        }
    }

    public BaseGUI getMenu(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public boolean hasMenuOpen(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }

    public void refreshMenu(Player player) {
        BaseGUI menu = getMenu(player);
        if (menu != null) {
            menu.refresh();
        }
    }

    public void closeAllMenus() {
        for (UUID playerId : openMenus.keySet()) {
            Player player = plugin.getServer().getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.closeInventory();
            }
        }
        openMenus.clear();
    }

    public void removePlayer(Player player) {
        openMenus.remove(player.getUniqueId());
    }

    public int getOpenMenuCount() {
        return openMenus.size();
    }

    public boolean isMenuInventory(String title) {
        return openMenus.values().stream()
                .anyMatch(menu -> menu.getTitle().equals(title));
    }
}