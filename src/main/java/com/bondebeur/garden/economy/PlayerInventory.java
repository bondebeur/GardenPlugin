package com.bondebeur.garden.economy;

import com.bondebeur.garden.shop.ShopSeed;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Player inventory system for seeds
 * Handles buying and selling seeds
 */
public class PlayerInventory {
    private Player player;
    private EconomyManager economyManager;
    
    @Getter
    private java.util.Map<ShopSeed, Integer> seeds = new java.util.HashMap<>();

    public PlayerInventory(Player player, EconomyManager economyManager) {
        this.player = player;
        this.economyManager = economyManager;
        loadInventory();
    }

    /**
     * Load inventory from persistent data
     */
    private void loadInventory() {
        // TODO: Load from database or persistent data
    }

    /**
     * Save inventory to persistent data
     */
    public void saveInventory() {
        // TODO: Save to database or persistent data
    }

    /**
     * Add seed to player inventory
     */
    public void addSeed(ShopSeed seed, int quantity) {
        if (quantity <= 0) {
            return;
        }
        seeds.put(seed, seeds.getOrDefault(seed, 0) + quantity);
        saveInventory();
    }

    /**
     * Remove seed from player inventory
     */
    public boolean removeSeed(ShopSeed seed, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        int current = seeds.getOrDefault(seed, 0);
        if (current < quantity) {
            return false;
        }
        seeds.put(seed, current - quantity);
        if (seeds.get(seed) <= 0) {
            seeds.remove(seed);
        }
        saveInventory();
        return true;
    }

    /**
     * Get seed quantity
     */
    public int getSeedCount(ShopSeed seed) {
        return seeds.getOrDefault(seed, 0);
    }

    /**
     * Check if player has seed
     */
    public boolean hasSeed(ShopSeed seed, int quantity) {
        return getSeedCount(seed) >= quantity;
    }
}
