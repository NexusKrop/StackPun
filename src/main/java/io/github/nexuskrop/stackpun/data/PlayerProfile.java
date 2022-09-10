/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

import com.destroystokyo.paper.ClientOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player profile.
 *
 * @author WithLithum
 * @see ProfileManager
 */
public class PlayerProfile implements Serializable {
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

    private final String name;

    private boolean muted;

    private boolean deafened;

    private boolean silenced;

    private boolean hadWelcomed;

    private final List<UUID> blockedPlayers = new ArrayList<>();

    private transient ClientOption.ChatVisibility chatVisibility = ClientOption.ChatVisibility.FULL;

    /**
     * The name of the player when registering this player profile.
     */
    private String getName() {
        return name;
    }

    /**
     * Whether this user was muted.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.MuteCommand
     */
    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Whether this user was deafened.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.DeafenCommand
     */
    public boolean isDeafened() {
        return deafened;
    }

    public void setDeafened(boolean deafened) {
        this.deafened = deafened;
    }

    /**
     * Whether this user was silenced.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.SilenceCommand
     */
    public boolean isSilenced() {
        return silenced;
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    /**
     * Whether this user has had welcome message displayed.
     */
    public boolean hadWelcomed() {
        return hadWelcomed;
    }

    public void setHadWelcomed(boolean hadWelcomed) {
        this.hadWelcomed = hadWelcomed;
    }

    /**
     * A list of all blocked players.
     */
    public List<UUID> getBlockedPlayers() {
        return blockedPlayers;
    }

    /**
     * The chat visibility of this instance.
     */
    public ClientOption.ChatVisibility getChatVisibility() {
        return chatVisibility;
    }

    public void setChatVisibility(ClientOption.ChatVisibility chatVisibility) {
        this.chatVisibility = chatVisibility;
    }
}
