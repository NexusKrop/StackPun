/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.data.ProfileManager;
import io.github.nexuskrop.stackpun.frontend.CommandManager;
import io.github.nexuskrop.stackpun.frontend.commands.*;
import io.github.nexuskrop.stackpun.frontend.locale.MessageManager;
import io.github.nexuskrop.stackpun.players.ChatManager;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

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

    private CommandManager commandManager;
    private ChatManager chatManager;
    private ProfileManager profileManager;
    private World overWorld;

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


    public CommandManager commandManager() {
        return commandManager;
    }

    public ProfileManager profileManager() {
        return profileManager;
    }

    public MessageManager messageManager() {
        return messageManager;
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
        overWorld = this.getServer().getWorld("overworld");

        getSLF4JLogger().info("StackPun Service instantiated");

        messageManager = new MessageManager(new File(this.getDataFolder(), "msg.properties"), this.getSLF4JLogger());

        chatManager = new ChatManager(this);

        profileManager = new ProfileManager(this);
        profileManager.init();

        commandManager = new CommandManager();
        // TODO: 反射解析自动注册
        initCommands();
    }

    /**
     * Register all commands in the command manager.
     */
    public void initCommands() {
        commandManager.registerCommand(new BlipCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new SaveProfilesCommand());
        commandManager.registerCommand(new SetSpawnCommand());
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new WhoAmICommand());
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
