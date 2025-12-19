package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StatsMenu extends BaseGUI {

    public StatsMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getMessage("gui.titles.stats", player.getName()), 45);
    }

    @Override
    public void setupItems() {
        GUIUtils.fillBorders(inventory);
        
        CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(player.getUniqueId());
        int currentCombo = playerData != null ? playerData.getCurrentCombo() : 0;
        int bestCombo = playerData != null ? playerData.getBestCombo() : 0;
        int totalHits = playerData != null ? playerData.getTotalHits() : 0;

        setItem(11, ItemBuilder.create(GUIUtils.getComboMaterial(bestCombo))
                .setName(plugin.getLangManager().getGUIItem("best_combo"))
                .setLore(
                    plugin.getLangManager().getGUILore("best_combo_desc"),
                    "",
                    plugin.getLangManager().getMessage("gui.lore.best_combo_value", GUIUtils.formatNumber(bestCombo)),
                    "",
                    plugin.getLangManager().getMessage("gui.lore.level", getComboLevelName(bestCombo)),
                    "&7" + getComboDescription(bestCombo)
                )
                .setAmount(Math.max(1, Math.min(64, bestCombo)))
                .setGlowing(bestCombo > 50)
                .build()
        );

        setItem(13, ItemBuilder.create(Material.DIAMOND)
                .setName(plugin.getLangManager().getGUIItem("total_hits"))
                .setLore(
                    plugin.getLangManager().getGUILore("total_hits_desc"),
                    "",
                    plugin.getLangManager().getMessage("gui.lore.total_hits_value", GUIUtils.formatNumber(totalHits)),
                    "",
                    "&7Combat deneyiminizin göstergesi"
                )
                .setAmount(Math.max(1, Math.min(64, totalHits / 100)))
                .build()
        );

        plugin.getDatabaseManager().getLeaderboard(100).thenAccept(leaderboard -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                int rank = -1;
                for (int i = 0; i < leaderboard.size(); i++) {
                    if (leaderboard.get(i).getPlayerName().equals(player.getName())) {
                        rank = i + 1;
                        break;
                    }
                }

                Material rankMaterial = switch (rank) {
                    case 1 -> Material.GOLD_INGOT;
                    case 2 -> Material.IRON_INGOT;
                    case 3 -> Material.COPPER_INGOT;
                    default -> Material.COAL;
                };

                setItem(21, ItemBuilder.create(rankMaterial)
                        .setName(plugin.getLangManager().getGUIItem("rank"))
                        .setLore(
                            plugin.getLangManager().getGUILore("rank_desc"),
                            "",
                            rank > 0 ? plugin.getLangManager().getMessage("gui.lore.rank_value", rank) : plugin.getLangManager().getMessage("gui.lore.no_rank"),
                            "",
                            rank > 0 ? GUIUtils.getRankColor(rank) + getRankTitle(rank) : "&7İlk combo'nuzu yaparak sıralamaya girin!"
                        )
                        .setGlowing(rank <= 3 && rank > 0)
                        .build()
                );
            });
        });

        setItem(23, ItemBuilder.create(Material.CLOCK)
                .setName(plugin.getLangManager().getGUIItem("combat_info"))
                .setLore(
                    plugin.getLangManager().getGUILore("combat_info_desc"),
                    "",
                    plugin.getLangManager().getMessage("combat_info.timeout", plugin.getConfigManager().getComboTimeout()),
                    plugin.getLangManager().getMessage("combat_info.sounds") + ": " + (plugin.getConfigManager().isSoundsEnabled() ? plugin.getLangManager().getMessage("gui.lore.sounds_on") : plugin.getLangManager().getMessage("gui.lore.sounds_off")),
                    plugin.getLangManager().getMessage("combat_info.actionbar") + ": " + (plugin.getConfigManager().isActionBarEnabled() ? plugin.getLangManager().getMessage("gui.lore.sounds_on") : plugin.getLangManager().getMessage("gui.lore.sounds_off")),
                    plugin.getLangManager().getMessage("combat_info.mob_hits") + ": " + (plugin.getConfigManager().isMobHitsEnabled() ? plugin.getLangManager().getMessage("gui.lore.sounds_on") : plugin.getLangManager().getMessage("gui.lore.sounds_off")),
                    "",
                    plugin.getLangManager().getMessage("combat_info.server_settings")
                )
                .build()
        );

        String progressBar = GUIUtils.getProgressBar(bestCombo, getNextLevelTarget(bestCombo), 20);
        setItem(31, ItemBuilder.create(Material.EXPERIENCE_BOTTLE)
                .setName(plugin.getLangManager().getGUIItem("progress"))
                .setLore(
                    plugin.getLangManager().getGUILore("progress_desc"),
                    "",
                    progressBar,
                    "&f" + bestCombo + " / " + getNextLevelTarget(bestCombo),
                    "",
                    plugin.getLangManager().getMessage("general.continue")
                )
                .build()
        );

        setItem(36, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new MainMenu(plugin, p))
        );

        setItem(40, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(plugin.getLangManager().getMessage("system.stats_updated"));
            }
        );

        setItem(44, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private String getComboDescription(int combo) {
        if (combo == 0) return plugin.getLangManager().getMessage("combo_descriptions.none");
        else if (combo < 10) return plugin.getLangManager().getMessage("combo_descriptions.beginner");
        else if (combo < 25) return plugin.getLangManager().getMessage("combo_descriptions.good");
        else if (combo < 50) return plugin.getLangManager().getMessage("combo_descriptions.impressive");
        else if (combo < 100) return plugin.getLangManager().getMessage("combo_descriptions.outstanding");
        else if (combo < 200) return plugin.getLangManager().getMessage("combo_descriptions.legendary");
        else if (combo < 500) return plugin.getLangManager().getMessage("combo_descriptions.epic");
        else return plugin.getLangManager().getMessage("combo_descriptions.godlike");
    }

    private String getRankTitle(int rank) {
        return switch (rank) {
            case 1 -> plugin.getLangManager().getMessage("ranks.champion");
            case 2 -> plugin.getLangManager().getMessage("ranks.second");
            case 3 -> plugin.getLangManager().getMessage("ranks.third");
            default -> {
                if (rank <= 10) yield plugin.getLangManager().getMessage("ranks.top10");
                else if (rank <= 25) yield plugin.getLangManager().getMessage("ranks.top25");
                else if (rank <= 50) yield plugin.getLangManager().getMessage("ranks.top50");
                else yield plugin.getLangManager().getMessage("ranks.ranked");
            }
        };
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

    private int getNextLevelTarget(int current) {
        if (current < 10) return 10;
        else if (current < 25) return 25;
        else if (current < 50) return 50;
        else if (current < 100) return 100;
        else if (current < 200) return 200;
        else if (current < 500) return 500;
        else return current + 100; // Her 100'de bir hedef
    }
}