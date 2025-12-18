package com.melut.combatmaster.gui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GUIUtils {

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static void fillBorders(Inventory inventory) {
        ItemStack border = ItemBuilder.createBorder();
        int size = inventory.getSize();
        
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, border);
        }
        
        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, border);
        }
        
        for (int i = 9; i < size - 9; i += 9) {
            inventory.setItem(i, border);
            if (i + 8 < size) {
                inventory.setItem(i + 8, border);
            }
        }
    }

    public static void fillRow(Inventory inventory, int row, ItemStack item) {
        int start = row * 9;
        int end = start + 9;
        
        for (int i = start; i < end && i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
    }

    public static void fillColumn(Inventory inventory, int column, ItemStack item) {
        for (int row = 0; row < inventory.getSize() / 9; row++) {
            int slot = row * 9 + column;
            if (slot < inventory.getSize()) {
                inventory.setItem(slot, item);
            }
        }
    }

    public static String formatNumber(long number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String formatTime(long milliseconds) {
        return DATE_FORMAT.format(new Date(milliseconds));
    }

    public static String formatDuration(long seconds) {
        if (seconds < 60) {
            return seconds + " saniye";
        } else if (seconds < 3600) {
            return (seconds / 60) + " dakika " + (seconds % 60) + " saniye";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            return hours + " saat " + minutes + " dakika";
        } else {
            long days = seconds / 86400;
            long hours = (seconds % 86400) / 3600;
            return days + " gün " + hours + " saat";
        }
    }

    public static String getProgressBar(int current, int max, int length) {
        if (max <= 0) return ChatColor.GRAY + "▬".repeat(length);
        
        int progress = Math.min((current * length) / max, length);
        String completed = ChatColor.GREEN + "▬".repeat(progress);
        String remaining = ChatColor.GRAY + "▬".repeat(length - progress);
        
        return completed + remaining;
    }

    public static String getRankColor(int rank) {
        return switch (rank) {
            case 1 -> "&6"; // Gold
            case 2 -> "&7"; // Silver  
            case 3 -> "&c"; // Bronze
            default -> "&e"; // Yellow
        };
    }

    public static String getComboLevelName(int combo) {
        if (combo < 10) return "&7Başlangıç";
        else if (combo < 25) return "&f⭐ Deneyimli";
        else if (combo < 50) return "&e⭐⭐ Usta";
        else if (combo < 100) return "&6⭐⭐⭐ Uzman";
        else if (combo < 200) return "&c⭐⭐⭐⭐ Efsane";
        else if (combo < 500) return "&d⭐⭐⭐⭐⭐ Destansı";
        else return "&b✦ Tanrısal";
    }

    public static Material getComboMaterial(int combo) {
        if (combo < 10) return Material.WOODEN_SWORD;
        else if (combo < 25) return Material.STONE_SWORD;
        else if (combo < 50) return Material.IRON_SWORD;
        else if (combo < 100) return Material.GOLDEN_SWORD;
        else if (combo < 200) return Material.DIAMOND_SWORD;
        else if (combo < 500) return Material.NETHERITE_SWORD;
        else return Material.TRIDENT;
    }

    public static ItemStack createNavigationButton(String name, Material material, List<String> lore) {
        return ItemBuilder.create(material)
                .setName(name)
                .setLore(lore)
                .setGlowing(true)
                .build();
    }

    public static ItemStack createInfoItem(String name, Material material, Object value, String description) {
        return ItemBuilder.create(material)
                .setName(name)
                .setLore(
                    "&7" + description,
                    "",
                    "&e" + (value != null ? value.toString() : "Bilinmiyor")
                )
                .build();
    }

    public static boolean isValidSlot(int slot, int inventorySize) {
        return slot >= 0 && slot < inventorySize;
    }

    public static int getSlotFromCoordinates(int row, int column) {
        return row * 9 + column;
    }

    public static int getRowFromSlot(int slot) {
        return slot / 9;
    }

    public static int getColumnFromSlot(int slot) {
        return slot % 9;
    }
}