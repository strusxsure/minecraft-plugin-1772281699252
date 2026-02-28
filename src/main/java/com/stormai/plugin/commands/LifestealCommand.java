package com.stormai.plugin.commands;

import com.stormai.plugin.Main;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LifestealCommand implements CommandExecutor {

    private final Main plugin;
    private final HealthManager healthManager;

    public LifestealCommand(Main plugin) {
        this.plugin = plugin;
        this.healthManager = plugin.getHealthManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("lifesteal")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§6LifeSteal Plugin by StormAI");
            player.sendMessage("§7/lifesteal withdraw <amount> - Convert health to Heart items");
            return true;
        }

        if (args[0].equalsIgnoreCase("withdraw")) {
            if (args.length != 2) {
                player.sendMessage("§cUsage: /lifesteal withdraw <amount>");
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cPlease provide a valid number!");
                return true;
            }

            if (amount <= 0) {
                player.sendMessage("§cAmount must be positive!");
                return true;
            }

            int currentHealth = (int) player.getHealth();
            if (amount > currentHealth) {
                player.sendMessage("§cYou don't have enough health!");
                return true;
            }

            // Create heart items
            for (int i = 0; i < amount / 2; i++) {
                player.getInventory().addItem(healthManager.createHeartItem());
            }

            // Deduct health
            player.setHealth(currentHealth - amount);
            player.sendMessage("§aWithdrew " + amount + " health as Heart items!");

            return true;
        }

        return true;
    }
}