package com.melut.combatmaster.listeners;

import com.melut.combatmaster.CombatMaster;
import com.melut.combatmaster.managers.CombatManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class CombatListener implements Listener {

    private final CombatMaster plugin;
    private final CombatManager combatManager;

    public CombatListener(CombatMaster plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = getPlayerAttacker(event.getDamager());
        if (attacker == null) return;

        Entity victim = event.getEntity();
        if (!(victim instanceof LivingEntity)) return;

        // Dünya kontrolü
        if (!plugin.getConfigManager().isWorldEnabled(attacker.getWorld().getName())) {
            return;
        }

        // Kendine zarar verme kontrolü
        if (attacker.equals(victim)) return;

        // Hit işle
        combatManager.handleHit(attacker);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Player attacker = getPlayerAttacker(event.getDamager());
        if (attacker == null) return;

        // Dünya kontrolü
        if (!plugin.getConfigManager().isWorldEnabled(attacker.getWorld().getName())) {
            return;
        }

        // Eğer hasar 0 ise veya event iptal edilmişse miss sayılır
        if (event.getDamage() <= 0 || event.isCancelled()) {
            combatManager.handleMiss(attacker);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        // Oyuncu zarar aldıysa combo'yu sıfırla
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL &&
                event.getCause() != EntityDamageEvent.DamageCause.DROWNING &&
                event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {

            combatManager.resetCombo(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Oyuncu verilerini database'den yükle
        plugin.getDatabaseManager().loadPlayerData(player.getUniqueId()).thenAccept(playerData -> {
            if (playerData.getBestCombo() > 0 || playerData.getTotalHits() > 0) {
                // Ana thread'de combat manager'a veri yükle
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    combatManager.loadPlayerData(player.getUniqueId(), 
                        playerData.getBestCombo(), playerData.getTotalHits());
                });
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CombatManager.CombatData combatData = combatManager.getPlayerData(player.getUniqueId());

        if (combatData != null) {
            // Verileri kaydet
            plugin.getDatabaseManager().savePlayerData(
                    player.getUniqueId(),
                    player.getName(),
                    combatData.getBestCombo(),
                    combatData.getTotalHits()
            );
        }

        // Oyuncuyu manager'dan kaldır
        combatManager.removePlayer(player.getUniqueId());
    }

    private Player getPlayerAttacker(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        } else if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                return (Player) shooter;
            }
        }
        return null;
    }
}