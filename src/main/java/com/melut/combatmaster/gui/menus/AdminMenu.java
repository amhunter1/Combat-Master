package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminMenu extends BaseGUI {

    public AdminMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getGUITitle("admin"), 45);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Plugin Management Section
        setItem(10, ItemBuilder.create(Material.REDSTONE_TORCH)
                .setName(plugin.getLangManager().getMessage("gui.admin.plugin_management"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.admin.reload_description"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.admin.plugin_info_title"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.lore.level") + ": &e" + plugin.getDescription().getVersion(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.page_info") + ": &a" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.language_settings") + ": &e" + plugin.getLangManager().getCurrentLanguage(),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_details")
                )
                .setGlowing(true)
                .build(),
            p -> showPluginInfo(p)
        );

        setItem(12, ItemBuilder.create(Material.REPEATER)
                .setName(plugin.getLangManager().getMessage("gui.admin.reload_plugin"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.admin.reload_description"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("system.config_reloaded"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.language_settings"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.settings_updated"),
                    "",
                    plugin.getLangManager().getMessage("gui.admin.reload_warning"),
                    "",
                    plugin.getLangManager().getMessage("gui.admin.reload_action")
                )
                .build(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                try {
                    plugin.reloadPlugin();
                    p.sendMessage(plugin.getLangManager().getMessage("commands.plugin_reloaded"));
                    playSuccessSound();
                } catch (Exception e) {
                    p.sendMessage(plugin.getLangManager().getMessage("gui.admin.reload_error", e.getMessage()));
                    playErrorSound();
                }
            }
        );

        setItem(14, ItemBuilder.create(Material.COMPARATOR)
                .setName(plugin.getLangManager().getMessage("gui.admin.database_management"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.settings.database_settings"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.database_settings") + ": &e" + plugin.getConfigManager().getDatabaseType(),
                    "&f▸ " + plugin.getLangManager().getMessage("general.success") + ": &a" + plugin.getLangManager().getMessage("gui.lore.sounds_on"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.stats"),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_details")
                )
                .build(),
            p -> showDatabaseInfo(p)
        );

        // Player Management Section  
        setItem(16, ItemBuilder.create(Material.PLAYER_HEAD)
                .setName(plugin.getLangManager().getMessage("gui.admin.player_management"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.player_mgmt.player_options"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.stats") + ": &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.items.stats"),
                    "&f▸ " + plugin.getLangManager().getMessage("commands.reset_usage"),
                    "",
                    plugin.getLangManager().getMessage("gui.player_mgmt.click_to_manage")
                )
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new PlayerManagementMenu(plugin, p))
        );

        // System Information
        setItem(28, ItemBuilder.create(Material.BOOK)
                .setName(plugin.getLangManager().getMessage("gui.quick.system_info"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.info_desc"),
                    "",
                    "&f▸ Java Version: &e" + System.getProperty("java.version"),
                    "&f▸ Server: &e" + plugin.getServer().getName() + " " + plugin.getServer().getVersion(),
                    "&f▸ Plugin Folder: &7plugins/Combat-Master/",
                    "&f▸ Memory Usage: &a" + getMemoryUsage(),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.info_action")
                )
                .build(),
            p -> showSystemInfo(p)
        );

        setItem(30, ItemBuilder.create(Material.COMMAND_BLOCK)
                .setName(plugin.getLangManager().getMessage("gui.admin.quick_commands"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.quick.reset_all_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.reset_all_combos"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.leaderboard.no_data"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.quick.memory_cleanup"),
                    "",
                    plugin.getLangManager().getMessage("gui.quick.info_action")
                )
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new QuickCommandsMenu(plugin, p))
        );

        setItem(32, ItemBuilder.create(Material.WRITABLE_BOOK)
                .setName(plugin.getLangManager().getMessage("gui.admin.config_editor"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.settings.visual_settings"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("system.config_reloaded"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.language_settings"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.database_settings"),
                    "&f▸ " + plugin.getLangManager().getMessage("gui.settings.combat_settings"),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_details")
                )
                .build(),
            p -> showConfigInfo(p)
        );

        setItem(34, ItemBuilder.create(Material.BEACON)
                .setName("&a🎯 bStats Durumu")
                .setLore(
                    "&7Metrics ve istatistik durumu",
                    "",
                    "&f▸ bStats Plugin ID: &e28408",
                    "&f▸ Veri toplama: &aAktif",
                    "&f▸ Gizlilik: &7Anonim veriler",
                    "&f▸ URL: &ebstats.org/plugin/bukkit/Combat-Master",
                    "",
                    "&7İstatistikler otomatik olarak gönderiliyor"
                )
                .build()
        );

        // Navigation Buttons
        setItem(36, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new MainMenu(plugin, p))
        );

        setItem(40, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(plugin.getLangManager().getMessage("gui.admin.reload_success"));
            }
        );

        setItem(44, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private void showPluginInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&6&l⚡ Combat-Master Admin Bilgileri ⚡"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Plugin: &fCombat-Master v" + plugin.getDescription().getVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Geliştirici: &fMelut"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ API Versiyonu: &f" + plugin.getDescription().getAPIVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dil: &f" + plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Açık Menüler: &a" + plugin.getMenuManager().getOpenMenuCount()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size()));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a▸ Discord: &fhttps://discord.com/users/871721944268038175"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a▸ GitHub: &fhttps://github.com/amhunter1"));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showDatabaseInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&d&l📊 Database Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database Türü: &f" + plugin.getConfigManager().getDatabaseType().toUpperCase()));
        if (plugin.getConfigManager().getDatabaseType().equals("mysql")) {
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Host: &f" + plugin.getConfigManager().getDatabaseHost()));
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Port: &f" + plugin.getConfigManager().getDatabasePort()));
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database: &f" + plugin.getConfigManager().getDatabaseName()));
        } else {
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dosya: &f" + plugin.getConfigManager().getSqliteFileName()));
        }
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bağlantı: &aAktif"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Pool Boyutu: &f10 (max)"));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showSystemInfo(Player player) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;

        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&b&l📊 Sistem Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Java Versiyonu: &f" + System.getProperty("java.version")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ OS: &f" + System.getProperty("os.name")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sunucu: &f" + plugin.getServer().getName()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Versiyon: &f" + plugin.getServer().getVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bukkit: &f" + plugin.getServer().getBukkitVersion()));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bellek Kullanımı: &a" + usedMemory + "MB &7/ &e" + maxMemory + "MB"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Serbest Bellek: &a" + freeMemory + "MB"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ CPU Çekirdekleri: &f" + runtime.availableProcessors()));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showConfigInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c&l📝 Config Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dil: &f" + plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sesler: &f" + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Action Bar: &f" + (plugin.getConfigManager().isActionBarEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Combo Timeout: &f" + plugin.getConfigManager().getComboTimeout() + " saniye"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Mob Hits: &f" + (plugin.getConfigManager().isMobHitsEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Leaderboard Boyutu: &f" + plugin.getConfigManager().getLeaderboardSize()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database: &f" + plugin.getConfigManager().getDatabaseType().toUpperCase()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Aktif Dünyalar: &f" +
            (plugin.getConfigManager().getEnabledWorlds().isEmpty() ? "Tümü" :
            String.join(", ", plugin.getConfigManager().getEnabledWorlds()))));
        player.sendMessage("");
        playSuccessSound();
    }

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        return usedMemory + "MB / " + maxMemory + "MB";
    }
}