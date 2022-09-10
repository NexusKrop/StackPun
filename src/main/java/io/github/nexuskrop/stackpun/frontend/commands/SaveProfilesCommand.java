/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;

import java.util.concurrent.CompletableFuture;

/**
 * A legacy command allowing operators to save all profiles.
 */
public class SaveProfilesCommand implements StackCommand {
    private static final String BEGIN = "commands.save-profiles.begin";

    @Override
    public void register() {
        new CommandAPICommand("save-profiles")
                .withPermission(StackPun.cmdPerm("save-profiles"))
                .executes((sender, args) -> {
                    StackCommand.sendMessageLoc(sender, BEGIN);
                    CompletableFuture.runAsync(() -> StackPun.api().profileManager().save());
                })
                .register();
    }
}
