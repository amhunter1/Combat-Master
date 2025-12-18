package com.melut.combatmaster.gui;

import com.melut.combatmaster.CombatMaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseGUI {

    protected final CombatMaster plugin;
    protected final Player player;
    protected Inventory inventory;
    protected final String title;
    protected final int size;
    protected final Map<Integer, GUIAction> actions;

    public BaseGUI(CombatMaster plugin, Player player, String title, int size) {
        this.plugin = plugin;
        this.player = player;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.size = size;
        this.actions = new HashMap<>();
        this.inventory = Bukkit.createInventory(null, size, this.title);
    }

    public abstract void setupItems();

    public void open() {
        setupItems();
        player.openInventory(inventory);
        playSound(Sound.BLOCK_CHEST_OPEN, 0.5f, 1.2f);
    }

    public void refresh() {
        inventory.clear();
        actions.clear();
        setupItems();
    }

    protected void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    protected void setItem(int slot, ItemStack item, GUIAction action) {
        inventory.setItem(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    protected void playSound(Sound sound, float volume, float pitch) {
        if (plugin.getConfigManager().isSoundsEnabled()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    protected void playClickSound() {
        playSound(Sound.UI_BUTTON_CLICK, 0.3f, 1.0f);
    }

    protected void playErrorSound() {
        playSound(Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
    }

    protected void playSuccessSound() {
        playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.5f);
    }

    public void handleClick(int slot) {
        GUIAction action = actions.get(slot);
        if (action != null) {
            playClickSound();
            action.execute(player);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    @FunctionalInterface
    public interface GUIAction {
        void execute(Player player);
    }
}