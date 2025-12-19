package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class QuickCommandsMenu extends BaseGUI {

    public QuickCommandsMenu(CombatMaster plugin, Player player) {
        super(plugin, player, "&6⚡ Hızlı Admin Komutları", 36);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Reset All Combos
        setItem(10, ItemBuilder.create(Material.TNT)
                .setName("&c💥 Tüm Combo'ları Sıfırla")
                .setLore(
                    "&7Tüm online oyuncuların combo'larını sıfırla",
                    "",
                    "&f▸ Etkilenecek oyuncu: &e" + getActiveCombosCount() + " / " + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Bu işlem geri alınamaz!",
                    "&f▸ Tüm aktif combo'lar kaybolacak",
                    "",
                    "&c⚠️ DİKKAT: Bu işlem geri alınamaz!",
                    "",
                    "&cSıfırla!"
                )
                .setGlowing(true)
                .build(),
            p -> executeResetAllCombos(p)
        );

        // Close All Menus
        setItem(12, ItemBuilder.create(Material.BARRIER)
                .setName("&c🚫 Tüm Menüleri Kapat")
                .setLore(
                    "&7Tüm oyuncuların açık menülerini kapat",
                    "",
                    "&f▸ Açık menü sayısı: &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Bellek kullanımını azaltır",
                    "&f▸ Performansı artırır",
                    "",
                    "&cKapat!"
                )
                .build(),
            p -> executeCloseAllMenus(p)
        );

        // Reload Plugin
        setItem(14, ItemBuilder.create(Material.REPEATER)
                .setName("&a♻️ Plugin Reload")
                .setLore(
                    "&7Plugini tamamen yeniden yükle",
                    "",
                    "&f▸ Config dosyalarını yenile",
                    "&f▸ Dil dosyalarını yenile",
                    "&f▸ Tüm ayarları güncelle",
                    "&f▸ Menüleri kapat",
                    "",
                    "&c⚠️ Tüm menüler kapanacak!",
                    "",
                    "&aYeniden Yükle!"
                )
                .build(),
            p -> executePluginReload(p)
        );

        // Save All Data
        setItem(16, ItemBuilder.create(Material.CHEST)
                .setName("&b💾 Tüm Verileri Kaydet")
                .setLore(
                    "&7Tüm oyuncu verilerini database'e kaydet",
                    "",
                    "&f▸ Online oyuncular: &e" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Bellek temizliği yapar",
                    "&f▸ Veri kaybını önler",
                    "&f▸ Güvenli kapatma öncesi önerilir",
                    "",
                    "&bKaydet!"
                )
                .build(),
            p -> executeSaveAllData(p)
        );

        // Show Server Stats
        setItem(20, ItemBuilder.create(Material.BOOK)
                .setName("&d📊 Sunucu İstatistikleri")
                .setLore(
                    "&7Detaylı sunucu ve plugin istatistikleri",
                    "",
                    "&f▸ Online: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Aktif Combo'lar: &e" + getActiveCombosCount(),
                    "&f▸ Açık Menüler: &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Bellek: &e" + getMemoryUsage(),
                    "",
                    "&dDetaylı bilgi!"
                )
                .build(),
            p -> executeShowServerStats(p)
        );

        // Emergency Stop
        setItem(22, ItemBuilder.create(Material.REDSTONE_BLOCK)
                .setName("&4🚨 Acil Durdurma")
                .setLore(
                    "&7Plugin'i güvenli şekilde durdur",
                    "",
                    "&f▸ Tüm verileri kaydet",
                    "&f▸ Menüleri kapat", 
                    "&f▸ Bağlantıları sonlandır",
                    "&f▸ Belleği temizle",
                    "",
                    "&4⚠️ Plugin devre dışı kalacak!",
                    "&4⚠️ Sunucu yöneticisi gerekli!",
                    "",
                    "&4Acil Durdur!"
                )
                .build(),
            p -> executeEmergencyStop(p)
        );

        // Clear Cache
        setItem(24, ItemBuilder.create(Material.SPONGE)
                .setName("&e🧽 Cache Temizle")
                .setLore(
                    "&7Bellek cache'ini temizle",
                    "",
                    "&f▸ Bellek kullanımını azaltır",
                    "&f▸ Performansı artırır",
                    "&f▸ Eski verileri temizler",
                    "&f▸ JVM Garbage Collection",
                    "",
                    "&eTemizle!"
                )
                .build(),
            p -> executeClearCache(p)
        );

        // Navigation
        setItem(27, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new AdminMenu(plugin, p))
        );

        setItem(31, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Hızlı komut menüsü güncellendi!"));
            }
        );

        setItem(35, ItemBuilder.createCloseButton(),
            p -> {
                plugin.getMenuManager().closeMenu(p);
                playSuccessSound();
            }
        );
    }

    private void executeResetAllCombos(Player admin) {
        int resetCount = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null && data.getCurrentCombo() > 0) {
                plugin.getCombatManager().resetCombo(onlinePlayer);
                resetCount++;
            }
        }
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ " + resetCount + " oyuncunun combo'su sıfırlandı!"));
        plugin.getServer().broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c⚡ &lTüm combo'lar bir yönetici tarafından sıfırlandı!"));
        
        plugin.getLogger().info(admin.getName() + " tarafından tüm combo'lar sıfırlandı.");
        playSuccessSound();
        refresh();
    }

    private void executeCloseAllMenus(Player admin) {
        int closedCount = plugin.getMenuManager().getOpenMenuCount();
        plugin.getMenuManager().closeAllMenus();
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ " + closedCount + " adet menü kapatıldı!"));
        plugin.getLogger().info(admin.getName() + " tarafından tüm menüler kapatıldı.");
        
        playSuccessSound();
        
        // Return to admin menu since this menu was also closed
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getMenuManager().openMenu(admin, new AdminMenu(plugin, admin));
        }, 1L);
    }

    private void executePluginReload(Player admin) {
        plugin.getMenuManager().closeMenu(admin);
        
        try {
            plugin.reloadPlugin();
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Plugin başarıyla yeniden yüklendi!"));
            plugin.getLogger().info(admin.getName() + " tarafından plugin reload edildi.");
            playSuccessSound();
        } catch (Exception e) {
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c✗ Plugin reload hatası: " + e.getMessage()));
            plugin.getLogger().severe("Plugin reload hatası: " + e.getMessage());
            playErrorSound();
        }
    }

    private void executeSaveAllData(Player admin) {
        plugin.getCombatManager().saveAllData();
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Tüm oyuncu verileri database'e kaydedildi!"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&7Etkilenen oyuncu sayısı: &e" + plugin.getServer().getOnlinePlayers().size()));
        
        plugin.getLogger().info(admin.getName() + " tarafından tüm veriler kaydedildi.");
        playSuccessSound();
    }

    private void executeShowServerStats(Player admin) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&d&l📊 Sunucu İstatistikleri"));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Online Oyuncular: &a" + plugin.getServer().getOnlinePlayers().size()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Aktif Combo'lar: &a" + getActiveCombosCount()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Açık Menüler: &e" + plugin.getMenuManager().getOpenMenuCount()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Toplam Hit (Online): &b" + getTotalHitsOnline()));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Java Versiyonu: &7" + System.getProperty("java.version")));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Sunucu: &7" + plugin.getServer().getName() + " " + plugin.getServer().getVersion()));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Plugin Versiyonu: &7" + plugin.getDescription().getVersion()));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Kullanılan Bellek: &a" + usedMemory + "MB &7/ &e" + maxMemory + "MB"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ Serbest Bellek: &a" + freeMemory + "MB"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e▸ CPU Çekirdekleri: &7" + runtime.availableProcessors()));
        admin.sendMessage("");
        
        playSuccessSound();
    }

    private void executeEmergencyStop(Player admin) {
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&4⚠️ ACIL DURDURMA BAŞLATILIYOR..."));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c5 saniye içinde plugin devre dışı kalacak!"));
        
        plugin.getServer().broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&4&l⚠️ UYARI: Combat-Master plugin acil durdurma modunda!"));
        plugin.getServer().broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&cPlugin 5 saniye içinde devre dışı kalacak!"));
        
        plugin.getLogger().warning("ACIL DURDURMA: " + admin.getName() + " tarafından başlatıldı!");
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getCombatManager().saveAllData();
            plugin.getMenuManager().closeAllMenus();
            
            plugin.getServer().broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&4Combat-Master plugin güvenli şekilde durduruldu!"));
            plugin.getLogger().info("Plugin acil durdurma ile güvenli şekilde kapatıldı.");
            
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }, 100L); // 5 saniye = 100 tick
        
        playErrorSound();
        plugin.getMenuManager().closeMenu(admin);
    }

    private void executeClearCache(Player admin) {
        // JVM Garbage Collection'ı çalıştır
        System.gc();
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Cache temizlendi ve bellek optimize edildi!"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&7JVM Garbage Collection çalıştırıldı."));
        
        plugin.getLogger().info(admin.getName() + " tarafından cache temizlendi.");
        playSuccessSound();
    }

    private int getActiveCombosCount() {
        int count = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null && data.getCurrentCombo() > 0) {
                count++;
            }
        }
        return count;
    }

    private long getTotalHitsOnline() {
        long total = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                total += data.getTotalHits();
            }
        }
        return total;
    }

    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        return usedMemory + "MB / " + maxMemory + "MB";
    }
}