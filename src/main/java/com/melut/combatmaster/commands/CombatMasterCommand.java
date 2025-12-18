package com.melut.combatmaster.commands;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.gui.menus.MainMenu;
import com.melut.combatmaster.gui.menus.StatsMenu;
import com.melut.combatmaster.managers.ConfigManager;
import com.melut.combatmaster.managers.CombatManager;
import com.melut.combatmaster.database.DatabaseManager;

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
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                plugin.getMenuManager().openMenu(player, new MainMenu(plugin, player));
                return true;
            } else {
                showHelp(sender);
                return true;
            }
        }

        switch (args[0].toLowerCase()) {
            case "gui":
            case "menu":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getLangManager().getMessage("commands.player_only", "&cBu komut sadece oyuncular tarafından kullanılabilir!"));
                    return true;
                }
                if (!sender.hasPermission("combatmaster.use")) {
                    noPerm(sender);
                    return true;
                }
                Player player = (Player) sender;
                plugin.getMenuManager().openMenu(player, new MainMenu(plugin, player));
                break;
            case "reload":
                if (!sender.hasPermission("combatmaster.admin")) {
                    noPerm(sender);
                    break;
                }
                handleReload(sender);
                break;

            case "stats":
                if (!sender.hasPermission("combatmaster.use") && !sender.hasPermission("combatmaster.admin")) {
                    noPerm(sender);
                    break;
                }
                handleStats(sender, args);
                break;

            case "reset":
                if (!sender.hasPermission("combatmaster.admin")) {
                    noPerm(sender);
                    break;
                }
                handleReset(sender, args);
                break;

            case "version":
            case "info":
                if (!sender.hasPermission("combatmaster.admin")) {
                    noPerm(sender);
                    break;
                }
                handleInfo(sender);
                break;

            case "top":
                if (!sender.hasPermission("combatmaster.use") && !sender.hasPermission("combatmaster.admin")) {
                    noPerm(sender);
                    break;
                }
                handleTop(sender);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(plugin.getLangManager().getMessage("commands.help_header"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.gui_usage"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.stats_usage"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.top_usage"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.reload_usage"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.reset_usage"));
        sender.sendMessage(plugin.getLangManager().getMessage("commands.info_usage"));
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.reloadPlugin();
            sender.sendMessage(plugin.getLangManager().getMessage("commands.plugin_reloaded"));
        } catch (Exception e) {
            sender.sendMessage(plugin.getLangManager().getMessage("system.config_error", "&cPlugin yeniden yüklenirken hata oluştu: " + e.getMessage()));
            plugin.getLogger().severe("Reload hatası: " + e.getMessage());
        }
    }

    private void handleStats(CommandSender sender, String[] args) {
        if (args.length < 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                plugin.getMenuManager().openMenu(player, new StatsMenu(plugin, player));
            } else {
                sender.sendMessage(plugin.getLangManager().getMessage("commands.console_specify_player", "&cKonsol için oyuncu adı belirtmelisiniz!"));
            }
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getLangManager().getMessage("commands.player_not_found"));
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getMenuManager().openMenu(player, new StatsMenu(plugin, target));
        } else {
            showPlayerStats(sender, target);
        }
    }

    private void showPlayerStats(CommandSender sender, Player target) {
        CombatManager.CombatData combatData = plugin.getCombatManager().getPlayerData(target.getUniqueId());

        sender.sendMessage(plugin.getLangManager().getMessage("gui.titles.stats", target.getName()));

        if (combatData != null) {
            sender.sendMessage(plugin.getLangManager().getMessage("gui.items.current_combo") + ": " + combatData.getCurrentCombo());
            sender.sendMessage(plugin.getLangManager().getMessage("gui.items.best_combo") + ": " + combatData.getBestCombo());
            sender.sendMessage(plugin.getLangManager().getMessage("gui.items.total_hits") + ": " + combatData.getTotalHits());
        } else {
            sender.sendMessage(plugin.getLangManager().getMessage("commands.no_combat_data", "&7Bu oyuncunun henüz combat verisi bulunmuyor."));
        }
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getLangManager().getMessage("commands.reset_usage"));
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getLangManager().getMessage("commands.player_not_found"));
            return;
        }

        plugin.getCombatManager().resetCombo(target);
        sender.sendMessage(plugin.getLangManager().getMessage("commands.combo_reset", target.getName() + " oyuncusunun combo'su sıfırlandı."));
        target.sendMessage(plugin.getLangManager().getMessage("commands.combo_reset_notify", "Combat combo'nuz bir yönetici tarafından sıfırlandı."));
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(plugin.getLangManager().getGUITitle("main"));
        sender.sendMessage("&eVersiyon: &f" + plugin.getDescription().getVersion());
        sender.sendMessage("&eGeliştirici: &fMelut");
        sender.sendMessage("&eDil: &f" + plugin.getLangManager().getCurrentLanguage());
        sender.sendMessage("&eAktif Dünyalar: &f" +
                (configManager.getEnabledWorlds().isEmpty() ? "Tümü" : String.join(", ", configManager.getEnabledWorlds())));
        sender.sendMessage(plugin.getLangManager().getMessage("combat_info.timeout", configManager.getComboTimeout()));
    }

    private void handleTop(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Leaderboard yükleniyor...");
        plugin.getDatabaseManager().getLeaderboard(plugin.getConfigManager().getLeaderboardSize()).thenAccept(leaderboard -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfigManager().getLeaderboardTitle());
                sender.sendMessage("");
                sender.sendMessage(title);
                sender.sendMessage("");

                if (leaderboard.isEmpty()) {
                    sender.sendMessage(ChatColor.GRAY + "Henüz hiç combat verisi bulunmuyor.");
                    sender.sendMessage("");
                    return;
                }

                String format = plugin.getConfigManager().getLeaderboardFormat();
                for (DatabaseManager.LeaderboardEntry entry : leaderboard) {
                    String line = format
                            .replace("{rank}", String.valueOf(entry.getRank()))
                            .replace("{player}", entry.getPlayerName())
                            .replace("{combo}", String.valueOf(entry.getBestCombo()));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
                sender.sendMessage("");
            });
        }).exceptionally(throwable -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> sender.sendMessage(ChatColor.RED + "Leaderboard yüklenirken hata oluştu!"));
            plugin.getLogger().severe("Leaderboard hatası: " + throwable.getMessage());
            return null;
        });
    }

    private void noPerm(CommandSender sender) {
        sender.sendMessage(plugin.getLangManager().getMessage("commands.no_permission"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> commands = Arrays.asList("gui", "menu", "stats", "top", "reload", "reset", "info");
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