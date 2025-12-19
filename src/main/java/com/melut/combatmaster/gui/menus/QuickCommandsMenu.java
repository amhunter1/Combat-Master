package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class QuickCommandsMenu extends BaseGUI {

    public QuickCommandsMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getMessage("gui.quick.title"), 36);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Reset All Combos
        setItem(10, ItemBuilder.create(Material.TNT)
                .setName(plugin.getLangManager().getMessage("gui.quick.reset_all_combos"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.reset_all_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.player_mgmt.player_options") + ": &e" + getActiveCombosCount() + " / " + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.reset_warning"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.player_mgmt.reset_all_player_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.reset_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.reset_action")
                )
                .setGlowing(true)
                .build(),
            p -> executeResetAllCombos(p)
        );

        // Close All Menus
        setItem(12, ItemBuilder.create(Material.BARRIER)
                .setName(plugin.getLangManager().getMessage("gui.quick.close_all_menus"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.close_all_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.page_info") + ": &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.close_action")
                )
                .build(),
            p -> executeCloseAllMenus(p)
        );

        // Reload Plugin
        setItem(14, ItemBuilder.create(Material.REPEATER)
                .setName(plugin.getLangManager().getMessage("gui.admin.reload_plugin"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.reload_all_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("system.config_reloaded"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.language_settings"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.settings_updated"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.close_all_menus"),
                    "",
                    plugin.getLangManager().getMessage("gui.admin.reload_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.reload_all_action")
                )
                .build(),
            p -> executePluginReload(p)
        );

        // Save All Data
        setItem(16, ItemBuilder.create(Material.CHEST)
                .setName(plugin.getLangManager().getMessage("gui.quick.save_all_data"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.save_all_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.stats") + ": &e" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "",
                    plugin.getLangManager().getMessage("general.success")
                )
                .build(),
            p -> executeSaveAllData(p)
        );

        // Show Server Stats
        setItem(20, ItemBuilder.create(Material.BOOK)
                .setName(plugin.getLangManager().getMessage("gui.quick.system_info"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.info_desc"),
                    "",
                    "&f▸ Online: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.current_combo") + ": &e" + getActiveCombosCount(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.page_info") + ": &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Memory: &e" + getMemoryUsage(),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.info_action")
                )
                .build(),
            p -> executeShowServerStats(p)
        );

        // Emergency Stop
        setItem(22, ItemBuilder.create(Material.REDSTONE_BLOCK)
                .setName(plugin.getLangManager().getMessage("gui.quick.emergency_stop"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.stop_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.save_all_data"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.close_all_menus"),
                    "&f▸ " + plugin.getLangManager().getMessage("system.plugin_disabled"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.memory_cleanup"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.stop_warning"),
                    plugin.getLangManager().getMessage("gui.quick.stop_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.stop_action")
                )
                .build(),
            p -> executeEmergencyStop(p)
        );

        // Clear Cache
        setItem(24, ItemBuilder.create(Material.SPONGE)
                .setName(plugin.getLangManager().getMessage("gui.quick.memory_cleanup"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.cleanup_desc"),
                    "&f▸ JVM Garbage Collection",
                    "",
                    plugin.getLangManager().getMessage("gui.quick.cleanup_action")
                )
                .build(),
            p -> executeClearCache(p)
        );

        // Navigation
        setItem(27, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new AdminMenu(plugin, p))
        );

        setItem(31, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(plugin.getLangManager().getMessage("system.quick_menu_updated"));
            }
        );

        setItem(35, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private void executeResetAllCombos(Player admin) {
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
        
        plugin.getLogger().info(admin.getName() + " tarafından tüm combo'lar sıfırlandı.");
        playSuccessSound();
        refresh();
    }

    private void executeCloseAllMenus(Player admin) {
        int closedCount = plugin.getMenuManager().getOpenMenuCount();
        plugin.getMenuManager().closeAllMenus();
        
        admin.sendMessage(plugin.getLangManager().getMessage("system.menus_closed_success", closedCount));
        plugin.getLogger().info(admin.getName() + " tarafından tüm menüler kapatıldı.");
        
        playSuccessSound();
        
        // Return to admin menu since this menu was also closed
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getMenuManager().openMenu(admin, new AdminMenu(plugin, admin));
        }, 1L);
    }

    private void executePluginReload(Player admin) {
        plugin.getMenuManager().closeMenu(admin);
        
        try {
            plugin.reloadPlugin();
            admin.sendMessage(plugin.getLangManager().getMessage("system.plugin_reloaded"));
            plugin.getLogger().info(admin.getName() + " tarafından plugin reload edildi.");
            playSuccessSound();
        } catch (Exception e) {
            admin.sendMessage(plugin.getLangManager().getMessage("system.reload_error", e.getMessage()));
            plugin.getLogger().severe("Plugin reload hatası: " + e.getMessage());
            playErrorSound();
        }
    }

    private void executeSaveAllData(Player admin) {
        plugin.getCombatManager().saveAllData();
        
        admin.sendMessage(plugin.getLangManager().getMessage("system.data_saved"));
        admin.sendMessage(plugin.getLangManager().getMessage("system.data_saved_details", plugin.getServer().getOnlinePlayers().size()));
        
        plugin.getLogger().info(admin.getName() + " tarafından tüm veriler kaydedildi.");
        playSuccessSound();
    }

    private void executeShowServerStats(Player admin) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&d&l📊 Sunucu İstatistikleri"));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Aktif Combo'lar: &a" + getActiveCombosCount()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Açık Menüler: &e" + plugin.getMenuManager().getOpenMenuCount()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Toplam Hit (Online): &b" + getTotalHitsOnline()));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Java Versiyonu: &7" + System.getProperty("java.version")));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sunucu: &7" + plugin.getServer().getName() + " " + plugin.getServer().getVersion()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Plugin Versiyonu: &7" + plugin.getDescription().getVersion()));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Kullanılan Bellek: &a" + usedMemory + "MB &7/ &e" + maxMemory + "MB"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Serbest Bellek: &a" + freeMemory + "MB"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ CPU Çekirdekleri: &7" + runtime.availableProcessors()));
        admin.sendMessage("");
        
        playSuccessSound();
    }

    private void executeEmergencyStop(Player admin) {
        admin.sendMessage(plugin.getLangManager().getMessage("system.emergency_stop_warning"));
        admin.sendMessage(plugin.getLangManager().getMessage("system.emergency_stop_countdown"));
        
        plugin.getServer().broadcastMessage(plugin.getLangManager().getMessage("system.emergency_stop_broadcast"));
        plugin.getServer().broadcastMessage(plugin.getLangManager().getMessage("system.emergency_stop_broadcast_countdown"));
        
        plugin.getLogger().warning("ACIL DURDURMA: " + admin.getName() + " tarafından başlatıldı!");
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getCombatManager().saveAllData();
            plugin.getMenuManager().closeAllMenus();
            
            plugin.getServer().broadcastMessage(plugin.getLangManager().getMessage("system.emergency_stop_complete"));
            plugin.getLogger().info("Plugin acil durdurma ile güvenli şekilde kapatıldı.");
            
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }, 100L); // 5 saniye = 100 tick
        
        playErrorSound();
        plugin.getMenuManager().closeMenu(admin);
    }

    private void executeClearCache(Player admin) {
        // JVM Garbage Collection'ı çalıştır
        System.gc();
        
        admin.sendMessage(plugin.getLangManager().getMessage("system.cache_cleaned"));
        admin.sendMessage(plugin.getLangManager().getMessage("system.gc_executed"));
        
        plugin.getLogger().info(admin.getName() + " tarafından cache temizlendi.");
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

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        return usedMemory + "MB / " + maxMemory + "MB";
    }
}