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

        if (!plugin.getConfigManager().isWorldEnabled(attacker.getWorld().getName())) {
            return;
        }

        if (attacker.equals(victim)) return;

        combatManager.handleHit(attacker);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL &&
                event.getCause() != EntityDamageEvent.DamageCause.DROWNING &&
                event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {

            combatManager.resetCombo(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getDatabaseManager().loadPlayerData(player.getUniqueId()).thenAccept(playerData -> {
            if (playerData.getBestCombo() > 0) {
                CombatManager.CombatData combatData = new CombatManager.CombatData();
                combatData.setBestCombo(playerData.getBestCombo());
                combatData.setTotalHits(playerData.getTotalHits());
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CombatManager.CombatData combatData = combatManager.getPlayerData(player.getUniqueId());

        if (combatData != null) {
            plugin.getDatabaseManager().savePlayerData(
                    player.getUniqueId(),
                    player.getName(),
                    combatData.getBestCombo(),
                    combatData.getTotalHits()
            );
        }

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