/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.frontend.commands.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.event.InventoryListener;
import x.nexuskrop.stackpun.net.StatusListener;
import x.nexuskrop.stackpun.util.StackPunImpl;

/**
 * Provides initialisation service and main API access point for the StackPun plugin.
 *
 * @author WithLithum
 * @apiNote Use {@code StackPun.instance()} to acquire a shared instance. Do not create a new
 * instance as it will mess up the environment.
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class StackPun extends JavaPlugin {
    private static StackPunImpl impl;

    private static void setInstance(StackPun instance) {
        impl = new StackPunImpl(instance);
    }

    /**
     * Gets the API instance of this plugin.
     *
     * @return An instance of {@link StackPun} as {@link IStackPun}. Will always return the same
     * instance.
     */
    public static @NotNull IStackPun api() {
        return impl;
    }

    /**
     * Enables this {@link JavaPlugin} and initialises this plugin.
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        setInstance(this);
        impl.initialise(this);

        legacyCommandsAdd();

        var listener = new StatusListener(this.getSLF4JLogger());
        getServer().getPluginManager().registerEvents(listener, this);
        impl.configManager().addMonitored(listener);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    /**
     * Register all commands in the command manager.
     */
    public void legacyCommandsAdd() {
        impl.commandManagerV2().addLegacy(BlipCommand.class,
                UnmuteCommand.class,
                MuteCommand.class,
                SaveProfilesCommand.class,
                SetSpawnCommand.class,
                SaveProfilesCommand.class,
                SpawnCommand.class,
                SetSpawnCommand.class,
                WhoAmICommand.class,
                IgnoreCommand.class,
                DeafenCommand.class,
                SilenceCommand.class,
                StackPunCommand.class);
    }

    /**
     * Disables this plugin and cleans up everything.
     */
    @Override
    public void onDisable() {
        var profileManager = impl.profileManager();

        profileManager.save();
        // 禁止再写入，保存
        profileManager.prohibitWrite(true);
    }
}
