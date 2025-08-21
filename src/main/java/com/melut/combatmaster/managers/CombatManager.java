package com.melut.combatmaster.managers;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.database.DatabaseManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {

    private final CombatMaster plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, CombatData> playerCombos;
    private final Map<UUID, BukkitTask> timeoutTasks;

    public CombatManager(CombatMaster plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerCombos = new ConcurrentHashMap<>();
        this.timeoutTasks = new ConcurrentHashMap<>();
    }

    public void handleHit(Player player, org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        UUID playerUuid = player.getUniqueId();

        if (!plugin.getConfigManager().isWorldEnabled(player.getWorld().getName())) {
            return;
        }

        CombatData combatData = playerCombos.computeIfAbsent(playerUuid, k -> new CombatData());

        combatData.incrementTotalHits();

        combatData.incrementCombo();
        int currentCombo = combatData.getCurrentCombo();

        if (currentCombo < 2) {
            return;
        }

        // Ses
        if (plugin.getConfigManager().isSoundsEnabled()) {
            playComboSound(player, currentCombo);
        }

        // Action bar
        if (plugin.getConfigManager().isActionBarEnabled()) {
            showActionBarMessage(player, currentCombo);
        }

        resetTimeoutTask(player);

        if (currentCombo > combatData.getBestCombo()) {
            combatData.setBestCombo(currentCombo);
        }
    }

    public void handleHit(Player player) {
        handleHit(player, null);
    }

    private void playComboSound(Player player, int combo) {
        ConfigManager.SoundConfig soundConfig = plugin.getConfigManager().getComboSound(combo);
        if (soundConfig != null) {
            player.playSound(player.getLocation(), soundConfig.getSound(),
                    soundConfig.getVolume(), soundConfig.getPitch());
        }
    }

    private void showActionBarMessage(Player player, int combo) {
        List<String> messages = plugin.getConfigManager().getComboMessages();
        if (messages.isEmpty()) return;

        int messageInterval = plugin.getConfigManager().getComboMessageInterval();
        if (combo % messageInterval != 0) return;

        int messageIndex = ((combo / messageInterval) - 1) % messages.size();
        String message = messages.get(messageIndex);

        String format = formatActionBarMessage(combo, message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(format));
    }

    private String formatActionBarMessage(int combo, String message) {
        String format = plugin.getConfigManager().getActionBarFormat();
        String comboColor = plugin.getConfigManager().getComboColor(combo);

        format = format.replace("{combo}", comboColor + combo);
        format = format.replace("{message}", message);

        return ChatColor.translateAlternateColorCodes('&', format);
    }

    private void resetTimeoutTask(Player player) {
        UUID playerUuid = player.getUniqueId();

        BukkitTask oldTask = timeoutTasks.remove(playerUuid);
        if (oldTask != null && !oldTask.isCancelled()) {
            oldTask.cancel();
        }

        BukkitTask timeoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                resetCombo(player);
                timeoutTasks.remove(playerUuid);
            }
        }.runTaskLater(plugin, plugin.getConfigManager().getComboTimeout() * 20L);

        timeoutTasks.put(playerUuid, timeoutTask);
    }

    public void resetCombo(Player player) {
        UUID playerUuid = player.getUniqueId();
        CombatData combatData = playerCombos.get(playerUuid);

        if (combatData != null && combatData.getCurrentCombo() > 0) {
            int currentCombo = combatData.getCurrentCombo();

            if (currentCombo >= 2) {
                ConfigManager.SoundConfig resetSound = plugin.getConfigManager().getResetSound();
                if (resetSound != null) {
                    player.playSound(player.getLocation(), resetSound.getSound(),
                            resetSound.getVolume(), resetSound.getPitch());
                }

                savePlayerData(player, combatData);
            }

            combatData.resetCombo();

            BukkitTask task = timeoutTasks.remove(playerUuid);
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
    }

    public void loadPlayerData(UUID playerUuid, int bestCombo, int totalHits) {
        CombatData combatData = playerCombos.computeIfAbsent(playerUuid, k -> new CombatData());
        combatData.setBestCombo(bestCombo);
        combatData.setTotalHits(totalHits);
    }

    private void savePlayerData(Player player, CombatData combatData) {
        databaseManager.savePlayerData(
                player.getUniqueId(),
                player.getName(),
                combatData.getBestCombo(),
                combatData.getTotalHits()
        );
    }

    public void saveAllData() {
        for (Map.Entry<UUID, CombatData> entry : playerCombos.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                savePlayerData(player, entry.getValue());
            }
        }

        timeoutTasks.values().forEach(task -> {
            if (!task.isCancelled()) {
                task.cancel();
            }
        });
        timeoutTasks.clear();
    }

    public void reloadConfig() {
        for (Map.Entry<UUID, BukkitTask> entry : timeoutTasks.entrySet()) {
            if (!entry.getValue().isCancelled()) {
                entry.getValue().cancel();
            }

            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                resetTimeoutTask(player);
            }
        }
    }

    public CombatData getPlayerData(UUID playerUuid) {
        return playerCombos.get(playerUuid);
    }

    public void removePlayer(UUID playerUuid) {
        playerCombos.remove(playerUuid);
        BukkitTask task = timeoutTasks.remove(playerUuid);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public static class CombatData {
        private int currentCombo;
        private int bestCombo;
        private int totalHits;

        public CombatData() {
            this.currentCombo = 0;
            this.bestCombo = 0;
            this.totalHits = 0;
        }

        public void incrementCombo() {
            this.currentCombo++;
        }

        public void resetCombo() {
            this.currentCombo = 0;
        }

        public void incrementTotalHits() {
            this.totalHits++;
        }

        public int getCurrentCombo() {
            return currentCombo;
        }

        public int getBestCombo() {
            return bestCombo;
        }

        public void setBestCombo(int bestCombo) {
            this.bestCombo = bestCombo;
        }

        public int getTotalHits() {
            return totalHits;
        }

        public void setTotalHits(int totalHits) {
            this.totalHits = totalHits;
        }
    }
}