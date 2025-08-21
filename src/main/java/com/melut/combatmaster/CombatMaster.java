package com.melut.combatmaster;

import com.melut.combatmaster.commands.CombatMasterCommand;
import com.melut.combatmaster.database.DatabaseManager;
import com.melut.combatmaster.listeners.CombatListener;
import com.melut.combatmaster.managers.CombatManager;
import com.melut.combatmaster.managers.ConfigManager;
import com.melut.combatmaster.placeholders.CombatPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatMaster extends JavaPlugin {

    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private CombatManager combatManager;

    @Override
    public void onEnable() {
        getLogger().info("Combat-Master plugin başlatılıyor...");

        configManager = new ConfigManager(this);

        databaseManager = new DatabaseManager(this);
        if (!databaseManager.initialize()) {
            getLogger().severe("Database başlatılamadı! Plugin devre dışı bırakılıyor.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        combatManager = new CombatManager(this, databaseManager);

        getServer().getPluginManager().registerEvents(new CombatListener(this, combatManager), this);

        CombatMasterCommand combatMasterCommand = new CombatMasterCommand(this, configManager);
        getCommand("combatmaster").setExecutor(combatMasterCommand);
        getCommand("combatmaster").setTabCompleter(combatMasterCommand);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CombatPlaceholders(this, databaseManager).register();
            getLogger().info("PlaceholderAPI entegrasyonu başarılı!");
        }

        getLogger().info("Combat-Master plugin başarıyla yüklendi!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Combat-Master plugin kapatılıyor...");

        if (databaseManager != null) {
            databaseManager.close();
        }

        if (combatManager != null) {
            combatManager.saveAllData();
        }

        getLogger().info("Combat-Master plugin başarıyla kapatıldı!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public void reloadPlugin() {
        configManager.reloadConfig();
        combatManager.reloadConfig();
        getLogger().info("Plugin başarıyla yeniden yüklendi!");
    }
}