package com.stormai.plugin;

import com.stormai.plugin.commands.LifestealCommand;
import com.stormai.plugin.listeners.PlayerDeathListener;
import com.stormai.plugin.listeners.PlayerInteractListener;
import com.stormai.plugin.utils.ConfigManager;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private HealthManager healthManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize managers
        this.healthManager = new HealthManager(this);
        this.configManager = new ConfigManager(this);

        // Load player data
        this.healthManager.loadAllPlayerData();

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        // Register commands
        getCommand("lifesteal").setExecutor(new LifestealCommand(this));

        // Register custom recipe
        registerHeartRecipe();

        // Schedule data saving
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.healthManager.saveAllPlayerData();
        }, 0L, 20L * 60L * 5L); // Save every 5 minutes

        getLogger().info("LifeSteal Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Save all player data
        this.healthManager.saveAllPlayerData();
        getLogger().info("LifeSteal Plugin disabled!");
    }

    private void registerHeartRecipe() {
        ItemStack heartItem = healthManager.createHeartItem();
        NamespacedKey key = new NamespacedKey(this, "heart_item");
        ShapedRecipe recipe = new ShapedRecipe(key, heartItem);

        recipe.shape("GGG", "GNG", "GGG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('N', Material.NETHER_STAR);

        Bukkit.addRecipe(recipe);
    }

    public HealthManager getHealthManager() {
        return healthManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}