package com.bondebeur.garden.economy;

import org.bukkit.entity.Player;

/**
 * Economy system for the Garden plugin
 * Handles player money, deposits, withdrawals, and transactions
 */
public class EconomyManager {
    private static final String MONEY_KEY = "garden_money";
    
    /**
     * Get player's current balance
     */
    public long getBalance(Player player) {
        return player.getPersistentDataContainer().getLong(
            new org.bukkit.NamespacedKey(
                org.bukkit.Bukkit.getPluginManager().getPlugin("GardenPlugin"),
                MONEY_KEY
            )
        );
    }

    /**
     * Set player's balance
     */
    public void setBalance(Player player, long amount) {
        if (amount < 0) {
            amount = 0;
        }
        player.getPersistentDataContainer().set(
            new org.bukkit.NamespacedKey(
                org.bukkit.Bukkit.getPluginManager().getPlugin("GardenPlugin"),
                MONEY_KEY
            ),
            org.bukkit.persistence.PersistentDataType.LONG,
            amount
        );
    }

    /**
     * Add money to player
     */
    public void addMoney(Player player, long amount) {
        if (amount <= 0) {
            return;
        }
        long currentBalance = getBalance(player);
        setBalance(player, currentBalance + amount);
    }

    /**
     * Remove money from player
     */
    public boolean removeMoney(Player player, long amount) {
        if (amount <= 0) {
            return false;
        }
        long currentBalance = getBalance(player);
        if (currentBalance < amount) {
            return false; // Not enough money
        }
        setBalance(player, currentBalance - amount);
        return true;
    }

    /**
     * Check if player has enough money
     */
    public boolean hasEnoughMoney(Player player, long amount) {
        return getBalance(player) >= amount;
    }

    /**
     * Transfer money from one player to another
     */
    public boolean transferMoney(Player from, Player to, long amount) {
        if (!removeMoney(from, amount)) {
            return false;
        }
        addMoney(to, amount);
        return true;
    }

    /**
     * Format money for display
     */
    public String formatMoney(long amount) {
        if (amount >= 1_000_000) {
            return String.format("$%.2fM", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("$%.2fK", amount / 1_000.0);
        }
        return "$" + amount;
    }
}
