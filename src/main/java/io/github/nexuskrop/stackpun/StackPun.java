/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.data.ProfileManager;
import io.github.nexuskrop.stackpun.frontend.commands.*;
import io.github.nexuskrop.stackpun.frontend.locale.MessageManager;
import io.github.nexuskrop.stackpun.players.ChatManager;
import io.github.nexuskrop.stackpun.players.PlayerManager;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import x.nexuskrop.stackpun.net.NetworkManager;
import x.nexuskrop.stackpun.net.StatusListener;

import java.io.File;

/**
 * Provides initialisation service and main API access point for the StackPun plugin.
 *
 * @author WithLithum
 * @apiNote Use {@code StackPun.instance()} to acquire a shared instance. Do not create a new
 * instance as it will mess up the environment.
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class StackPun extends JavaPlugin implements IStackPun {
    private static StackPun _instance;

    private x.nexuskrop.stackpun.commands.CommandManager commandManagerV2;
    private ChatManager chatManager;
    private ProfileManager profileManager;
    private PlayerManager playerManager;
    private World overWorld;
    private ConfigManager configManager;
    private NetworkManager networkManager;

    private MessageManager messageManager;

    private void setInstance(StackPun instance) {
        _instance = instance;
    }

    /**
     * Gets the API instance of this plugin.
     *
     * @return An instance of {@link StackPun} as {@link IStackPun}. Will always return the same
     * instance.
     */
    public static IStackPun api() {
        return _instance;
    }

    @Override
    public x.nexuskrop.stackpun.commands.CommandManager commandManagerV2() {
        return commandManagerV2;
    }

    public ProfileManager profileManager() {
        return profileManager;
    }

    public MessageManager messageManager() {
        return messageManager;
    }

    public ConfigManager configManager() {
        return configManager;
    }

    @Override
    public PlayerManager playerManager() {
        return playerManager;
    }

    public NetworkManager networkManager() {
        return networkManager;
    }

    @Override
    @Nullable
    public World overWorld() {
        return null;
    }

    @Override
    public boolean isGameRuleEnabled(GameRule<Boolean> gameRule, boolean def) {
        var ow = overWorld();
        if (ow != null) {
            return Boolean.TRUE.equals(ow.getGameRuleValue(gameRule));
        } else {
            return def;
        }
    }

    public ChatManager chatManager() {
        return chatManager;
    }

    public static String cmdPerm(String command) {
        return "stackpun.commands." + command;
    }

    /**
     * Enables this {@link JavaPlugin} and initialises this plugin.
     */
    @Override
    public void onEnable() {
        setInstance(this);
        this.saveDefaultConfig();

        configManager = new ConfigManager(this);
        overWorld = this.getServer().getWorld("overworld");

        getSLF4JLogger().info("StackPun Service instantiated");

        messageManager = new MessageManager(new File(this.getDataFolder(), "msg.properties"), this.getSLF4JLogger());

        chatManager = new ChatManager(this);

        profileManager = new ProfileManager(this);
        profileManager.init();

        playerManager = new PlayerManager(this);
        configManager.addMonitored(playerManager);

        networkManager = new NetworkManager(this);

        commandManagerV2 = new x.nexuskrop.stackpun.commands.CommandManager(this.getSLF4JLogger());
        commandManagerV2.addFromProject();

        legacyCommandsAdd();

        var listener = new StatusListener(this.getSLF4JLogger());
        getServer().getPluginManager().registerEvents(listener, this);
        configManager.addMonitored(listener);
    }

    /**
     * Register all commands in the command manager.
     */
    public void legacyCommandsAdd() {
        commandManagerV2.addLegacy(BlipCommand.class,
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
        profileManager.save();
        // 禁止再写入，保存
        profileManager.prohibitWrite(true);
    }
}
