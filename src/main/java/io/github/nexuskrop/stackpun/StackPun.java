/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.frontend.commands.*;
import io.github.nexuskrop.stackpun.worlds.GameEventsHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
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
@Plugin(name = "StackPun", version = "0.1.3")
@Description("A NexusKrop plugin to reborn the Stack project")
@Author("NexusKrop Project")
@ApiVersion(ApiVersion.Target.v1_19)
@Dependency("CommandAPI")
@Dependency("ProtocolLib")
@LogPrefix("StaP")
public class StackPun extends JavaPlugin {
    private static StackPunImpl impl;
    private static PluginDescriptionFile descriptionFile;

    private static void setInstance(StackPun instance) {
        impl = new StackPunImpl(instance);
        initialised = true;
        descriptionFile = instance.getDescription();
    }

    public static PluginDescriptionFile description() {
        return descriptionFile;
    }

    private static boolean initialised;

    /**
     * Gets the API instance of this plugin.
     *
     * @return An instance of {@link StackPun} as {@link IStackPun}. Will always return the same
     * instance.
     * @throws IllegalStateException The StackPun API is not initialised.
     */
    public static @NotNull IStackPun api() {
        if (!initialised) {
            Bukkit.getServer().getLogger().info("NOTE: StackPun API cannot be called if it is not yet initialised.");
            Bukkit.getServer().getLogger().info("NOTE: DO NOT access StackPun API in-line at declaration or in constructor.");
            throw new IllegalStateException("StackPun API is not yet initialised.");
        }

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
        getServer().getPluginManager().registerEvents(new GameEventsHandler(), this);
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
        if (!initialised) {
            getSLF4JLogger().warn("Disabled before initialisation is ready. This suggests exception being thrown WHILE the plugin is loading.");
            getSLF4JLogger().warn("Check your stacktrace and fix the issue (usually in one of the managers).");
            return;
        }

        var profileManager = impl.profileManager();

        profileManager.save();
        // 禁止再写入，保存
        profileManager.prohibitWrite(true);
    }
}
