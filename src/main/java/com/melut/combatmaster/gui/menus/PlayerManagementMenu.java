package com.melut.combatmaster.gui.menus;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.BaseGUI;
import com.melut.combatmaster.gui.utils.GUIUtils;
import com.melut.combatmaster.gui.utils.ItemBuilder;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerManagementMenu extends BaseGUI {

    public PlayerManagementMenu(CombatMaster plugin, Player player) {
        super(plugin, player, "&c👥 Oyuncu Yönetimi", 54);
    }

    @Override
    public void setupItems() {
        if (!player.hasPermission("combatmaster.admin")) {
            plugin.getMenuManager().closeMenu(player);
            player.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
            return;
        }

        GUIUtils.fillBorders(inventory);

        // Online Players Section
        int slot = 10;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (slot > 43) break; // Prevent overflow
            
            CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            int currentCombo = playerData != null ? playerData.getCurrentCombo() : 0;
            int bestCombo = playerData != null ? playerData.getBestCombo() : 0;
            int totalHits = playerData != null ? playerData.getTotalHits() : 0;

            boolean hasActiveCombo = currentCombo > 0;
            Material headMaterial = hasActiveCombo ? Material.PLAYER_HEAD : Material.SKELETON_SKULL;

            setItem(slot, ItemBuilder.create(headMaterial)
                    .setName("&e" + onlinePlayer.getName())
                    .setLore(
                        "&7Oyuncu yönetim seçenekleri",
                        "",
                        "&f▸ Mevcut Combo: " + (hasActiveCombo ? "&a" + currentCombo : "&7Yok"),
                        "&f▸ En İyi Combo: &6" + bestCombo,
                        "&f▸ Toplam Hit: &e" + totalHits,
                        "&f▸ Dünya: &7" + onlinePlayer.getWorld().getName(),
                        "&f▸ Sağlık: &c" + Math.round(onlinePlayer.getHealth()) + "/20",
                        "",
                        "&aYönetim için tıkla!"
                    )
                    .setGlowing(hasActiveCombo)
                    .build(),
                p -> openPlayerActions(p, onlinePlayer)
            );
            
            slot++;
            // Skip border slots
            if (slot % 9 == 8) slot += 2;
        }

        // Management Tools
        setItem(45, ItemBuilder.create(Material.TNT)
                .setName("&c💥 Tüm Combo'ları Sıfırla")
                .setLore(
                    "&7Tüm oyuncuların combo'larını sıfırla",
                    "",
                    "&f▸ Online oyuncular: &e" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Bu işlem geri alınamaz!",
                    "",
                    "&c⚠️ DİKKAT: Tüm aktif combo'lar silinecek!",
                    "",
                    "&cSıfırlama için tıkla!"
                )
                .build(),
            p -> resetAllCombos(p)
        );

        setItem(46, ItemBuilder.create(Material.CLOCK)
                .setName("&d📊 Toplu İstatistik")
                .setLore(
                    "&7Tüm oyuncuların genel istatistikleri",
                    "",
                    "&f▸ Online: &a" + plugin.getServer().getOnlinePlayers().size(),
                    "&f▸ Aktif Combo Sayısı: &e" + getActiveCombosCount(),
                    "&f▸ Toplam Hit (Online): &b" + getTotalHitsOnline(),
                    "",
                    "&dDetaylar için tıkla!"
                )
                .build(),
            p -> showBulkStats(p)
        );

        setItem(47, ItemBuilder.create(Material.BARRIER)
                .setName("&c🚫 Tüm Menüleri Kapat")
                .setLore(
                    "&7Tüm oyuncuların açık menülerini kapat",
                    "",
                    "&f▸ Açık menü sayısı: &e" + plugin.getMenuManager().getOpenMenuCount(),
                    "&f▸ Bu işlem tüm GUI'ları kapatacak",
                    "",
                    "&cKapatma için tıkla!"
                )
                .build(),
            p -> {
                int closedCount = plugin.getMenuManager().getOpenMenuCount();
                plugin.getMenuManager().closeAllMenus();
                p.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ " + closedCount + " adet menü kapatıldı!"));
                playSuccessSound();
                refresh();
            }
        );

        // Navigation
        setItem(49, ItemBuilder.createBackButton(),
            p -> plugin.getMenuManager().openMenu(p, new AdminMenu(plugin, p))
        );

        setItem(53, ItemBuilder.createRefreshButton(),
            p -> {
                refresh();
                playSuccessSound();
                p.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ Oyuncu listesi güncellendi!"));
            }
        );
    }

    private void openPlayerActions(Player admin, Player target) {
        admin.closeInventory();
        
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&e&l👤 " + target.getName() + " Yönetim Seçenekleri"));
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a1. &f/combatmaster reset " + target.getName() + " &7- Combo'sunu sıfırla"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a2. &f/combatmaster stats " + target.getName() + " &7- İstatistikleri göster"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a3. &f/tp " + target.getName() + " &7- Oyuncuya ışınlan"));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a4. &f/tp " + admin.getName() + " " + target.getName() + " &7- Oyuncuyu yanına çek"));
        admin.sendMessage("");
        
        CombatManager.CombatData playerData = plugin.getCombatManager().getPlayerData(target.getUniqueId());
        if (playerData != null) {
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f📊 &7Detaylı İstatistikler:"));
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ Mevcut Combo: &e" + playerData.getCurrentCombo()));
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ En İyi Combo: &6" + playerData.getBestCombo()));
            admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ Toplam Hit: &b" + playerData.getTotalHits()));
        }
        
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&7Menüye dönmek için &e/combatmaster gui &7yazabilirsiniz."));
        admin.sendMessage("");
        
        playSuccessSound();
    }

    private void resetAllCombos(Player admin) {
        int resetCount = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null && data.getCurrentCombo() > 0) {
                plugin.getCombatManager().resetCombo(onlinePlayer);
                resetCount++;
            }
        }
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&a✓ " + resetCount + " oyuncunun combo'su sıfırlandı!"));
        plugin.getServer().broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c⚡ Tüm combo'lar bir yönetici tarafından sıfırlandı!"));
        
        playSuccessSound();
        refresh();
    }

    private void showBulkStats(Player admin) {
        admin.sendMessage("");
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&d&l📊 Toplu Oyuncu İstatistikleri"));
        admin.sendMessage("");
        
        int onlineCount = plugin.getServer().getOnlinePlayers().size();
        int activeCombos = getActiveCombosCount();
        long totalHits = getTotalHitsOnline();
        int maxCombo = getMaxComboOnline();
        String topPlayer = getTopPlayerOnline();
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ Online Oyuncular: &a" + onlineCount));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ Aktif Combo'lar: &e" + activeCombos));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ Toplam Hit (Online): &b" + GUIUtils.formatNumber(totalHits)));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ En Yüksek Combo: &6" + maxCombo));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&f▸ En İyi Oyuncu: &e" + (topPlayer != null ? topPlayer : "Yok")));
        admin.sendMessage("");
        
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&7Bu istatistikler sadece online oyuncuları kapsamaktadır."));
        admin.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&7Tam istatistikler için database'e bakınız."));
        admin.sendMessage("");
        
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

    private int getMaxComboOnline() {
        int max = 0;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                max = Math.max(max, Math.max(data.getCurrentCombo(), data.getBestCombo()));
            }
        }
        return max;
    }

    private String getTopPlayerOnline() {
        String topPlayer = null;
        int maxCombo = 0;
        
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CombatManager.CombatData data = plugin.getCombatManager().getPlayerData(onlinePlayer.getUniqueId());
            if (data != null) {
                int bestCombo = data.getBestCombo();
                if (bestCombo > maxCombo) {
                    maxCombo = bestCombo;
                    topPlayer = onlinePlayer.getName();
                }
            }
        }
        
        return topPlayer;
    }
}