package com.melut.combatmaster.gui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        if (itemMeta != null) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            itemMeta.setLore(loreList);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (itemMeta != null) {
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            itemMeta.setLore(coloredLore);
        }
        return this;
    }

    public ItemBuilder addLore(String... lines) {
        if (itemMeta != null) {
            List<String> lore = itemMeta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            for (String line : lines) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            itemMeta.setLore(lore);
        }
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(Math.max(1, amount));
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        if (itemMeta != null && glowing) {
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder hideFlags() {
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.values());
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
        }
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (itemMeta != null) {
            itemMeta.setUnbreakable(unbreakable);
        }
        return this;
    }

    public ItemStack build() {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemBuilder create(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder create(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemStack createBackButton() {
        return new ItemBuilder(Material.ARROW)
                .setName("&c« Geri")
                .setLore("&7Ana menüye geri dön")
                .build();
    }

    public static ItemStack createCloseButton() {
        return new ItemBuilder(Material.BARRIER)
                .setName("&cKapat")
                .setLore("&7Menüyü kapat")
                .build();
    }

    public static ItemStack createRefreshButton() {
        return new ItemBuilder(Material.LIME_DYE)
                .setName("&aYenile")
                .setLore("&7Sayfayı yenile")
                .build();
    }

    public static ItemStack createPlaceholder(String name) {
        return new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                .setName(name != null ? name : "&7")
                .setLore("")
                .build();
    }

    public static ItemStack createBorder() {
        return createPlaceholder("&7");
    }
}