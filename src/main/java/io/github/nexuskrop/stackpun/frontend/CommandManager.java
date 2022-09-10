/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend;

import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The legacy command manager.
 *
 * @deprecated In favour of {@link x.nexuskrop.stackpun.commands.CommandManager}
 */
@Deprecated(forRemoval = true, since = "0.1.2")
public class CommandManager {
    private final List<StackCommand> commands = new ArrayList<>();

    public void registerCommand(@NotNull StackCommand instance) {
        Objects.requireNonNull(instance).register();
        Bukkit.getServer().getLogger().info("StaP registering command " + instance.getClass().getName());
        commands.add(instance);
    }
}
