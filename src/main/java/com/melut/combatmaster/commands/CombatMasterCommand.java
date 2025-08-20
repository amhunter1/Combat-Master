package com.melut.combatmaster.commands;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.managers.ConfigManager;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatMasterCommand implements CommandExecutor, TabCompleter {

    private final CombatMaster plugin;
    private final ConfigManager configManager;

    public CombatMasterCommand(CombatMaster plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("combatmaster.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.no-permission", "&cBu komutu kullanma yetkiniz yok!")));
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;

            case "stats":
                handleStats(sender, args);
                break;

            case "reset":
                handleReset(sender, args);
                break;

            case "version":
            case "info":
                handleInfo(sender);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Combat-Master Komutları ===");
        sender.sendMessage(ChatColor.YELLOW + "/combatmaster reload" + ChatColor.WHITE + " - Plugini yeniden yükle");
        sender.sendMessage(ChatColor.YELLOW + "/combatmaster stats [oyuncu]" + ChatColor.WHITE + " - Oyuncu istatistiklerini göster");
        sender.sendMessage(ChatColor.YELLOW + "/combatmaster reset [oyuncu]" + ChatColor.WHITE + " - Oyuncu combo'sunu sıfırla");
        sender.sendMessage(ChatColor.YELLOW + "/combatmaster info" + ChatColor.WHITE + " - Plugin bilgileri");
        sender.sendMessage(ChatColor.YELLOW + "/leaderboard" + ChatColor.WHITE + " - Sıralamayı göster");
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.plugin-reloaded", "&aPlugin başarıyla yeniden yüklendi!")));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Plugin yeniden yüklenirken hata oluştu: " + e.getMessage());
            plugin.getLogger().severe("Reload hatası: " + e.getMessage());
        }
    }

    private void handleStats(CommandSender sender, String[] args) {
        if (args.length < 2) {
            if (sender instanceof Player) {
                showPlayerStats(sender, (Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Konsol için oyuncu adı belirtmelisiniz!");
            }
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.player-not-found", "&cOyuncu bulunamadı!")));
            return;
        }

        showPlayerStats(sender, target);
    }

    private void showPlayerStats(CommandSender sender, Player target) {
        CombatManager.CombatData combatData = plugin.getCombatManager().getPlayerData(target.getUniqueId());

        sender.sendMessage(ChatColor.GOLD + "=== " + target.getName() + " Combat İstatistikleri ===");

        if (combatData != null) {
            sender.sendMessage(ChatColor.YELLOW + "Mevcut Combo: " + ChatColor.WHITE + combatData.getCurrentCombo());
            sender.sendMessage(ChatColor.YELLOW + "En İyi Combo: " + ChatColor.WHITE + combatData.getBestCombo());
            sender.sendMessage(ChatColor.YELLOW + "Toplam Hit: " + ChatColor.WHITE + combatData.getTotalHits());
        } else {
            sender.sendMessage(ChatColor.GRAY + "Bu oyuncunun henüz combat verisi bulunmuyor.");
        }
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /combatmaster reset <oyuncu>");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.player-not-found", "&cOyuncu bulunamadı!")));
            return;
        }

        plugin.getCombatManager().resetCombo(target);
        sender.sendMessage(ChatColor.GREEN + target.getName() + " oyuncusunun combo'su sıfırlandı.");
        target.sendMessage(ChatColor.YELLOW + "Combat combo'nuz bir yönetici tarafından sıfırlandı.");
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Combat-Master ===");
        sender.sendMessage(ChatColor.YELLOW + "Versiyon: " + ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "Geliştirici: " + ChatColor.WHITE + "Melut");
        sender.sendMessage(ChatColor.YELLOW + "Aktif Dünyalar: " + ChatColor.WHITE +
                (configManager.getEnabledWorlds().isEmpty() ? "Tümü" : String.join(", ", configManager.getEnabledWorlds())));
        sender.sendMessage(ChatColor.YELLOW + "Combo Timeout: " + ChatColor.WHITE + configManager.getComboTimeout() + " saniye");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> commands = Arrays.asList("reload", "stats", "reset", "info");
            for (String cmd : commands) {
                if (cmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("reset"))) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }

        return completions;
    }
}