package com.bondebeur.garden.shop;

import lombok.Getter;
import java.util.*;

public class ShopInventory {
    @Getter
    private final Map<ShopSeed, Integer> inventory = new LinkedHashMap<>();
    
    @Getter
    private long lastRefreshTime;
    
    @Getter
    private boolean hasLegendary = false;
    
    @Getter
    private boolean hasMythic = false;

    public ShopInventory() {
        this.lastRefreshTime = System.currentTimeMillis();
    }

    /**
     * Generate a new shop inventory with random seeds
     * @param luck Player's luck multiplier (1.0 = no luck, 1.5 = 50% luck)
     */
    public void generateInventory(double luck) {
        inventory.clear();
        hasLegendary = false;
        hasMythic = false;
        
        Random random = new Random();
        
        // Generate seeds based on their chances
        for (ShopSeed seed : SeedRegistry.getAllSeeds()) {
            double adjustedChance = seed.getBaseChance() * luck;
            double roll = random.nextDouble() * 100;
            
            if (roll < adjustedChance) {
                int stock = random.nextInt(seed.getMaxStock() - seed.getMinStock() + 1) + seed.getMinStock();
                inventory.put(seed, stock);
                
                if (seed.getRarity() == ShopSeed.SeedRarity.LEGENDARY) {
                    hasLegendary = true;
                } else if (seed.getRarity() == ShopSeed.SeedRarity.MYTHIC) {
                    hasMythic = true;
                }
            }
        }
        
        this.lastRefreshTime = System.currentTimeMillis();
    }

    public Integer getStock(ShopSeed seed) {
        return inventory.getOrDefault(seed, 0);
    }

    public boolean hasStock(ShopSeed seed) {
        return inventory.containsKey(seed) && inventory.get(seed) > 0;
    }

    public void addSeed(ShopSeed seed, int quantity) {
        inventory.put(seed, inventory.getOrDefault(seed, 0) + quantity);
    }

    public List<ShopSeed> getAvailableSeeds() {
        return new ArrayList<>(inventory.keySet());
    }
}
