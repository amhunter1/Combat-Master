package com.melut.combatmaster.managers;

import com.melut.combatmaster.CombatMaster;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final CombatMaster plugin;
    private FileConfiguration config;

    public ConfigManager(CombatMaster plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }
    public boolean isSoundsEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }

    public Sound getHitSound() {
        try {
            return Sound.valueOf(config.getString("sounds.hit-sound", "ENTITY_PLAYER_ATTACK_STRONG").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz ses: " + config.getString("sounds.hit-sound"));
            return Sound.ENTITY_PLAYER_ATTACK_STRONG;
        }
    }

    public float getHitSoundVolume() {
        return (float) config.getDouble("sounds.volume", 1.0);
    }

    public float getHitSoundPitch() {
        return (float) config.getDouble("sounds.pitch", 1.0);
    }

    public SoundConfig getComboSound(int combo) {
        String level;
        if (combo <= 5) level = "low";
        else if (combo <= 15) level = "medium";
        else if (combo <= 30) level = "high";
        else level = "epic";
        
        return getSoundConfig("sounds.combo-sounds." + level);
    }

    
    public SoundConfig getResetSound() {
        return config.getBoolean("sounds.reset-sound.enabled", true) ? 
            getSoundConfig("sounds.reset-sound") : null;
    }
    
    private SoundConfig getSoundConfig(String path) {
        try {
            Sound sound = Sound.valueOf(config.getString(path + ".sound", "ENTITY_PLAYER_ATTACK_STRONG").toUpperCase());
            float volume = (float) config.getDouble(path + ".volume", 1.0);
            float pitch = (float) config.getDouble(path + ".pitch", 1.0);
            return new SoundConfig(sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz ses: " + config.getString(path + ".sound"));
            return new SoundConfig(Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
        }
    }
    
    public static class SoundConfig {
        private final Sound sound;
        private final float volume;
        private final float pitch;
        
        public SoundConfig(Sound sound, float volume, float pitch) {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
        }
        
        public Sound getSound() { return sound; }
        public float getVolume() { return volume; }
        public float getPitch() { return pitch; }
    }
    public List<String> getComboMessages() {
        return config.getStringList("combo.messages");
    }

    public int getComboMessageInterval() {
        return config.getInt("combo.message-interval", 1);
    }

    public boolean isActionBarEnabled() {
        return config.getBoolean("combo.actionbar.enabled", true);
    }

    public String getActionBarFormat() {
        return config.getString("combo.actionbar.format", "&e{combo}x &a{message}");
    }

    public int getComboTimeout() {
        return config.getInt("combo.timeout-seconds", 10);
    }
    public List<String> getEnabledWorlds() {
        return config.getStringList("enabled-worlds");
    }

    public boolean isWorldEnabled(String worldName) {
        List<String> worlds = getEnabledWorlds();
        return worlds.isEmpty() || worlds.contains(worldName);
    }

    public String getDatabaseType() {
        return config.getString("database.type", "sqlite");
    }

    public String getDatabaseHost() {
        return config.getString("database.mysql.host", "localhost");
    }

    public int getDatabasePort() {
        return config.getInt("database.mysql.port", 3306);
    }

    public String getDatabaseName() {
        return config.getString("database.mysql.database", "combatmaster");
    }

    public String getDatabaseUsername() {
        return config.getString("database.mysql.username", "root");
    }

    public String getDatabasePassword() {
        return config.getString("database.mysql.password", "");
    }
    public int getLeaderboardSize() {
        return config.getInt("leaderboard.size", 10);
    }

    public String getLeaderboardTitle() {
        return config.getString("leaderboard.title", "&6&l⚔ Combat Sıralaması ⚔");
    }

    public String getLeaderboardFormat() {
        return config.getString("leaderboard.format", "&7{rank}. &e{player} &7- &c{combo} combo");
    }

    public boolean isMobHitsEnabled() {
        return config.getBoolean("combo.mob-hits.enabled", false);
    }
    public boolean isGroundHitsEnabled() {
        return config.getBoolean("combo.ground-hits.enabled", true);
    }
    public boolean isCriticalOnlyEnabled() {
        return config.getBoolean("combo.ground-hits.critical-only", false);
    }
    public boolean isColorSystemEnabled() {
        return config.getBoolean("combo.color-system.enabled", true);
    }
    
    public String getComboColor(int combo) {
        if (!isColorSystemEnabled()) return "&e";
        
        if (combo <= 5) return config.getString("combo.color-system.colors.1-5", "&7");
        else if (combo <= 10) return config.getString("combo.color-system.colors.6-10", "&f");
        else if (combo <= 20) return config.getString("combo.color-system.colors.11-20", "&e");
        else if (combo <= 35) return config.getString("combo.color-system.colors.21-35", "&6");
        else if (combo <= 50) return config.getString("combo.color-system.colors.36-50", "&c");
        else if (combo <= 75) return config.getString("combo.color-system.colors.51-75", "&d");
        else if (combo <= 100) return config.getString("combo.color-system.colors.76-100", "&5");
        else return config.getString("combo.color-system.colors.101+", "&b");
    }
    public String getSqliteFileName() {
        return config.getString("database.sqlite.file", "combatmaster.db");
    }
    public String getMessage(String key) {
        return config.getString("messages." + key, "&cMesaj bulunamadı: " + key);
    }
}
