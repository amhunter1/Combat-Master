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
                .setName(plugin.getLangManager().getMessage("gui.admin.bstats_title"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.admin.bstats_desc"),
                    "",
                    "&f▸ bStats Plugin ID: &e28408",
                    plugin.getLangManager().getMessage("gui.admin.bstats_data_collection"),
                    plugin.getLangManager().getMessage("gui.admin.bstats_privacy"),
                    "&f▸ URL: &ebstats.org/plugin/bukkit/Combat-Master",
                    "",
                    plugin.getLangManager().getMessage("gui.admin.bstats_auto_send")
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
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.plugin_info_header"));
        player.sendMessage("");
        player.sendMessage("&e▸ Plugin: &fCombat-Master v" + plugin.getDescription().getVersion());
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.developer"));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.api_version", plugin.getDescription().getAPIVersion()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.language_info", plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.open_menus", plugin.getMenuManager().getOpenMenuCount()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.online_players", plugin.getServer().getOnlinePlayers().size()));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.discord_info"));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.github_info"));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showDatabaseInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_info_header"));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_type", plugin.getConfigManager().getDatabaseType().toUpperCase()));
        if (plugin.getConfigManager().getDatabaseType().equals("mysql")) {
            player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_host", plugin.getConfigManager().getDatabaseHost()));
            player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_port", plugin.getConfigManager().getDatabasePort()));
            player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_name", plugin.getConfigManager().getDatabaseName()));
        } else {
            player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_file", plugin.getConfigManager().getSqliteFileName()));
        }
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_connection"));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_pool"));
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
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.system_info_header"));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.java_version", System.getProperty("java.version")));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.os_info", System.getProperty("os.name")));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.server_info", plugin.getServer().getName()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.server_version", plugin.getServer().getVersion()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.bukkit_version", plugin.getServer().getBukkitVersion()));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.memory_usage", usedMemory, maxMemory));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.free_memory", freeMemory));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.cpu_cores", runtime.availableProcessors()));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showConfigInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.config_info_header"));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.language_setting", plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.sounds_setting", plugin.getConfigManager().isSoundsEnabled() ? plugin.getLangManager().getMessage("gui.admin.active_status") : plugin.getLangManager().getMessage("gui.admin.inactive_status")));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.actionbar_setting", plugin.getConfigManager().isActionBarEnabled() ? plugin.getLangManager().getMessage("gui.admin.active_status") : plugin.getLangManager().getMessage("gui.admin.inactive_status")));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.combo_timeout_setting", plugin.getConfigManager().getComboTimeout()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.mob_hits_setting", plugin.getConfigManager().isMobHitsEnabled() ? plugin.getLangManager().getMessage("gui.admin.active_status") : plugin.getLangManager().getMessage("gui.admin.inactive_status")));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.leaderboard_size_setting", plugin.getConfigManager().getLeaderboardSize()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.database_setting", plugin.getConfigManager().getDatabaseType().toUpperCase()));
        player.sendMessage(plugin.getLangManager().getMessage("gui.admin.enabled_worlds_setting",
            (plugin.getConfigManager().getEnabledWorlds().isEmpty() ? plugin.getLangManager().getMessage("gui.admin.all_worlds") :
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