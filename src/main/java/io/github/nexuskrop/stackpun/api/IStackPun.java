/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.api;

import io.github.nexuskrop.stackpun.ConfigManager;
import io.github.nexuskrop.stackpun.data.ProfileManager;
import io.github.nexuskrop.stackpun.frontend.locale.MessageManager;
import io.github.nexuskrop.stackpun.players.ChatManager;
import io.github.nexuskrop.stackpun.players.PlayerManager;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import x.nexuskrop.stackpun.commands.CommandManager;
import x.nexuskrop.stackpun.net.NetworkManager;

/**
 * Represents the API interface for StackPun plugin.
 *
 * @author WithLithum
 * @see io.github.nexuskrop.stackpun.StackPun
 */
public interface IStackPun {
    /**
     * Gets a shared instance of the second generation command manager.
     *
     * @return An instanceof the second-generation command manager. Will always return the same instance.
     */
    CommandManager commandManagerV2();

    /**
     * Gets a shared instance of the {@link ChatManager}.
     *
     * @return An instance of {@link ChatManager}. Will always return the same instance.
     */
    ChatManager chatManager();

    /**
     * Gets a shared instance of the {@link ProfileManager}.
     *
     * @return An instance of {@link ProfileManager}. Will always return the same instance.
     */
    ProfileManager profileManager();

    /**
     * Gets a shared instance of the {@link MessageManager}.
     *
     * @return An instance of {@link MessageManager}. Will always return the same instance.
     */
    MessageManager messageManager();

    /**
     * Gets a shared instance of the {@link PlayerManager}.
     *
     * @return An instance of {@link PlayerManager}. Will always return the same instance.
     */
    PlayerManager playerManager();

    NetworkManager networkManager();

    /**
     * Gets the overworld.
     *
     * @return An instance of {@link World} representing the overworld.
     */
    @Nullable
    World overWorld();

    ConfigManager configManager();

    /**
     * Gets whether a game rule is enabled.
     *
     * @param gameRule The rule to check.
     * @param def      The default value if unable to acquire such rule.
     * @return The rule result.
     */
    boolean isGameRuleEnabled(GameRule<Boolean> gameRule, boolean def);
}
