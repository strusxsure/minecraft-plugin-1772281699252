package com.stormai.plugin.listeners;

import com.stormai.plugin.Main;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final Main plugin;
    private final HealthManager healthManager;

    public PlayerInteractListener(Main plugin) {
        this.plugin = plugin;
        this.healthManager = plugin.getHealthManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || event.getHand() == null) {
            return;
        }

        if (item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
            // Check if it's our custom heart item (using display name or custom tag)
            if (item.getItemMeta().getDisplayName().equals("Heart")) {
                event.setCancelled(true);

                if (healthManager.getPlayerMaxHealth(player.getUniqueId()) < 40) {
                    healthManager.setPlayerMaxHealth(player.getUniqueId(),
                            healthManager.getPlayerMaxHealth(player.getUniqueId()) + 2);
                    item.setAmount(item.getAmount() - 1);
                    player.sendMessage("§aYou consumed a Heart! +1 max health.");
                } else {
                    player.sendMessage("§cYou already have maximum health!");
                }
            }
        }
    }
}