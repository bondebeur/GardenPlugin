package com.bondebeur.garden.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.*;

public class ShopGUI {
    private ShopManager shopManager;
    private Map<Integer, ShopSeed> slotToSeed = new HashMap<>();

    public ShopGUI(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    /**
     * Create a shop GUI inventory for a player
     */
    public org.bukkit.inventory.Inventory createShopInventory(Player player) {
        org.bukkit.inventory.Inventory inventory = org.bukkit.Bukkit.createInventory(
            null,
            54,
            Component.text("🛒 Seed Shop", TextColor.color(0xFFD700))
                .decoration(TextDecoration.BOLD, true)
        );

        // Fill background with gray glass
        ItemStack grayGlass = createGlassPane();
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, grayGlass);
            inventory.setItem(45 + i, grayGlass);
        }
        for (int i = 9; i < 45; i += 9) {
            inventory.setItem(i, grayGlass);
            inventory.setItem(i + 8, grayGlass);
        }

        // Add timer in top-left
        ItemStack timerItem = createTimerItem();
        inventory.setItem(1, timerItem);

        // Add available seeds
        ShopInventory globalShop = shopManager.getGlobalShop();
        int slot = 10;
        int slotIndex = 0;

        for (ShopSeed seed : globalShop.getAvailableSeeds()) {
            if (slot >= 45) break;

            if (slot % 9 == 0 || slot % 9 == 8) {
                slot++; // Skip border columns
            }

            ItemStack seedItem = createSeedItem(seed, globalShop.getStock(seed));
            inventory.setItem(slot, seedItem);
            slotToSeed.put(slot, seed);
            slot++;
        }

        return inventory;
    }

    /**
     * Create a seed display item
     */
    private ItemStack createSeedItem(ShopSeed seed, int stock) {
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();

        // Display name with rarity color
        TextColor rarityColor = getRarityColor(seed.getRarity());
        Component displayName = Component.text(seed.getIcon() + " " + seed.getName())
            .color(TextColor.color(0xFFFFFF))
            .decoration(TextDecoration.BOLD, true);
        meta.displayName(displayName);

        // Lore with details
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        
        // Rarity
        String rarityEmoji = getRarityEmoji(seed.getRarity());
        lore.add(Component.text(rarityEmoji + " " + seed.getRarity().name())
            .color(rarityColor));
        
        // Stock
        lore.add(Component.text("📦 Stock: " + stock)
            .color(TextColor.color(0xAAAAAA)));
        
        // Price
        lore.add(Component.text("💰 Price: $" + formatPrice(seed.getPrice()))
            .color(TextColor.color(0xFFD700))
            .decoration(TextDecoration.BOLD, true));
        
        // Description
        lore.add(Component.text(""));
        lore.add(Component.text(seed.getDescription())
            .color(TextColor.color(0xCCCCCC)));
        
        lore.add(Component.text(""));
        lore.add(Component.text("Click to purchase")
            .color(TextColor.color(0x00FF00))
            .decoration(TextDecoration.ITALIC, true));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Create timer item for next refresh
     */
    private ItemStack createTimerItem() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();

        long timeUntilRefresh = shopManager.getTimeUntilNextRefresh();
        long minutes = (timeUntilRefresh / 1000) / 60;
        long seconds = (timeUntilRefresh / 1000) % 60;

        Component displayName = Component.text(String.format("🕒 Next Refresh: %02d:%02d", minutes, seconds))
            .color(TextColor.color(0x00FFFF))
            .decoration(TextDecoration.BOLD, true);
        meta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text("The shop refreshes every 5 minutes")
            .color(TextColor.color(0xAAAAAA)));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Create glass pane for GUI borders
     */
    private ItemStack createGlassPane() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);
        return item;
    }

    private TextColor getRarityColor(ShopSeed.SeedRarity rarity) {
        return switch (rarity) {
            case COMMON -> TextColor.color(0xFFFFFF);
            case RARE -> TextColor.color(0x00FF00);
            case EPIC -> TextColor.color(0x00FFFF);
            case LEGENDARY -> TextColor.color(0xFFD700);
            case MYTHIC -> TextColor.color(0xFF00FF);
        };
    }

    private String getRarityEmoji(ShopSeed.SeedRarity rarity) {
        return switch (rarity) {
            case COMMON -> "⚪";
            case RARE -> "🟢";
            case EPIC -> "🔷";
            case LEGENDARY -> "⭐";
            case MYTHIC -> "✨";
        };
    }

    private String formatPrice(long price) {
        if (price >= 1_000_000) {
            return String.format("%.1fM", price / 1_000_000.0);
        } else if (price >= 1_000) {
            return String.format("%.1fK", price / 1_000.0);
        }
        return String.valueOf(price);
    }

    public ShopSeed getSeedAtSlot(int slot) {
        return slotToSeed.get(slot);
    }

    public void clearCache() {
        slotToSeed.clear();
    }
}
