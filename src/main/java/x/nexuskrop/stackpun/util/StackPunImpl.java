/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.util;

import io.github.nexuskrop.stackpun.ConfigManager;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.data.ProfileManager;
import io.github.nexuskrop.stackpun.frontend.locale.MessageManager;
import io.github.nexuskrop.stackpun.players.ChatManager;
import io.github.nexuskrop.stackpun.players.PlayerManager;
import x.nexuskrop.stackpun.commands.CommandManager;
import x.nexuskrop.stackpun.net.NetworkManager;

import java.io.File;

/**
 * Implementation of the {@link IStackPun} interface.
 * This is not part of the API, therefore you should not create this.
 * To get an instance of this class, use {@link StackPun#api()}.
 */
public class StackPunImpl implements IStackPun {
    private final CommandManager commandManager;
    private final ChatManager chatManager;
    private final ProfileManager profileManager;
    private final MessageManager messageManager;
    private final PlayerManager playerManager;
    private final NetworkManager networkManager;
    private final ConfigManager configManager;

    /**
     * Initialises an instance of the {@link StackPunImpl} class.
     *
     * @param plugin The plugin.
     */
    public StackPunImpl(StackPun plugin) {
        commandManager = new CommandManager(plugin.getSLF4JLogger());
        chatManager = new ChatManager();
        profileManager = new ProfileManager(plugin.getSLF4JLogger());
        messageManager = new MessageManager(new File(plugin.getDataFolder(), "msg.properties"), plugin.getSLF4JLogger());
        playerManager = new PlayerManager(plugin.getSLF4JLogger());
        networkManager = new NetworkManager(plugin.getSLF4JLogger());
        configManager = new ConfigManager(plugin);
    }

    /**
     * Initialises this instance.
     */
    public void initialise(StackPun self) {
        self.getSLF4JLogger().info("Initialising StackPun managers");
        profileManager.init(self);
        messageManager.tryInit();
        chatManager.init(self);
        playerManager.init(self);

        commandManager.addFromProject();

        configManager.addMonitored(playerManager);
        self.getSLF4JLogger().info("Finished StackPun managers initialisation");
    }

    @Override
    public CommandManager commandManagerV2() {
        return commandManager;
    }

    @Override
    public ChatManager chatManager() {
        return chatManager;
    }

    @Override
    public ProfileManager profileManager() {
        return profileManager;
    }

    @Override
    public MessageManager messageManager() {
        return messageManager;
    }

    @Override
    public PlayerManager playerManager() {
        return playerManager;
    }

    @Override
    public NetworkManager networkManager() {
        return networkManager;
    }

    @Override
    public ConfigManager configManager() {
        return configManager;
    }
}