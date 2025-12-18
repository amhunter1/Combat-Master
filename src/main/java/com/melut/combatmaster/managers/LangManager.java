package com.melut.combatmaster.managers;

import com.melut.combatmaster.CombatMaster;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LangManager {

    private final CombatMaster plugin;
    private final Map<String, FileConfiguration> languages;
    private String currentLanguage;

    public LangManager(CombatMaster plugin) {
        this.plugin = plugin;
        this.languages = new HashMap<>();
        this.currentLanguage = "tr"; // Varsayılan Türkçe
        loadLanguages();
    }

    private void loadLanguages() {
        loadLanguage("tr");
        loadLanguage("en");
        
        String configLang = plugin.getConfig().getString("language", "tr");
        if (languages.containsKey(configLang)) {
            this.currentLanguage = configLang;
        }
        
        plugin.getLogger().info("Dil sistemi yüklendi. Aktif dil: " + currentLanguage);
    }

    private void loadLanguage(String langCode) {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        File langFile = new File(langFolder, langCode + ".yml");
        
        if (!langFile.exists()) {
            try (InputStream inputStream = plugin.getResource("lang/" + langCode + ".yml")) {
                if (inputStream != null) {
                    Files.copy(inputStream, langFile.toPath());
                    plugin.getLogger().info("Dil dosyası oluşturuldu: " + langCode + ".yml");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Dil dosyası oluşturulamadı: " + langCode + ".yml - " + e.getMessage());
                return;
            }
        }

        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        languages.put(langCode, langConfig);
        plugin.getLogger().info("Dil yüklendi: " + langCode);
    }

    public String getMessage(String key) {
        return getMessage(key, currentLanguage);
    }

    public String getMessage(String key, String langCode) {
        FileConfiguration langConfig = languages.get(langCode);
        if (langConfig == null) {
            langConfig = languages.get("tr"); // Fallback to Turkish
        }
        
        if (langConfig == null) {
            return "&cDil dosyası bulunamadı: " + key;
        }

        String message = langConfig.getString(key);
        if (message == null) {
            // Fallback to Turkish if key not found
            if (!langCode.equals("tr") && languages.containsKey("tr")) {
                message = languages.get("tr").getString(key);
            }
            
            if (message == null) {
                return "&cMesaj bulunamadı: " + key;
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(String key, Object... replacements) {
        String message = getMessage(key);
        
        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }
        
        return message;
    }

    public void setLanguage(String langCode) {
        if (languages.containsKey(langCode)) {
            this.currentLanguage = langCode;
            plugin.getLogger().info("Dil değiştirildi: " + langCode);
        } else {
            plugin.getLogger().warning("Desteklenmeyen dil: " + langCode);
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public boolean isLanguageSupported(String langCode) {
        return languages.containsKey(langCode);
    }

    public void reloadLanguages() {
        languages.clear();
        loadLanguages();
    }

    // GUI için özel metodlar
    public String getGUITitle(String key) {
        return getMessage("gui.titles." + key);
    }

    public String getGUIItem(String key) {
        return getMessage("gui.items." + key);
    }

    public String getGUILore(String key) {
        return getMessage("gui.lore." + key);
    }

    public String getCommand(String key) {
        return getMessage("commands." + key);
    }

    public String getSystem(String key) {
        return getMessage("system." + key);
    }
}