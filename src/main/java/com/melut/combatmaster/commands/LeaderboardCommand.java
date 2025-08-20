package com.melut.combatmaster.commands;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.database.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LeaderboardCommand implements CommandExecutor {

    private final CombatMaster plugin;
    private final DatabaseManager databaseManager;

    public LeaderboardCommand(CombatMaster plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("combatmaster.use")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.no-permission", "&cBu komutu kullanma yetkiniz yok!")));
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Leaderboard yükleniyor...");

        databaseManager.getLeaderboard(plugin.getConfigManager().getLeaderboardSize()).thenAccept(leaderboard -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                displayLeaderboard(sender, leaderboard);
            });
        }).exceptionally(throwable -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage(ChatColor.RED + "Leaderboard yüklenirken hata oluştu!");
            });
            plugin.getLogger().severe("Leaderboard hatası: " + throwable.getMessage());
            return null;
        });

        return true;
    }

    private void displayLeaderboard(CommandSender sender, List<DatabaseManager.LeaderboardEntry> leaderboard) {
        String title = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfigManager().getLeaderboardTitle());

        sender.sendMessage("");
        sender.sendMessage(title);
        sender.sendMessage("");

        if (leaderboard.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Henüz hiç combat verisi bulunmuyor.");
            sender.sendMessage("");
            return;
        }

        String format = plugin.getConfigManager().getLeaderboardFormat();

        for (DatabaseManager.LeaderboardEntry entry : leaderboard) {
            String line = format
                    .replace("{rank}", String.valueOf(entry.getRank()))
                    .replace("{player}", entry.getPlayerName())
                    .replace("{combo}", String.valueOf(entry.getBestCombo()));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }

        sender.sendMessage("");
    }
}