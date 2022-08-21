package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.frontend.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides initialisation service for the StackPun plugin.
 * @see org.bukkit.plugin.java.JavaPlugin
 * @author WithLithum
 */
public class StackPun extends JavaPlugin {
    private static StackPun _instance;

    private CommandManager commandManager;

    private void setInstance(StackPun instance) {
        _instance = instance;
    }

    public static StackPun instance() {
        return _instance;
    }

    /**
     * Gets the command manager of this instance.
     * @return The command manager.
     */
    public CommandManager commandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        getSLF4JLogger().info("StackPun Service instantiated");

        commandManager = new CommandManager();
    }
}
