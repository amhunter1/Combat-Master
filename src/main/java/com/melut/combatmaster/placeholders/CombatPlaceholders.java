package com.melut.combatmaster.placeholders;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.database.DatabaseManager;
import com.melut.combatmaster.managers.CombatManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class CombatPlaceholders extends PlaceholderExpansion {

    private final CombatMaster plugin;
    private final DatabaseManager databaseManager;

    public CombatPlaceholders(CombatMaster plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    @Override
    public String getIdentifier() {
        return "combatmaster";
    }

    @Override
    public String getAuthor() {
        return "Melut";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return "";

        CombatManager.CombatData combatData = plugin.getCombatManager().getPlayerData(player.getUniqueId());

        switch (params.toLowerCase()) {
            case "current_combo":
                return combatData != null ? String.valueOf(combatData.getCurrentCombo()) : "0";

            case "best_combo":
                return combatData != null ? String.valueOf(combatData.getBestCombo()) : "0";

            case "total_hits":
                return combatData != null ? String.valueOf(combatData.getTotalHits()) : "0";

            case "rank":
                return getRank(player);

            default:
                if (params.startsWith("leaderboard_")) {
                    return handleLeaderboardPlaceholder(params);
                }
        }

        return null;
    }

    private String getRank(Player player) {
        return "N/A";
    }

    private String handleLeaderboardPlaceholder(String params) {
        String[] parts = params.split("_");
        if (parts.length < 3) return "";

        try {
            int position = Integer.parseInt(parts[1]);
            String type = parts[2]; // name veya combo
            return "N/A";

        } catch (NumberFormatException e) {
            return "";
        }
    }
}