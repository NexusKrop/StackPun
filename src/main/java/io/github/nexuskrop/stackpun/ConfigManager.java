/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun;

import org.bukkit.configuration.Configuration;
import x.nexuskrop.stackpun.util.IReloadable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ConfigManager {
    private final StackPun plugin;
    private final List<IReloadable> monitoring = new ArrayList<>();

    public ConfigManager(StackPun stackPun) {
        this.plugin = stackPun;
    }

    public void reload() {
        plugin.reloadConfig();

        plugin.chatManager().clearCache();
        for (var monitored : monitoring) {
            monitored.reload();
        }
    }

    public void addMonitored(IReloadable reloadable) {
        monitoring.add(Objects.requireNonNull(reloadable));
    }

    public Configuration getConfig() {
        return plugin.getConfig();
    }
}
