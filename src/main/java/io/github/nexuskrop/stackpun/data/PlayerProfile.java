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

public class PlayerProfile {
    public PlayerProfile(String playerName) {
        name = playerName;
    }

    public PlayerProfile() {
        name = "___";
    }

    public final String name;
    public boolean muted;
    public boolean deafened;
    public boolean silenced;
    public boolean hadWelcomed;
    public boolean wasModded;
    public boolean wasNotifiedForModded;
    public List<UUID> blockedPlayers = new ArrayList<>();

    public transient ClientOption.ChatVisibility chatVisibility = ClientOption.ChatVisibility.FULL;
}
