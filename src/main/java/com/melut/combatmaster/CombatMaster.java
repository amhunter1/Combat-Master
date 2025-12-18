package com.melut.combatmaster;

import com.melut.combatmaster.commands.CombatMasterCommand;
import com.melut.combatmaster.database.DatabaseManager;
import com.melut.combatmaster.gui.MenuListener;
import com.melut.combatmaster.gui.MenuManager;
import com.melut.combatmaster.listeners.CombatListener;
import com.melut.combatmaster.managers.CombatManager;
import com.melut.combatmaster.managers.ConfigManager;
import com.melut.combatmaster.managers.LangManager;
import com.melut.combatmaster.placeholders.CombatPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatMaster extends JavaPlugin {

    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private CombatManager combatManager;
    private MenuManager menuManager;
    private LangManager langManager;

    @Override
    public void onEnable() {
        getLogger().info("Combat-Master plugin başlatılıyor...");

        configManager = new ConfigManager(this);
        langManager = new LangManager(this);

        databaseManager = new DatabaseManager(this);
        if (!databaseManager.initialize()) {
            getLogger().severe("Database başlatılamadı! Plugin devre dışı bırakılıyor.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        combatManager = new CombatManager(this, databaseManager);

        menuManager = new MenuManager(this);

        getServer().getPluginManager().registerEvents(new CombatListener(this, combatManager), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this, menuManager), this);

        CombatMasterCommand combatMasterCommand = new CombatMasterCommand(this, configManager);
        getCommand("combatmaster").setExecutor(combatMasterCommand);
        getCommand("combatmaster").setTabCompleter(combatMasterCommand);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CombatPlaceholders(this, databaseManager).register();
            getLogger().info("PlaceholderAPI entegrasyonu başarılı!");
        }

        // bStats Metrics
        int pluginId = 28408;
        Metrics metrics = new Metrics(this, pluginId);
        getLogger().info("bStats metrics başlatıldı!");

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

        if (menuManager != null) {
            menuManager.closeAllMenus();
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

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public void reloadPlugin() {
        configManager.reloadConfig();
        langManager.reloadLanguages();
        combatManager.reloadConfig();
        if (menuManager != null) {
            menuManager.closeAllMenus();
        }
        getLogger().info("Plugin başarıyla yeniden yüklendi!");
    }
}