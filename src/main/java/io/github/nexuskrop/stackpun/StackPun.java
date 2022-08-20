package io.github.nexuskrop.stackpun;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides initialisation service for the StackPun plugin.
 * @see org.bukkit.plugin.java.JavaPlugin
 * @author WithLithum
 */
public class StackPun extends JavaPlugin {
    @Override
    public void onEnable() {
        getSLF4JLogger().info("StackPun Service instantiated");
    }
}
