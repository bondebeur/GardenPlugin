package com.bondebeur.garden.shop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import com.bondebeur.garden.economy.EconomyManager;
import com.bondebeur.garden.economy.PlayerInventory;
import com.bondebeur.garden.economy.TransactionHandler;

public class ShopListener implements Listener {
    private ShopManager shopManager;
    private ShopGUI shopGUI;
    private EconomyManager economyManager;
    private TransactionHandler transactionHandler;
    private java.util.Map<Player, PlayerInventory> playerInventories = new java.util.HashMap<>();

    public ShopListener(ShopManager shopManager, ShopGUI shopGUI, EconomyManager economyManager) {
        this.shopManager = shopManager;
        this.shopGUI = shopGUI;
        this.economyManager = economyManager;
        this.transactionHandler = new TransactionHandler(economyManager);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if this is the shop inventory
        if (!event.getView().getTitle().contains("Seed Shop")) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        ShopSeed seed = shopGUI.getSeedAtSlot(slot);
        if (seed == null) {
            return;
        }

        // Get or create player inventory
        PlayerInventory inventory = playerInventories.computeIfAbsent(player, k -> new PlayerInventory(player, economyManager));

        // Determine quantity based on click type
        int quantity = 1;
        if (event.isShiftClick()) {
            quantity = 64; // Shift-click = bulk purchase
        } else if (event.isRightClick()) {
            quantity = 10; // Right-click = 10x
        }

        // Process purchase
        transactionHandler.buySeed(player, seed, inventory, quantity);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Seed Shop")) {
            shopGUI.clearCache();
        }
    }
}
