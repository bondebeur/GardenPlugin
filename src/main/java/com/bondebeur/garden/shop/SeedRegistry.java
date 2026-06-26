package com.bondebeur.garden.shop;

import java.util.*;

public class SeedRegistry {
    private static final Map<String, ShopSeed> SEEDS = new HashMap<>();

    static {
        // Common Seeds
        registerSeed("tomato", new ShopSeed(
            "tomato", "Tomato Seed", "🍅",
            ShopSeed.SeedRarity.COMMON, 100,
            3, 12, 50,
            "A classic garden staple. Grows tomatoes."
        ));
        
        registerSeed("carrot", new ShopSeed(
            "carrot", "Carrot Seed", "🥕",
            ShopSeed.SeedRarity.COMMON, 100,
            3, 12, 75,
            "Sweet and orange. Perfect for salads."
        ));
        
        registerSeed("corn", new ShopSeed(
            "corn", "Corn Seed", "🌽",
            ShopSeed.SeedRarity.COMMON, 80,
            2, 10, 150,
            "Golden kernels of deliciousness."
        ));
        
        registerSeed("strawberry", new ShopSeed(
            "strawberry", "Strawberry Seed", "🍓",
            ShopSeed.SeedRarity.COMMON, 70,
            2, 8, 250,
            "Red, sweet, and irresistible."
        ));
        
        registerSeed("watermelon", new ShopSeed(
            "watermelon", "Watermelon Seed", "🍉",
            ShopSeed.SeedRarity.COMMON, 60,
            1, 6, 500,
            "Refreshing summer fruit."
        ));

        // Rare Seeds
        registerSeed("pineapple", new ShopSeed(
            "pineapple", "Pineapple Seed", "🍍",
            ShopSeed.SeedRarity.RARE, 15,
            1, 4, 2500,
            "Tropical and exotic. Quite rare!"
        ));
        
        registerSeed("mango", new ShopSeed(
            "mango", "Mango Seed", "🥭",
            ShopSeed.SeedRarity.RARE, 12,
            1, 3, 3000,
            "The king of fruits. Rarely found."
        ));
        
        registerSeed("blueberry", new ShopSeed(
            "blueberry", "Blueberry Seed", "🫐",
            ShopSeed.SeedRarity.RARE, 10,
            1, 3, 4000,
            "Small but mighty. Very rare."
        ));
        
        registerSeed("coconut", new ShopSeed(
            "coconut", "Coconut Seed", "🥥",
            ShopSeed.SeedRarity.RARE, 8,
            1, 2, 5000,
            "Paradise in a seed."
        ));
        
        registerSeed("chili", new ShopSeed(
            "chili", "Chili Pepper Seed", "🌶️",
            ShopSeed.SeedRarity.RARE, 8,
            1, 2, 6000,
            "Spicy and legendary."
        ));

        // Epic Seeds
        registerSeed("starfruit", new ShopSeed(
            "starfruit", "Star Fruit Seed", "⭐",
            ShopSeed.SeedRarity.EPIC, 3,
            1, 1, 25000,
            "Shines with an ethereal glow."
        ));
        
        registerSeed("moonblossom", new ShopSeed(
            "moonblossom", "Moon Blossom Seed", "🌙",
            ShopSeed.SeedRarity.EPIC, 2,
            1, 1, 35000,
            "Blooms under moonlight. Mystical."
        ));
        
        registerSeed("crystalberry", new ShopSeed(
            "crystalberry", "Crystal Berry Seed", "💎",
            ShopSeed.SeedRarity.EPIC, 2,
            1, 1, 40000,
            "Sparkles like precious gems."
        ));

        // Legendary Seeds
        registerSeed("bloodrose", new ShopSeed(
            "bloodrose", "Blood Rose Seed", "🥀",
            ShopSeed.SeedRarity.LEGENDARY, 0.5,
            1, 1, 150000,
            "A legendary crimson bloom. Extremely rare."
        ));
        
        registerSeed("meteorfruit", new ShopSeed(
            "meteorfruit", "Meteor Fruit Seed", "☄️",
            ShopSeed.SeedRarity.LEGENDARY, 0.3,
            1, 1, 250000,
            "Fell from the stars. Ultra rare."
        ));
        
        registerSeed("auroraflower", new ShopSeed(
            "auroraflower", "Aurora Flower Seed", "🌌",
            ShopSeed.SeedRarity.LEGENDARY, 0.2,
            1, 1, 400000,
            "Displays the colors of the northern lights."
        ));

        // Mythic Seeds
        registerSeed("rainbowfruit", new ShopSeed(
            "rainbowfruit", "Rainbow Fruit Seed", "🌈",
            ShopSeed.SeedRarity.MYTHIC, 0.05,
            1, 1, 2000000,
            "Only exists in legends. Impossibly rare."
        ));
        
        registerSeed("goldenapple", new ShopSeed(
            "goldenapple", "Golden Apple Tree Seed", "🍎",
            ShopSeed.SeedRarity.MYTHIC, 0.02,
            1, 1, 5000000,
            "The most legendary seed. Ever witnessed."
        ));
    }

    public static void registerSeed(String id, ShopSeed seed) {
        SEEDS.put(id, seed);
    }

    public static ShopSeed getSeed(String id) {
        return SEEDS.get(id);
    }

    public static Collection<ShopSeed> getAllSeeds() {
        return SEEDS.values();
    }

    public static Collection<ShopSeed> getSeedsByRarity(ShopSeed.SeedRarity rarity) {
        List<ShopSeed> result = new ArrayList<>();
        for (ShopSeed seed : SEEDS.values()) {
            if (seed.getRarity() == rarity) {
                result.add(seed);
            }
        }
        return result;
    }
}
