package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainMenu extends BaseGUI {

    public MainMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getGUITitle("main"), 45);
    }

    @Override
    public void setupItems() {
        GUIUtils.fillBorders(inventory);
        
        CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(player.getUniqueId());
        int currentCombo = playerData != null ? playerData.getCurrentCombo() : 0;
        int bestCombo = playerData != null ? playerData.getBestCombo() : 0;
        int totalHits = playerData != null ? playerData.getTotalHits() : 0;

        setItem(10, ItemBuilder.create(Material.PLAYER_HEAD)
                .setName(plugin.getLangManager().getGUIItem("stats"))
                .setLore(
                    plugin.getLangManager().getGUILore("stats_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getGUIItem("current_combo") + ": &a" + GUIUtils.formatNumber(currentCombo),
                    "&f▸ " + plugin.getLangManager().getGUIItem("best_combo") + ": &6" + GUIUtils.formatNumber(bestCombo),
                    "&f▸ " + plugin.getLangManager().getGUIItem("total_hits") + ": &e" + GUIUtils.formatNumber(totalHits),
                    "&f▸ " + plugin.getLangManager().getGUILore("level") + ": " + getComboLevelName(bestCombo),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_details")
                )
                .setGlowing(currentCombo > 0)
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new StatsMenu(plugin, p))
        );

        setItem(12, ItemBuilder.create(Material.GOLDEN_SWORD)
                .setName(plugin.getLangManager().getGUIItem("leaderboard"))
                .setLore(
                    plugin.getLangManager().getGUILore("leaderboard_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("help.leaderboard"),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_leaderboard")
                )
                .setGlowing(true)
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new LeaderboardMenu(plugin, p))
        );

        setItem(14, ItemBuilder.create(Material.REDSTONE)
                .setName(plugin.getLangManager().getGUIItem("settings"))
                .setLore(
                    plugin.getLangManager().getGUILore("settings_desc"),
                    "",
                    "&f▸ " + plugin.getLangManager().getMessage("combat_info.sounds"),
                    "&f▸ " + plugin.getLangManager().getMessage("combat_info.actionbar"),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_settings")
                )
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new SettingsMenu(plugin, p))
        );

        if (player.hasPermission("combatmaster.admin")) {
            setItem(16, ItemBuilder.create(Material.COMMAND_BLOCK)
                    .setName(plugin.getLangManager().getGUIItem("admin"))
                    .setLore(
                        plugin.getLangManager().getGUILore("admin_desc"),
                        "",
                        plugin.getLangManager().getMessage("general.click_for_admin")
                    )
                    .setGlowing(true)
                    .build(),
                p -> plugin.getMenuManager().openMenu(p, new AdminMenu(plugin, p))
            );
        }

        setItem(31, ItemBuilder.create(Material.BARRIER)
                .setName(plugin.getLangManager().getGUIItem("close"))
                .setLore(plugin.getLangManager().getGUILore("close_desc"))
                .build(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );

        setItem(40, ItemBuilder.create(Material.BOOK)
                .setName(plugin.getLangManager().getGUIItem("help"))
                .setLore(
                    plugin.getLangManager().getGUILore("help_desc"),
                    "",
                    plugin.getLangManager().getMessage("help.commands"),
                    plugin.getLangManager().getMessage("help.discord"),
                    plugin.getLangManager().getMessage("help.github"),
                    "",
                    plugin.getLangManager().getMessage("general.click_for_help")
                )
                .build(),
            p -> showHelpInfo(p)
        );
    }

    private void showHelpInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getGUITitle("main"));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("help.combo_system"));
        player.sendMessage(plugin.getLangManager().getMessage("help.hit_system"));
        player.sendMessage(plugin.getLangManager().getMessage("help.sound_effects"));
        player.sendMessage(plugin.getLangManager().getMessage("help.leaderboard"));
        player.sendMessage(plugin.getLangManager().getMessage("help.titles"));
        player.sendMessage("");
        player.sendMessage(plugin.getLangManager().getMessage("help.commands"));
        player.sendMessage(plugin.getLangManager().getMessage("help.discord"));
        player.sendMessage(plugin.getLangManager().getMessage("help.github"));
        player.sendMessage("");
        playSuccessSound();
    }

    private String getComboLevelName(int combo) {
        if (combo < 10) return plugin.getLangManager().getMessage("levels.beginner");
        else if (combo < 25) return plugin.getLangManager().getMessage("levels.experienced");
        else if (combo < 50) return plugin.getLangManager().getMessage("levels.master");
        else if (combo < 100) return plugin.getLangManager().getMessage("levels.expert");
        else if (combo < 200) return plugin.getLangManager().getMessage("levels.legendary");
        else if (combo < 500) return plugin.getLangManager().getMessage("levels.epic");
        else return plugin.getLangManager().getMessage("levels.godlike");
    }
}