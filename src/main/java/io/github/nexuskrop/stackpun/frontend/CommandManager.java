/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend;

import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager {
    private final List<StackCommand> commands = new ArrayList<>();

    public void registerCommand(@NotNull StackCommand instance) {
        Objects.requireNonNull(instance).register();
        commands.add(instance);
    }
}
