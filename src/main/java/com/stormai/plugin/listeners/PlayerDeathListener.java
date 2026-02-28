package com.stormai.plugin.listeners;

import com.stormai.plugin.Main;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Main plugin;
    private final HealthManager healthManager;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
        this.healthManager = plugin.getHealthManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Safety check: ensure killer is a player
        if (killer == null || !(killer instanceof Player)) {
            return;
        }

        int victimMaxHealth = healthManager.getPlayerMaxHealth(victim.getUniqueId());
        int killerMaxHealth = healthManager.getPlayerMaxHealth(killer.getUniqueId());

        // Transfer health from victim to killer
        if (victimMaxHealth > 2) {
            healthManager.setPlayerMaxHealth(victim.getUniqueId(), victimMaxHealth - 2);
            if (killerMaxHealth < 40) {
                healthManager.setPlayerMaxHealth(killer.getUniqueId(), killerMaxHealth + 2);
            }
            Bukkit.broadcastMessage(killer.getName() + " stole a heart from " + victim.getName() + "!");
        }

        // Check if victim should be eliminated
        if (victimMaxHealth == 2) {
            // Ban the player (you can change this to spectator mode if preferred)
            Bukkit.getBanList(Bukkit.BanList.Type.NAME).addBan(
                    victim.getName(),
                    "Eliminated from LifeSteal",
                    null,
                    "LifeSteal Plugin"
            );
            victim.kickPlayer("You have been eliminated from LifeSteal!");

            Bukkit.broadcastMessage("§c" + victim.getName() + " has been eliminated! They're out of the game!");
        }
    }
}