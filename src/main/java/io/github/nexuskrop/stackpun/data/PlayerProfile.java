/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

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
    public List<UUID> blockedPlayers = new ArrayList<>();
}
