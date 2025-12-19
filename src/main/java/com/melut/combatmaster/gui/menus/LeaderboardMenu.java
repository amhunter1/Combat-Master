package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.database.DatabaseManager;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaderboardMenu extends BaseGUI {

    private final int currentPage;
    private final int itemsPerPage = 21; // 3 rows of 7 items
    private List<DatabaseManager.LeaderboardEntry> leaderboardData;

    public LeaderboardMenu(CombatMaster plugin, Player player) {
        this(plugin, player, 0);
    }

    public LeaderboardMenu(CombatMaster plugin, Player player, int page) {
        super(plugin, player, plugin.getLangManager().getGUITitle("leaderboard"), 54);
        this.currentPage = page;
        loadLeaderboardData();
    }

    @Override
    public void setupItems() {
        GUIUtils.fillBorders(inventory);
        
        if (leaderboardData == null || leaderboardData.isEmpty()) {
            setItem(22, ItemBuilder.create(Material.BARRIER)
                    .setName("&c" + plugin.getLangManager().getMessage("gui.leaderboard.no_data"))
                    .setLore(
                        plugin.getLangManager().getMessage("gui.leaderboard_extended.no_data_desc1"),
                        plugin.getLangManager().getMessage("gui.leaderboard_extended.no_data_desc2")
                    )
                    .build()
            );
        } else {
            displayLeaderboardItems();
        }

        setupNavigationButtons();
        setupControlButtons();
    }

    private void loadLeaderboardData() {
        plugin.getDatabaseManager().getLeaderboard(1000).thenAccept(data -> {
            this.leaderboardData = data;
            plugin.getServer().getScheduler().runTask(plugin, this::refresh);
        });
    }

    private void displayLeaderboardItems() {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, leaderboardData.size());
        
        int slot = 10; // Start position
        int itemCount = 0;
        
        for (int i = startIndex; i < endIndex; i++) {
            DatabaseManager.LeaderboardEntry entry = leaderboardData.get(i);
            
            // Skip border slots (8, 9, 17, 18, 26, 27, 35, 36)
            while (slot % 9 == 8 || slot % 9 == 0) {
                slot++;
            }
            
            Material material = getRankMaterial(entry.getRank());
            boolean isCurrentPlayer = entry.getPlayerName().equals(player.getName());
            
            setItem(slot, ItemBuilder.create(material)
                    .setName(getRankColor(entry.getRank()) + "#" + entry.getRank() + " " + entry.getPlayerName())
                    .setLore(
                        "",
                        "&f▸ " + plugin.getLangManager().getMessage("gui.items.best_combo") + ": &6" + GUIUtils.formatNumber(entry.getBestCombo()),
                        "&f▸ " + plugin.getLangManager().getMessage("levels.title", getComboLevelName(entry.getBestCombo())),
                        "&f▸ " + plugin.getLangManager().getMessage("ranks.title", getRankTitle(entry.getRank())),
                        "",
                        isCurrentPlayer ? plugin.getLangManager().getMessage("gui.leaderboard_extended.current_player") : plugin.getLangManager().getMessage("gui.leaderboard_extended.click_for_details")
                    )
                    .setGlowing(isCurrentPlayer || entry.getRank() <= 3)
                    .setAmount(Math.max(1, Math.min(64, entry.getRank())))
                    .build(),
                p -> {
                    if (!isCurrentPlayer) {
                        Player target = plugin.getServer().getPlayer(entry.getPlayerName());
                        if (target != null) {
                            plugin.getMenuManager().openMenu(p, new StatsMenu(plugin, target));
                        } else {
                            p.sendMessage(plugin.getLangManager().getMessage("commands.player_not_found"));
                        }
                    }
                }
            );
            
            slot++;
            itemCount++;
            
            // Move to next row after 7 items
            if (itemCount % 7 == 0) {
                slot += 2; // Skip border slots
            }
        }
    }

    private void setupNavigationButtons() {
        int totalPages = getTotalPages();
        
        // Previous page button
        if (currentPage > 0) {
            setItem(45, ItemBuilder.create(Material.ARROW)
                    .setName(plugin.getLangManager().getGUIItem("prev_page"))
                    .setLore(
                        "&7Page: " + (currentPage) + "/" + totalPages,
                        plugin.getLangManager().getMessage("gui.leaderboard_extended.previous_page")
                    )
                    .build(),
                p -> plugin.getMenuManager().openMenu(p, new LeaderboardMenu(plugin, p, currentPage - 1))
            );
        } else {
            setItem(45, ItemBuilder.createPlaceholder(""));
        }

        // Page info
        setItem(49, ItemBuilder.create(Material.BOOK)
                .setName(plugin.getLangManager().getMessage("gui.settings.page_info"))
                .setLore(
                    "&7Current Page: &e" + (currentPage + 1),
                    "&7Total Pages: &e" + totalPages,
                    "&7Total Players: &e" + (leaderboardData != null ? leaderboardData.size() : 0),
                    "",
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.page_info", itemsPerPage)
                )
                .build()
        );

        // Next page button
        if (currentPage < totalPages - 1) {
            setItem(53, ItemBuilder.create(Material.ARROW)
                    .setName(plugin.getLangManager().getGUIItem("next_page"))
                    .setLore(
                        "&7Page: " + (currentPage + 2) + "/" + totalPages,
                        "&aNext page"
                    )
                    .build(),
                p -> plugin.getMenuManager().openMenu(p, new LeaderboardMenu(plugin, p, currentPage + 1))
            );
        } else {
            setItem(53, ItemBuilder.createPlaceholder(""));
        }
    }

    private void setupControlButtons() {
        // Back button
        setItem(36, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new MainMenu(plugin, p))
        );

        // Refresh button
        setItem(40, ItemBuilder.createRefreshButton(),
            p -> {
                loadLeaderboardData();
                playSuccessSound();
                p.sendMessage(plugin.getLangManager().getMessage("system.stats_updated"));
            }
        );

        // Close button
        setItem(44, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );

        // Your rank button
        findPlayerRank();
    }

    private void findPlayerRank() {
        if (leaderboardData == null) return;
        
        for (DatabaseManager.LeaderboardEntry entry : leaderboardData) {
            if (entry.getPlayerName().equals(player.getName())) {
                setItem(4, ItemBuilder.create(Material.PLAYER_HEAD)
                        .setName(plugin.getLangManager().getMessage("gui.leaderboard_extended.your_ranking"))
                        .setLore(
                            plugin.getLangManager().getMessage("gui.leaderboard_extended.ranking_desc"),
                            "",
                            "&f▸ Rank: " + getRankColor(entry.getRank()) + "#" + entry.getRank(),
                            "&f▸ " + plugin.getLangManager().getMessage("gui.items.best_combo") + ": &6" + GUIUtils.formatNumber(entry.getBestCombo()),
                            "&f▸ " + getRankTitle(entry.getRank()),
                            "",
                            plugin.getLangManager().getMessage("gui.leaderboard_extended.ranking_success")
                        )
                        .setGlowing(true)
                        .build(),
                    p -> {
                        // Jump to page containing the player
                        int playerPage = (entry.getRank() - 1) / itemsPerPage;
                        if (playerPage != currentPage) {
                            plugin.getMenuManager().openMenu(p, new LeaderboardMenu(plugin, p, playerPage));
                        }
                    }
                );
                return;
            }
        }

        // Player not in ranking
        setItem(4, ItemBuilder.create(Material.GRAY_DYE)
                .setName(plugin.getLangManager().getMessage("gui.leaderboard_extended.not_ranked"))
                .setLore(
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.not_ranked_desc"),
                    "",
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.make_first_combo"),
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.join_leaderboard"),
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.compete_with_others"),
                    "",
                    plugin.getLangManager().getMessage("gui.leaderboard_extended.good_luck")
                )
                .build()
        );
    }

    private int getTotalPages() {
        if (leaderboardData == null || leaderboardData.isEmpty()) return 1;
        return (int) Math.ceil((double) leaderboardData.size() / itemsPerPage);
    }

    private Material getRankMaterial(int rank) {
        return switch (rank) {
            case 1 -> Material.GOLD_INGOT;
            case 2 -> Material.IRON_INGOT;
            case 3 -> Material.COPPER_INGOT;
            default -> {
                if (rank <= 10) yield Material.DIAMOND;
                else if (rank <= 25) yield Material.EMERALD;
                else if (rank <= 50) yield Material.COAL;
                else yield Material.COBBLESTONE;
            }
        };
    }

    private String getRankColor(int rank) {
        return GUIUtils.getRankColor(rank);
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
}