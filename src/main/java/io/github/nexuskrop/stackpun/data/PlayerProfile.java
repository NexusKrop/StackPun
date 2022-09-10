/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

import com.destroystokyo.paper.ClientOption;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player profile.
 *
 * @author WithLithum
 * @see ProfileManager
 */
public class PlayerProfile {
    /**
     * Initialises a new instance of the {@link ProfileManager} class.
     *
     * @param playerName The name of the player.
     */
    public PlayerProfile(String playerName) {
        name = playerName;
    }

    /**
     * Initialises a new instance of the {@link ProfileManager} class.
     */
    public PlayerProfile() {
        name = "___";
    }

    /**
     * The name of the player when registering this player profile.
     */
    public final String name;

    /**
     * Whether this user was muted.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.MuteCommand
     */
    public boolean muted;

    /**
     * Whether this user was deafened.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.DeafenCommand
     */
    public boolean deafened;

    /**
     * Whether this user was silenced.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.SilenceCommand
     */
    public boolean silenced;

    /**
     * Whether this user has had welcome message displayed.
     */
    public boolean hadWelcomed;

    /**
     * A list of all blocked players.
     */
    public List<UUID> blockedPlayers = new ArrayList<>();

    /**
     * The chat visibility of this instance.
     */
    public transient ClientOption.ChatVisibility chatVisibility = ClientOption.ChatVisibility.FULL;
}
