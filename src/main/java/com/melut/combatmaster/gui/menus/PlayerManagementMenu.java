package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerManagementMenu extends BaseGUI {

    public PlayerManagementMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getMessage("gui.player_mgmt.title"), 54);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Online Players Section
        int slot = 10;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (slot > 43) break; // Prevent overflow
            
            CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            int currentCombo = playerData != null ? playerData.getCurrentCombo() : 0;
            int bestCombo = playerData != null ? playerData.getBestCombo() : 0;
            int totalHits = playerData != null ? playerData.getTotalHits() : 0;

            boolean hasActiveCombo = currentCombo > 0;
            Material headMaterial = hasActiveCombo ? Material.PLAYER_HEAD : Material.SKELETON_SKULL;

            setItem(slot, ItemBuilder.create(headMaterial)
                    .setName("&e" + onlinePlayer.getName())
                    .setLore(
                        plugin.getLangManager().getMessage("gui.player_mgmt.player_options"),
                        "",
                        "&f▸ " + plugin.getLangManager().getMessage("gui.items.current_combo") + ": " + (hasActiveCombo ? "&a" + currentCombo : "&7" + plugin.getLangManager().getMessage("gui.leaderboard.no_data")),
                        "&f▸ " + plugin.getLangManager().getMessage("gui.items.best_combo") + ": &6" + bestCombo,
                        "&f▸ " + plugin.getLangManager().getMessage("gui.items.total_hits") + ": &e" + totalHits,
                        "&f▸ World: &7" + onlinePlayer.getWorld().getName(),
                        "&f▸ Health: &c" + Math.round(onlinePlayer.getHealth()) + "/20",
                        "",
                        plugin.getLangManager().getMessage("gui.player_mgmt.click_to_manage")
                    )
                    .setGlowing(hasActiveCombo)
                    .build(),
                p -> openPlayerActions(p, onlinePlayer)
            );
            
            slot++;
            // Skip border slots
            if (slot % 9 == 8) slot += 2;
        }

        // Management Tools
        setItem(45, ItemBuilder.create(Material.TNT)
                .setName(plugin.getLangManager().getMessage("gui.player_mgmt.reset_all_player_combos"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.player_mgmt.reset_all_player_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.stats") + ": &e" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.reset_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.player_mgmt.reset_all_player_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.player_mgmt.reset_all_player_action")
                )
                .build(),
            p -> resetAllCombos(p)
        );

        setItem(46, ItemBuilder.create(Material.CLOCK)
                .setName(plugin.getLangManager().getMessage("gui.player_mgmt.global_stats"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.player_mgmt.global_stats_desc"),
                    "",
                    "&f▸ Online: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.current_combo") + ": &e" + getActiveCombosCount(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.total_hits") + " (Online): &b" + getTotalHitsOnline(),
                    "",
                    plugin.getLangManager().getMessage("gui.player_mgmt.global_stats_action")
                )
                .build(),
            p -> showBulkStats(p)
        );

        setItem(47, ItemBuilder.create(Material.BARRIER)
                .setName(plugin.getLangManager().getMessage("gui.player_mgmt.close_all_player_menus"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.player_mgmt.close_all_player_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.page_info") + ": &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.player_mgmt.close_all_player_desc"),
                    "",
                    plugin.getLangManager().getMessage("gui.player_mgmt.close_all_player_action")
                )
                .build(),
            p -> {
                int closedCount = plugin.getMenuManager().getOpenMenuCount();
                plugin.getMenuManager().closeAllMenus();
                p.sendMessage(plugin.getLangManager().getMessage("system.menus_closed_success", closedCount));
                playSuccessSound();
                refresh();
            }
        );

        // Navigation
        setItem(49, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new AdminMenu(plugin, p))
        );

        setItem(53, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(plugin.getLangManager().getMessage("gui.player_mgmt.player_list_updated"));
            }
        );
    }

    private void openPlayerActions(Player admin, Player target) {
        admin.closeInventory();
        
        admin.sendMessage("");
        admin.sendMessage(plugin.getLangManager().getMessage("gui.player_mgmt.management_options", target.getName()));
        admin.sendMessage("");
        admin.sendMessage("&a1. &f/combatmaster reset " + target.getName() + " &7- " + plugin.getLangManager().getMessage("commands.reset_usage"));
        admin.sendMessage("&a2. &f/combatmaster stats " + target.getName() + " &7- " + plugin.getLangManager().getMessage("commands.stats_usage"));
        admin.sendMessage("&a3. &f/tp " + target.getName() + " &7- Teleport to player");
        admin.sendMessage("&a4. &f/tp " + admin.getName() + " " + target.getName() + " &7- Teleport player to you");
        admin.sendMessage("");
        
        CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(target.getUniqueId());
        if (playerData != null) {
            admin.sendMessage(plugin.getLangManager().getMessage("gui.player_mgmt.detailed_stats"));
            admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.current_combo") + ": &e" + playerData.getCurrentCombo());
            admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.best_combo") + ": &6" + playerData.getBestCombo());
            admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.total_hits") + ": &b" + playerData.getTotalHits());
        }
        
        admin.sendMessage("");
        admin.sendMessage(plugin.getLangManager().getMessage("gui.player_mgmt.return_to_menu"));
        admin.sendMessage("");
        
        playSuccessSound();
    }

    private void resetAllCombos(Player admin) {
        int resetCount = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null && data.getCurrentCombo() > 0) {
                plugin.getCombatManager().resetCombo(onlinePlayer);
                resetCount++;
            }
        }
        
        admin.sendMessage(plugin.getLangManager().getMessage("system.combos_reset_success", resetCount));
        plugin.getServer().broadcastMessage(plugin.getLangManager().getMessage("system.combo_reset_broadcast"));
        
        playSuccessSound();
        refresh();
    }

    private void showBulkStats(Player admin) {
        admin.sendMessage("");
        admin.sendMessage(plugin.getLangManager().getMessage("gui.player_mgmt.global_stats"));
        admin.sendMessage("");
        
        int onlineCount = plugin.getServer().getOnlinePlayers().size();
        int activeCombos = getActiveCombosCount();
        long totalHits = getTotalHitsOnline();
        int maxCombo = getMaxComboOnline();
        String topPlayer = getTopPlayerOnline();
        
        admin.sendMessage("&f▸ Online Players: &a" + onlineCount);
        admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.current_combo") + ": &e" + activeCombos);
        admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.total_hits") + " (Online): &b" + GUIUtils.formatNumber(totalHits));
        admin.sendMessage("&f▸ " + plugin.getLangManager().getMessage("gui.items.best_combo") + ": &6" + maxCombo);
        admin.sendMessage("&f▸ Top Player: &e" + (topPlayer != null ? topPlayer : plugin.getLangManager().getMessage("gui.leaderboard.no_data")));
        admin.sendMessage("");
        
        admin.sendMessage("&7These statistics only include online players.");
        admin.sendMessage("&7For complete statistics, check the database.");
        admin.sendMessage("");
        
        playSuccessSound();
    }

    private int getActiveCombosCount() {
        int count = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null && data.getCurrentCombo() > 0) {
                count++;
            }
        }
        return count;
    }

    private long getTotalHitsOnline() {
        long total = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                total += data.getTotalHits();
            }
        }
        return total;
    }

    private int getMaxComboOnline() {
        int max = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                max = Math.max(max, Math.max(data.getCurrentCombo(), data.getBestCombo()));
            }
        }
        return max;
    }

    private String getTopPlayerOnline() {
        String topPlayer = null;
        int maxCombo = 0;
        
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                int bestCombo = data.getBestCombo();
                if (bestCombo > maxCombo) {
                    maxCombo = bestCombo;
                    topPlayer = onlinePlayer.getName();
                }
            }
        }
        
        return topPlayer;
    }
}