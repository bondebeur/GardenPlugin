package com.bondebeur.garden.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopSeed {
    private String id;
    private String name;
    private String icon;
    private SeedRarity rarity;
    private int baseChance; // 0-100 (percentage)
    private int minStock;
    private int maxStock;
    private long price;
    private String description;

    public enum SeedRarity {
        COMMON(100, "White"),
        RARE(15, "Green"),
        EPIC(3, "Aqua"),
        LEGENDARY(0.5, "Gold"),
        MYTHIC(0.05, "Rainbow");

        private final double baseChance;
        private final String color;

        SeedRarity(double baseChance, String color) {
            this.baseChance = baseChance;
            this.color = color;
        }

        public double getBaseChance() {
            return baseChance;
        }

        public String getColor() {
            return color;
        }
    }
}
