package com.bondebeur.garden.shop;

import lombok.Getter;
import java.util.*;

public class ShopManager {
    private static final long REFRESH_INTERVAL = 5 * 60 * 1000; // 5 minutes
    private static final long LUCKY_EVENT_CHECK_INTERVAL = 60 * 1000; // 1 hour
    private static final long TRAVELING_MERCHANT_CHECK_INTERVAL = 30 * 60 * 1000; // 30 minutes
    
    @Getter
    private ShopInventory globalShop;
    
    @Getter
    private long nextRefreshTime;
    
    private long nextLuckyEventCheckTime;
    private long nextTravelingMerchantCheckTime;
    
    @Getter
    private double currentLuck = 1.0; // 1.0 = no luck
    
    @Getter
    private boolean luckyEventActive = false;
    
    @Getter
    private long luckyEventEndTime = 0;
    
    @Getter
    private boolean travelingMerchantActive = false;
    
    @Getter
    private long travelingMerchantEndTime = 0;
    
    private Random random = new Random();
    
    // Callbacks
    private Runnable onShopRefresh;
    private Runnable onLegendaryFound;
    private Runnable onMythicFound;
    private Runnable onLuckyEventStart;
    private Runnable onTravelingMerchantSpawn;

    public ShopManager() {
        this.globalShop = new ShopInventory();
        this.nextRefreshTime = System.currentTimeMillis() + REFRESH_INTERVAL;
        this.nextLuckyEventCheckTime = System.currentTimeMillis() + LUCKY_EVENT_CHECK_INTERVAL;
        this.nextTravelingMerchantCheckTime = System.currentTimeMillis() + TRAVELING_MERCHANT_CHECK_INTERVAL;
        
        // Initial shop generation
        globalShop.generateInventory(1.0);
    }

    /**
     * Call this method every game tick to update shop state
     */
    public void update() {
        long now = System.currentTimeMillis();
        
        // Check shop refresh
        if (now >= nextRefreshTime) {
            refreshShop();
        }
        
        // Check lucky event trigger
        if (now >= nextLuckyEventCheckTime) {
            checkLuckyEventTrigger();
            nextLuckyEventCheckTime = now + LUCKY_EVENT_CHECK_INTERVAL;
        }
        
        // Check traveling merchant trigger
        if (now >= nextTravelingMerchantCheckTime) {
            checkTravelingMerchantTrigger();
            nextTravelingMerchantCheckTime = now + TRAVELING_MERCHANT_CHECK_INTERVAL;
        }
        
        // End lucky event if time expired
        if (luckyEventActive && now >= luckyEventEndTime) {
            luckyEventActive = false;
            currentLuck = 1.0;
        }
        
        // End traveling merchant if time expired
        if (travelingMerchantActive && now >= travelingMerchantEndTime) {
            travelingMerchantActive = false;
        }
    }

    private void refreshShop() {
        currentLuck = luckyEventActive ? 1.5 : 1.0;
        globalShop.generateInventory(currentLuck);
        nextRefreshTime = System.currentTimeMillis() + REFRESH_INTERVAL;
        
        if (onShopRefresh != null) {
            onShopRefresh.run();
        }
        
        if (globalShop.isHasLegendary() && onLegendaryFound != null) {
            onLegendaryFound.run();
        }
        
        if (globalShop.isHasMythic() && onMythicFound != null) {
            onMythicFound.run();
        }
    }

    private void checkLuckyEventTrigger() {
        // 5% chance per hour
        if (random.nextDouble() < 0.05) {
            luckyEventActive = true;
            luckyEventEndTime = System.currentTimeMillis() + 10 * 60 * 1000; // 10 minutes
            
            if (onLuckyEventStart != null) {
                onLuckyEventStart.run();
            }
        }
    }

    private void checkTravelingMerchantTrigger() {
        // 1% chance per 30 minutes
        if (random.nextDouble() < 0.01) {
            travelingMerchantActive = true;
            travelingMerchantEndTime = System.currentTimeMillis() + 10 * 60 * 1000; // 10 minutes
            
            if (onTravelingMerchantSpawn != null) {
                onTravelingMerchantSpawn.run();
            }
        }
    }

    public long getTimeUntilNextRefresh() {
        return Math.max(0, nextRefreshTime - System.currentTimeMillis());
    }

    public void setOnShopRefresh(Runnable callback) {
        this.onShopRefresh = callback;
    }

    public void setOnLegendaryFound(Runnable callback) {
        this.onLegendaryFound = callback;
    }

    public void setOnMythicFound(Runnable callback) {
        this.onMythicFound = callback;
    }

    public void setOnLuckyEventStart(Runnable callback) {
        this.onLuckyEventStart = callback;
    }

    public void setOnTravelingMerchantSpawn(Runnable callback) {
        this.onTravelingMerchantSpawn = callback;
    }
}
