package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminMenu extends BaseGUI {

    public AdminMenu(CombatMaster plugin, Player player) {
        super(plugin, player, plugin.getLangManager().getGUITitle("admin"), 45);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Plugin Management Section
        setItem(10, ItemBuilder.create(Material.REDSTONE_TORCH)
                .setName("&c🔄 Plugin Yönetimi")
                .setLore(
                    "&7Plugin kontrolü ve yönetimi",
                    "",
                    "&f▸ Plugin bilgileri",
                    "&f▸ Versiyon: &e" + plugin.getDescription().getVersion(),
                    "&f▸ Aktif menüler: &a" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Dil: &e" + plugin.getLangManager().getCurrentLanguage(),
                    "",
                    "&eBilgiler için tıkla!"
                )
                .setGlowing(true)
                .build(),
            p -> showPluginInfo(p)
        );

        setItem(12, ItemBuilder.create(Material.REPEATER)
                .setName("&a♻️ Reload Plugin")
                .setLore(
                    "&7Plugini yeniden yükle",
                    "",
                    "&f▸ Config dosyalarını yenile",
                    "&f▸ Dil dosyalarını yenile", 
                    "&f▸ Tüm ayarları güncelle",
                    "",
                    "&c⚠️ Dikkat: Tüm menüler kapanacak!",
                    "",
                    "&aYeniden yükle!"
                )
                .build(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                try {
                    plugin.reloadPlugin();
                    p.sendMessage(plugin.getLangManager().getMessage("commands.plugin_reloaded"));
                    playSuccessSound();
                } catch (Exception e) {
                    p.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&cReload hatası: " + e.getMessage()));
                    playErrorSound();
                }
            }
        );

        setItem(14, ItemBuilder.create(Material.COMPARATOR)
                .setName("&d⚙️ Database Yönetimi")
                .setLore(
                    "&7Database durumu ve istatistikleri",
                    "",
                    "&f▸ Database türü: &e" + plugin.getConfigManager().getDatabaseType(),
                    "&f▸ Bağlantı durumu: &aAktif",
                    "&f▸ İstatistikler",
                    "",
                    "&dDetaylar için tıkla!"
                )
                .build(),
            p -> showDatabaseInfo(p)
        );

        // Player Management Section  
        setItem(16, ItemBuilder.create(Material.PLAYER_HEAD)
                .setName("&e👥 Oyuncu Yönetimi")
                .setLore(
                    "&7Online oyuncu yönetimi",
                    "",
                    "&f▸ Online oyuncular: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Oyuncu istatistikleri",
                    "&f▸ Combo reset işlemleri",
                    "",
                    "&eYönetim paneli!"
                )
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new PlayerManagementMenu(plugin, p))
        );

        // System Information
        setItem(28, ItemBuilder.create(Material.BOOK)
                .setName("&b📊 Sistem Bilgileri")
                .setLore(
                    "&7Sunucu ve sistem durumu",
                    "",
                    "&f▸ Java Versiyonu: &e" + System.getProperty("java.version"),
                    "&f▸ Sunucu: &e" + plugin.getServer().getName() + " " + plugin.getServer().getVersion(),
                    "&f▸ Plugin Klasörü: &7plugins/Combat-Master/",
                    "&f▸ Bellek Kullanımı: &a" + getMemoryUsage(),
                    "",
                    "&bDetaylı bilgi için tıkla!"
                )
                .build(),
            p -> showSystemInfo(p)
        );

        setItem(30, ItemBuilder.create(Material.COMMAND_BLOCK)
                .setName("&6⚡ Hızlı Komutlar")
                .setLore(
                    "&7Sık kullanılan admin komutları",
                    "",
                    "&f▸ Tüm combo'ları sıfırla",
                    "&f▸ Leaderboard'u temizle",  
                    "&f▸ Cache'i temizle",
                    "",
                    "&6Komut menüsü!"
                )
                .build(),
            p -> plugin.getMenuManager().openMenu(p, new QuickCommandsMenu(plugin, p))
        );

        setItem(32, ItemBuilder.create(Material.WRITABLE_BOOK)
                .setName("&c📝 Config Düzenleyici")
                .setLore(
                    "&7Config ayarlarını görüntüle",
                    "",
                    "&f▸ Mevcut config değerleri",
                    "&f▸ Dil ayarları",
                    "&f▸ Database ayarları",
                    "&f▸ Combat ayarları", 
                    "",
                    "&cConfig bilgileri!"
                )
                .build(),
            p -> showConfigInfo(p)
        );

        setItem(34, ItemBuilder.create(Material.BEACON)
                .setName("&a🎯 bStats Durumu")
                .setLore(
                    "&7Metrics ve istatistik durumu",
                    "",
                    "&f▸ bStats Plugin ID: &e28408",
                    "&f▸ Veri toplama: &aAktif",
                    "&f▸ Gizlilik: &7Anonim veriler",
                    "&f▸ URL: &ebstats.org/plugin/bukkit/Combat-Master",
                    "",
                    "&7İstatistikler otomatik olarak gönderiliyor"
                )
                .build()
        );

        // Navigation Buttons
        setItem(36, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new MainMenu(plugin, p))
        );

        setItem(40, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Admin panel güncellendi!"));
            }
        );

        setItem(44, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private void showPluginInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&6&l⚡ Combat-Master Admin Bilgileri ⚡"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Plugin: &fCombat-Master v" + plugin.getDescription().getVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Geliştirici: &fMelut"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ API Versiyonu: &f" + plugin.getDescription().getAPIVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dil: &f" + plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Açık Menüler: &a" + plugin.getMenuManager().getOpenMenuCount()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size()));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a▸ Discord: &fhttps://discord.com/users/871721944268038175"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a▸ GitHub: &fhttps://github.com/amhunter1"));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showDatabaseInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&d&l📊 Database Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database Türü: &f" + plugin.getConfigManager().getDatabaseType().toUpperCase()));
        if (plugin.getConfigManager().getDatabaseType().equals("mysql")) {
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Host: &f" + plugin.getConfigManager().getDatabaseHost()));
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Port: &f" + plugin.getConfigManager().getDatabasePort()));
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database: &f" + plugin.getConfigManager().getDatabaseName()));
        } else {
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dosya: &f" + plugin.getConfigManager().getSqliteFileName()));
        }
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bağlantı: &aAktif"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Pool Boyutu: &f10 (max)"));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showSystemInfo(Player player) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;

        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&b&l📊 Sistem Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Java Versiyonu: &f" + System.getProperty("java.version")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ OS: &f" + System.getProperty("os.name")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sunucu: &f" + plugin.getServer().getName()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Versiyon: &f" + plugin.getServer().getVersion()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bukkit: &f" + plugin.getServer().getBukkitVersion()));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Bellek Kullanımı: &a" + usedMemory + "MB &7/ &e" + maxMemory + "MB"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Serbest Bellek: &a" + freeMemory + "MB"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ CPU Çekirdekleri: &f" + runtime.availableProcessors()));
        player.sendMessage("");
        playSuccessSound();
    }

    private void showConfigInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c&l📝 Config Bilgileri"));
        player.sendMessage("");
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Dil: &f" + plugin.getLangManager().getCurrentLanguage()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sesler: &f" + (plugin.getConfigManager().isSoundsEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Action Bar: &f" + (plugin.getConfigManager().isActionBarEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Combo Timeout: &f" + plugin.getConfigManager().getComboTimeout() + " saniye"));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Mob Hits: &f" + (plugin.getConfigManager().isMobHitsEnabled() ? "&aAktif" : "&cKapalı")));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Leaderboard Boyutu: &f" + plugin.getConfigManager().getLeaderboardSize()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Database: &f" + plugin.getConfigManager().getDatabaseType().toUpperCase()));
        player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Aktif Dünyalar: &f" +
            (plugin.getConfigManager().getEnabledWorlds().isEmpty() ? "Tümü" :
            String.join(", ", plugin.getConfigManager().getEnabledWorlds()))));
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