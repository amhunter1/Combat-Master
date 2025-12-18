package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SettingsMenu extends BaseGUI {

    public SettingsMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getGUITitle("settings"), 45);
    }

    @Override
    public void setupItems() {
        GUIUtils.fillBorders(inventory);

        // Sound Settings Section
        setItem(10, ItemBuilder.create(Material.NOTE_BLOCK)
                .setName("&e🔊 Ses Ayarları")
                .setLore(
                    "&7Combat ses efektlerini yönetin",
                    "",
                    "&f▸ Durumu: " + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cKapalı"),
                    "&f▸ Combo sesleri: " + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cDevre Dışı"),
                    "&f▸ Reset sesi: " + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cDevre Dışı"),
                    "",
                    "&7Bu ayarlar sunucu genelindedir",
                    "&7ve sadece adminler değiştirebilir",
                    "",
                    "&eBilgi için tıkla!"
                )
                .setGlowing(plugin.getConfigManager().isSoundsEnabled())
                .build(),
            p -> showSoundInfo(p)
        );

        // Visual Settings Section
        setItem(12, ItemBuilder.create(Material.ENDER_EYE)
                .setName("&d👁️ Görsel Ayarlar")
                .setLore(
                    "&7Combat görsel efektlerini görün",
                    "",
                    "&f▸ Action Bar: " + (plugin.getConfigManager().isActionBarEnabled() ? "&aAktif" : "&cKapalı"),
                    "&f▸ Renk Sistemi: " + (plugin.getConfigManager().isColorSystemEnabled() ? "&aAktif" : "&cKapalı"),
                    "&f▸ Combo Mesajları: &a" + plugin.getConfigManager().getComboMessages().size() + " mesaj",
                    "&f▸ Mesaj Aralığı: &e" + plugin.getConfigManager().getComboMessageInterval() + " hit",
                    "",
                    "&7Bu ayarlar sunucu genelindedir",
                    "",
                    "&dDetaylar için tıkla!"
                )
                .setGlowing(plugin.getConfigManager().isActionBarEnabled())
                .build(),
            p -> showVisualInfo(p)
        );

        // Combat Settings Section
        setItem(14, ItemBuilder.create(Material.DIAMOND_SWORD)
                .setName("&c⚔️ Combat Ayarları")
                .setLore(
                    "&7Combat sistemi ayarlarını görün",
                    "",
                    "&f▸ Combo Timeout: &e" + plugin.getConfigManager().getComboTimeout() + " saniye",
                    "&f▸ Mob Vuruşları: " + (plugin.getConfigManager().isMobHitsEnabled() ? "&aAktif" : "&cKapalı"),
                    "&f▸ Yerden Vuruş: " + (plugin.getConfigManager().isGroundHitsEnabled() ? "&aAktif" : "&cKapalı"),
                    "&f▸ Sadece Kritik: " + (plugin.getConfigManager().isCriticalOnlyEnabled() ? "&aAktif" : "&cKapalı"),
                    "",
                    "&7Bu ayarlar sunucu genelindedir",
                    "",
                    "&cBilgi için tıkla!"
                )
                .build(),
            p -> showCombatInfo(p)
        );

        // World Settings Section
        setItem(16, ItemBuilder.create(Material.GRASS_BLOCK)
                .setName("&2🌍 Dünya Ayarları")
                .setLore(
                    "&7Combat sisteminin aktif olduğu dünyalar",
                    "",
                    "&f▸ Aktif Dünyalar: " + 
                    (plugin.getConfigManager().getEnabledWorlds().isEmpty() ? "&aTümü" : 
                    "&e" + plugin.getConfigManager().getEnabledWorlds().size() + " dünya"),
                    "",
                    plugin.getConfigManager().getEnabledWorlds().isEmpty() ? 
                    "&aTüm dünyalarda combat sistemi aktif" :
                    "&7Sadece seçili dünyalarda aktif",
                    "",
                    "&2Liste için tıkla!"
                )
                .build(),
            p -> showWorldInfo(p)
        );

        // Language Settings Section
        setItem(28, ItemBuilder.create(Material.WRITABLE_BOOK)
                .setName("&6🌐 Dil Ayarları")
                .setLore(
                    "&7Plugin dil ayarlarını görün",
                    "",
                    "&f▸ Mevcut Dil: &e" + plugin.getLangManager().getCurrentLanguage().toUpperCase(),
                    "&f▸ Desteklenen Diller: &aTR, EN",
                    "&f▸ Dil Dosyası: &7lang/" + plugin.getLangManager().getCurrentLanguage() + ".yml",
                    "",
                    "&7Dil değişimi için admin yetkisi gereklidir",
                    "&7Config dosyasından değiştirebilirsiniz",
                    "",
                    "&6Bilgi için tıkla!"
                )
                .setGlowing(true)
                .build(),
            p -> showLanguageInfo(p)
        );

        // Database Settings Section
        setItem(30, ItemBuilder.create(Material.CHEST)
                .setName("&9💾 Database Ayarları")
                .setLore(
                    "&7Veri saklama ayarları",
                    "",
                    "&f▸ Database Türü: &e" + plugin.getConfigManager().getDatabaseType().toUpperCase(),
                    "&f▸ Leaderboard Boyutu: &e" + plugin.getConfigManager().getLeaderboardSize(),
                    "&f▸ Otomatik Kayıt: &aAktif",
                    "",
                    plugin.getConfigManager().getDatabaseType().equals("sqlite") ?
                    "&f▸ Dosya: &7" + plugin.getConfigManager().getSqliteFileName() :
                    "&f▸ Host: &7" + plugin.getConfigManager().getDatabaseHost(),
                    "",
                    "&9Detaylar için tıkla!"
                )
                .build(),
            p -> showDatabaseInfo(p)
        );

        // Performance Settings Section
        setItem(32, ItemBuilder.create(Material.REDSTONE)
                .setName("&c⚡ Performans Bilgileri")
                .setLore(
                    "&7Plugin performans durumu",
                    "",
                    "&f▸ Açık Menüler: &a" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Bellek Kullanımı: &e" + getMemoryUsage(),
                    "&f▸ Java Versiyonu: &7" + System.getProperty("java.version"),
                    "",
                    "&7Plugin optimize edilmiş durumdadır",
                    "",
                    "&cSistem bilgileri!"
                )
                .build(),
            p -> showPerformanceInfo(p)
        );

        // Plugin Info Section
        setItem(34, ItemBuilder.create(Material.BEACON)
                .setName("&b ℹ️ Plugin Bilgileri")
                .setLore(
                    "&7Combat-Master hakkında",
                    "",
                    "&f▸ Versiyon: &e" + plugin.getDescription().getVersion(),
                    "&f▸ Geliştirici: &eMelut",
                    "&f▸ API Versiyonu: &7" + plugin.getDescription().getAPIVersion(),
                    "&f▸ bStats ID: &728408",
                    "",
                    "&a▸ Discord: &7discord.com/users/871721944268038175",
                    "&a▸ GitHub: &7github.com/amhunter1",
                    "",
                    "&bDetaylar için tıkla!"
                )
                .build(),
            p -> showPluginInfo(p)
        );

        // Navigation Buttons
        setItem(36, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new MainMenu(plugin, p))
        );

        setItem(40, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage("&a✓ Ayarlar menüsü güncellendi!");
            }
        );

        setItem(44, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private void showSoundInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&e&l🔊 Ses Ayarları Detayları");
        player.sendMessage("");
        player.sendMessage("&f▸ Ana Ses Sistemi: " + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cKapalı"));
        player.sendMessage("&f▸ Reset Sesi: " + (plugin.getConfigManager().getResetSound() != null ? "&aAktif" : "&cKapalı"));
        player.sendMessage("");
        player.sendMessage("&7Combo Seviye Sesleri:");
        player.sendMessage("&f▸ Düşük (1-5): &7ENTITY_PLAYER_ATTACK_STRONG");
        player.sendMessage("&f▸ Orta (6-15): &7ENTITY_EXPERIENCE_ORB_PICKUP");
        player.sendMessage("&f▸ Yüksek (16-30): &7ENTITY_PLAYER_LEVELUP");
        player.sendMessage("&f▸ Efsane (31+): &7ENTITY_ENDER_DRAGON_GROWL");
        player.sendMessage("");
        player.sendMessage("&7Bu ayarlar config.yml dosyasından değiştirilebilir.");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showVisualInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&d&l👁️ Görsel Ayarları Detayları");
        player.sendMessage("");
        player.sendMessage("&f▸ Action Bar: " + (plugin.getConfigManager().isActionBarEnabled() ? "&aAktif" : "&cKapalı"));
        player.sendMessage("&f▸ Action Bar Format: &7" + plugin.getConfigManager().getActionBarFormat());
        player.sendMessage("&f▸ Renk Sistemi: " + (plugin.getConfigManager().isColorSystemEnabled() ? "&aAktif" : "&cKapalı"));
        player.sendMessage("&f▸ Mesaj Sayısı: &e" + plugin.getConfigManager().getComboMessages().size());
        player.sendMessage("&f▸ Mesaj Aralığı: &eHer " + plugin.getConfigManager().getComboMessageInterval() + " hit'te");
        player.sendMessage("");
        player.sendMessage("&7Renk Seviyeleri:");
        player.sendMessage("&7▸ 1-5: &7Gri &f| 6-10: &fBeyaz &f| 11-20: &eSarı");
        player.sendMessage("&6▸ 21-35: &6Turuncu &f| 36-50: &cKırmızı &f| 51-75: &dPembe");
        player.sendMessage("&5▸ 76-100: &5Mor &f| 101+: &bAçık Mavi");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showCombatInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&c&l⚔️ Combat Ayarları Detayları");
        player.sendMessage("");
        player.sendMessage("&f▸ Combo Timeout: &e" + plugin.getConfigManager().getComboTimeout() + " saniye");
        player.sendMessage("&f▸ Mob Vuruşları: " + (plugin.getConfigManager().isMobHitsEnabled() ? "&aAktif (Sayılır)" : "&cKapalı (Sayılmaz)"));
        player.sendMessage("&f▸ Yerden Vuruş: " + (plugin.getConfigManager().isGroundHitsEnabled() ? "&aAktif" : "&cKapalı"));
        player.sendMessage("&f▸ Sadece Kritik: " + (plugin.getConfigManager().isCriticalOnlyEnabled() ? "&aAktif" : "&cKapalı"));
        player.sendMessage("");
        player.sendMessage("&7Combat Kuralları:");
        player.sendMessage("&f▸ Kendine vurmak combo'yu sıfırlamaz");
        player.sendMessage("&f▸ Düşme, boğulma hasarı combo'yu sıfırlar");
        player.sendMessage("&f▸ Ok ve diğer projektiller sayılır");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showWorldInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&2&l🌍 Dünya Ayarları Detayları");
        player.sendMessage("");
        if (plugin.getConfigManager().getEnabledWorlds().isEmpty()) {
            player.sendMessage("&f▸ Durum: &aTüm dünyalarda aktif");
            player.sendMessage("&f▸ Combat sistemi her dünyada çalışmaktadır");
        } else {
            player.sendMessage("&f▸ Aktif Dünyalar (&e" + plugin.getConfigManager().getEnabledWorlds().size() + "&f):");
            for (String world : plugin.getConfigManager().getEnabledWorlds()) {
                boolean isLoaded = plugin.getServer().getWorld(world) != null;
                player.sendMessage("&f  ▸ " + world + (isLoaded ? " &a(Yüklü)" : " &c(Yüklü Değil)"));
            }
        }
        player.sendMessage("");
        player.sendMessage("&7Bu ayar config.yml -> enabled-worlds bölümünden değiştirilir.");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showLanguageInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&6&l🌐 Dil Ayarları Detayları");
        player.sendMessage("");
        player.sendMessage("&f▸ Mevcut Dil: &e" + plugin.getLangManager().getCurrentLanguage().toUpperCase());
        player.sendMessage("&f▸ Desteklenen Diller: &aTR (Türkçe), EN (English)");
        player.sendMessage("&f▸ Dil Dosyası: &7plugins/Combat-Master/lang/" + plugin.getLangManager().getCurrentLanguage() + ".yml");
        player.sendMessage("");
        player.sendMessage("&7Dil Değiştirme:");
        player.sendMessage("&f▸ config.yml dosyasında 'language: tr' veya 'language: en'");
        player.sendMessage("&f▸ Değişiklikten sonra '/combatmaster reload' komutu");
        player.sendMessage("&f▸ Tüm mesajlar ve menüler yeni dilde görünecek");
        player.sendMessage("");
        player.sendMessage("&a▸ Kendi dilinizi eklemek için lang/ klasörüne yeni dosya");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showDatabaseInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&9&l💾 Database Ayarları Detayları");
        player.sendMessage("");
        player.sendMessage("&f▸ Database Türü: &e" + plugin.getConfigManager().getDatabaseType().toUpperCase());
        
        if (plugin.getConfigManager().getDatabaseType().equals("sqlite")) {
            player.sendMessage("&f▸ SQLite Dosyası: &7" + plugin.getConfigManager().getSqliteFileName());
            player.sendMessage("&f▸ Konum: &7plugins/Combat-Master/" + plugin.getConfigManager().getSqliteFileName());
        } else {
            player.sendMessage("&f▸ MySQL Host: &7" + plugin.getConfigManager().getDatabaseHost());
            player.sendMessage("&f▸ Port: &7" + plugin.getConfigManager().getDatabasePort());
            player.sendMessage("&f▸ Database: &7" + plugin.getConfigManager().getDatabaseName());
        }
        
        player.sendMessage("&f▸ Leaderboard Boyutu: &e" + plugin.getConfigManager().getLeaderboardSize());
        player.sendMessage("&f▸ Otomatik Kayıt: &aAktif");
        player.sendMessage("&f▸ Connection Pool: &7HikariCP");
        player.sendMessage("");
        player.sendMessage("&7Veriler güvenle saklanmakta ve otomatik olarak kaydedilmektedir.");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showPerformanceInfo(Player player) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;

        player.sendMessage("");
        player.sendMessage("&c&l⚡ Performans Bilgileri");
        player.sendMessage("");
        player.sendMessage("&f▸ Plugin Durumu: &aÇalışıyor");
        player.sendMessage("&f▸ Açık Menüler: &e" + plugin.getMenuManager().getOpenMenuCount());
        player.sendMessage("&f▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size());
        player.sendMessage("");
        player.sendMessage("&f▸ Java Versiyonu: &7" + System.getProperty("java.version"));
        player.sendMessage("&f▸ Kullanılan Bellek: &e" + usedMemory + "MB &7/ &e" + maxMemory + "MB");
        player.sendMessage("&f▸ Serbest Bellek: &a" + freeMemory + "MB");
        player.sendMessage("&f▸ CPU Çekirdekleri: &7" + runtime.availableProcessors());
        player.sendMessage("");
        player.sendMessage("&aPlugin optimize edilmiştir ve performans etkisi minimumdur.");
        player.sendMessage("");
        playSuccessSound();
    }

    private void showPluginInfo(Player player) {
        player.sendMessage("");
        player.sendMessage("&b&l ℹ️ Combat-Master Plugin Bilgileri");
        player.sendMessage("");
        player.sendMessage("&f▸ Plugin Adı: &eCombat-Master");
        player.sendMessage("&f▸ Versiyon: &e" + plugin.getDescription().getVersion());
        player.sendMessage("&f▸ Geliştirici: &eMelut");
        player.sendMessage("&f▸ API Versiyonu: &7" + plugin.getDescription().getAPIVersion());
        player.sendMessage("&f▸ Açıklama: &7" + plugin.getDescription().getDescription());
        player.sendMessage("");
        player.sendMessage("&f▸ bStats Plugin ID: &728408");
        player.sendMessage("&f▸ İstatistikler: &ahttps://bstats.org/plugin/bukkit/Combat-Master");
        player.sendMessage("");
        player.sendMessage("&a▸ Discord: &fhttps://discord.com/users/871721944268038175");
        player.sendMessage("&a▸ GitHub: &fhttps://github.com/amhunter1");
        player.sendMessage("&a▸ Destek: &fDiscord üzerinden ulaşabilirsiniz");
        player.sendMessage("");
        player.sendMessage("&eTeşekkürler! Combat-Master'ı kullandığınız için. &c❤");
        player.sendMessage("");
        playSuccessSound();
    }

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        return usedMemory + "MB / " + maxMemory + "MB";
    }
}