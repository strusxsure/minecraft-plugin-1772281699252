package com.stormai.plugin.utils;

import com.stormai.plugin.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HealthManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> playerMaxHealths;
    private final NamespacedKey heartKey;

    public HealthManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerMaxHealths = new HashMap<>();
        this.heartKey = new NamespacedKey(plugin, "heart_item");
    }

    public void setPlayerMaxHealth(UUID uuid, int maxHealth) {
        playerMaxHealths.put(uuid, maxHealth);
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            updatePlayerHealth(player);
        }
    }

    public int getPlayerMaxHealth(UUID uuid) {
        return playerMaxHealths.getOrDefault(uuid, 20);
    }

    public ItemStack createHeartItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Heart");
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public void updatePlayerHealth(Player player) {
        int maxHealth = getPlayerMaxHealth(player.getUniqueId());
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.setBaseValue(maxHealth);
        }
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }
    }

    public void loadAllPlayerData() {
        // Load from config or database
        // This is a simplified version using config.yml
        for (String uuidStr : plugin.getConfig().getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                int maxHealth = plugin.getConfig().getInt(uuidStr + ".max_health", 20);
                playerMaxHealths.put(uuid, maxHealth);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void saveAllPlayerData() {
        for (Map.Entry<UUID, Integer> entry : playerMaxHealths.entrySet()) {
            plugin.getConfig().set(entry.getKey().toString() + ".max_health", entry.getValue());
        }
        plugin.saveConfig();
    }
}