/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import org.bukkit.configuration.Configuration;

public final class ConfigManager {
    private final StackPun plugin;

    ConfigManager(StackPun stackPun) {
        this.plugin = stackPun;
    }

    public void reload() {
        plugin.reloadConfig();

        plugin.chatManager().clearCache();
    }

    public Configuration getConfig() {
        return plugin.getConfig();
    }
}
