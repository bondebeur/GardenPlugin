package com.bondebeur.garden.shop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;

public class ShopListener implements Listener {
    private ShopManager shopManager;
    private ShopGUI shopGUI;

    public ShopListener(ShopManager shopManager, ShopGUI shopGUI) {
        this.shopManager = shopManager;
        this.shopGUI = shopGUI;
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

        // TODO: Handle purchase - check player's money, validate stock exists, etc.
        // For now, just show feedback
        
        // Play purchase sound
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.2f);

        // Send purchase message
        player.sendMessage(Component.text("💰 You purchased ").color(TextColor.color(0xFFD700))
            .append(Component.text(seed.getName()).color(TextColor.color(0x00FF00)))
            .append(Component.text(" for $" + seed.getPrice()).color(TextColor.color(0xFFD700))));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Seed Shop")) {
            shopGUI.clearCache();
        }
    }
}
