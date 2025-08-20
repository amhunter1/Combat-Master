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
}