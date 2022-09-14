/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

import com.destroystokyo.paper.ClientOption;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a player profile.
 *
 * @author WithLithum
 * @see ProfileManager
 */
public class PlayerProfile implements Serializable {
    private boolean muted;

    private boolean deafened;

    private boolean silenced;

    private final Map<String, Boolean> preferences = new HashMap<>();

    private final List<UUID> blockedPlayers = new ArrayList<>();

    private transient ClientOption.ChatVisibility chatVisibility = ClientOption.ChatVisibility.FULL;

    /**
     * Whether this user was muted.
     *
     * @see io.github.nexuskrop.stackpun.frontend.commands.MuteCommand
     */
    public boolean isMuted() {
        return muted;
    }

    public boolean getPreference(String id) {
        if (!preferences.containsKey(id)) {
            return false;
        }

        return preferences.get(id);
    }

    /**
     * Stub for setting preferences.
     *
     * @param id    The ID.
     * @param value The value.
     * @deprecated In favour of {@link PlayerProfile#putPreferences(String, boolean)}
     */
    @Deprecated(since = "0.1.3-alpha", forRemoval = true)
    public void setPreferences(String id, boolean value) {
        putPreferences(id, value);
    }

    /**
     * Sets the specified preference identified by the specified ID to the specified value.
     *
     * @param id    The ID of the preference to set.
     * @param value The value to set to.
     */
    public void putPreferences(@NotNull String id, boolean value) {
        preferences.put(id, value);
    }

    public void isMuted(boolean muted) {
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

    public void isDeafened(boolean deafened) {
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

    public void isSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    /**
     * A list of all blocked players.
     */
    public List<UUID> blockedPlayers() {
        return blockedPlayers;
    }

    /**
     * Gets the chat visibility option of the client of the player associated with this instance.
     *
     * @return The chat visibility option of the client of the player associated with this instance.
     */
    public ClientOption.ChatVisibility chatVisibility() {
        return chatVisibility;
    }

    /**
     * Sets the chat visibility option of the client of the player associated with this instance.
     *
     * @param chatVisibility The value to set to.
     * @apiNote Do not call this API if you are not the manager who is responsible for updating this,
     * otherwise you screw up this config until the player changes their settings.
     */
    public void chatVisibility(ClientOption.ChatVisibility chatVisibility) {
        this.chatVisibility = chatVisibility;
    }
}
