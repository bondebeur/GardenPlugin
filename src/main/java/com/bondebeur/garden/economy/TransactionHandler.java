package com.bondebeur.garden.economy;

import com.bondebeur.garden.shop.ShopSeed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.particle.Particle;

/**
 * Transaction handler for buying and selling seeds
 */
public class TransactionHandler {
    private EconomyManager economyManager;

    public TransactionHandler(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    /**
     * Buy seed from shop
     * @return true if purchase was successful
     */
    public boolean buySeed(Player player, ShopSeed seed, PlayerInventory inventory, int quantity) {
        if (quantity <= 0) {
            player.sendMessage(Component.text("❌ Invalid quantity")
                .color(TextColor.color(0xFF0000)));
            return false;
        }

        long totalPrice = seed.getPrice() * quantity;

        // Check if player has enough money
        if (!economyManager.hasEnoughMoney(player, totalPrice)) {
            long needed = totalPrice - economyManager.getBalance(player);
            player.sendMessage(Component.text("❌ Not enough money! You need ")
                .color(TextColor.color(0xFF0000))
                .append(Component.text(economyManager.formatMoney(needed))
                    .color(TextColor.color(0xFFD700))));
            return false;
        }

        // Remove money from player
        economyManager.removeMoney(player, totalPrice);

        // Add seed to inventory
        inventory.addSeed(seed, quantity);

        // Play sound and particles
        playPurchaseAnimation(player);

        // Send success message
        player.sendMessage(Component.text("✅ Purchased ")
            .color(TextColor.color(0x00FF00))
            .append(Component.text(quantity + "x ").color(TextColor.color(0xFFFFFF)))
            .append(Component.text(seed.getName()).color(TextColor.color(0x00FFFF)))
            .append(Component.text(" for ").color(TextColor.color(0xFFFFFF)))
            .append(Component.text(economyManager.formatMoney(totalPrice))
                .color(TextColor.color(0xFFD700))));

        // Send balance update
        sendBalanceUpdate(player);

        return true;
    }

    /**
     * Sell seed to shop
     * @return true if sale was successful
     */
    public boolean sellSeed(Player player, ShopSeed seed, PlayerInventory inventory, int quantity) {
        if (quantity <= 0) {
            player.sendMessage(Component.text("❌ Invalid quantity")
                .color(TextColor.color(0xFF0000)));
            return false;
        }

        // Check if player has enough seeds
        if (!inventory.hasSeed(seed, quantity)) {
            int has = inventory.getSeedCount(seed);
            player.sendMessage(Component.text("❌ You only have " + has + " ")
                .color(TextColor.color(0xFF0000))
                .append(Component.text(seed.getName()).color(TextColor.color(0x00FFFF))));
            return false;
        }

        // Calculate sell price (50% of buy price)
        long sellPrice = (seed.getPrice() / 2) * quantity;

        // Remove seed from inventory
        inventory.removeSeed(seed, quantity);

        // Add money to player
        economyManager.addMoney(player, sellPrice);

        // Play sound and particles
        playSaleAnimation(player);

        // Send success message
        player.sendMessage(Component.text("✅ Sold ")
            .color(TextColor.color(0x00FF00))
            .append(Component.text(quantity + "x ").color(TextColor.color(0xFFFFFF)))
            .append(Component.text(seed.getName()).color(TextColor.color(0x00FFFF)))
            .append(Component.text(" for ").color(TextColor.color(0xFFFFFF)))
            .append(Component.text(economyManager.formatMoney(sellPrice))
                .color(TextColor.color(0xFFD700))));

        // Send balance update
        sendBalanceUpdate(player);

        return true;
    }

    /**
     * Play animation when buying
     */
    private void playPurchaseAnimation(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);
        
        // Spawn particles
        org.bukkit.Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 10, 0.5, 0.5, 0.5, 0.1);
    }

    /**
     * Play animation when selling
     */
    private void playSaleAnimation(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
        
        // Spawn particles
        org.bukkit.Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 10, 0.5, 0.5, 0.5, 0.1);
    }

    /**
     * Send player their current balance
     */
    private void sendBalanceUpdate(Player player) {
        long balance = economyManager.getBalance(player);
        player.sendActionBar(Component.text("💰 Balance: ")
            .color(TextColor.color(0xFFD700))
            .append(Component.text(economyManager.formatMoney(balance))
                .color(TextColor.color(0x00FFFF))));
    }
}
