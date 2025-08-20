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

    public void handleHit(Player player) {
        UUID playerUuid = player.getUniqueId();

        // Dünya kontrolü
        if (!plugin.getConfigManager().isWorldEnabled(player.getWorld().getName())) {
            return;
        }

        CombatData combatData = playerCombos.computeIfAbsent(playerUuid, k -> new CombatData());

        // Combo artır
        combatData.incrementCombo();
        combatData.incrementTotalHits();

        int currentCombo = combatData.getCurrentCombo();

        // Gelişmiş ses sistemi
        if (plugin.getConfigManager().isSoundsEnabled()) {
            playComboSound(player, currentCombo);
        }

        // Action bar mesajı göster
        if (plugin.getConfigManager().isActionBarEnabled()) {
            showActionBarMessage(player, currentCombo);
        }

        // Timeout task'ını resetle
        resetTimeoutTask(player);

        // Best combo güncelle
        if (currentCombo > combatData.getBestCombo()) {
            combatData.setBestCombo(currentCombo);
        }
    }

    public void handleMiss(Player player) {
        if (plugin.getConfigManager().isMissSystemEnabled()) {
            // Miss sesi çal
            ConfigManager.SoundConfig missSound = plugin.getConfigManager().getMissSound();
            if (missSound != null) {
                player.playSound(player.getLocation(), missSound.getSound(), 
                    missSound.getVolume(), missSound.getPitch());
            }
            
            // Miss'te combo sıfırlansın mı?
            if (plugin.getConfigManager().shouldResetOnMiss()) {
                resetCombo(player);
            }
        }
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

        // Döngüsel mesaj seçimi
        int messageIndex = ((combo / messageInterval) - 1) % messages.size();
        String message = messages.get(messageIndex);

        // Gelişmiş format sistemi
        String format = formatActionBarMessage(combo, message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(format));
    }
    
    private String formatActionBarMessage(int combo, String message) {
        String format = plugin.getConfigManager().getActionBarFormat();
        String comboColor = plugin.getConfigManager().getComboColor(combo);
        
        // Placeholder'ları değiştir
        format = format.replace("{combo}", comboColor + combo);
        format = format.replace("{message}", message);
        
        // Renk kodlarını çevir
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    private void resetTimeoutTask(Player player) {
        UUID playerUuid = player.getUniqueId();

        // Eski task'ı iptal et
        BukkitTask oldTask = timeoutTasks.remove(playerUuid);
        if (oldTask != null && !oldTask.isCancelled()) {
            oldTask.cancel();
        }

        // Yeni timeout task başlat
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
            // Reset sesi çal
            ConfigManager.SoundConfig resetSound = plugin.getConfigManager().getResetSound();
            if (resetSound != null) {
                player.playSound(player.getLocation(), resetSound.getSound(), 
                    resetSound.getVolume(), resetSound.getPitch());
            }
            
            // Verileri kaydet
            savePlayerData(player, combatData);

            // Combo'yu sıfırla
            combatData.resetCombo();

            // Timeout task'ını iptal et
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

        // Tüm timeout task'larını iptal et
        timeoutTasks.values().forEach(task -> {
            if (!task.isCancelled()) {
                task.cancel();
            }
        });
        timeoutTasks.clear();
    }

    public void reloadConfig() {
        // Mevcut task'ları iptal et ve yeniden başlat
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

    // Combat verisi sınıfı
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