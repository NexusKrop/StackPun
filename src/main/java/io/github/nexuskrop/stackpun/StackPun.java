/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import io.github.nexuskrop.stackpun.data.ProfileManager;
import io.github.nexuskrop.stackpun.frontend.CommandManager;
import io.github.nexuskrop.stackpun.frontend.commands.BlipCommand;
import io.github.nexuskrop.stackpun.frontend.commands.MuteCommand;
import io.github.nexuskrop.stackpun.frontend.commands.SaveProfilesCommand;
import io.github.nexuskrop.stackpun.frontend.commands.UnmuteCommand;
import io.github.nexuskrop.stackpun.players.ChatManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides initialisation service for the StackPun plugin.
 *
 * @author WithLithum
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class StackPun extends JavaPlugin {
    private static StackPun _instance;

    private CommandManager commandManager;
    private ChatManager chatManager;
    private ProfileManager profileManager;

    private void setInstance(StackPun instance) {
        _instance = instance;
    }

    public static StackPun instance() {
        return _instance;
    }

    /**
     * Gets the command manager of this instance.
     *
     * @return The command manager.
     */
    public CommandManager commandManager() {
        return commandManager;
    }

    public ProfileManager profileManager() {
        return profileManager;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        getSLF4JLogger().info("StackPun Service instantiated");

        chatManager = new ChatManager(this);

        profileManager = new ProfileManager(this);
        profileManager.init();

        commandManager = new CommandManager();
        // TODO: 反射解析自动注册
        initCommands();
    }

    public void initCommands() {
        commandManager.registerCommand(new BlipCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new SaveProfilesCommand());
    }

    @Override
    public void onDisable() {
        profileManager.save();
        // 禁止再写入，保存
        profileManager.prohibitWrite(true);
    }
}
