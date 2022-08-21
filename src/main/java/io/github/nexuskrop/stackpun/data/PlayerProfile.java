/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

public class PlayerProfile {
    public PlayerProfile(String playerName) {
        name = playerName;
    }

    public final String name;
    public boolean muted;
}
