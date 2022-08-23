/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;

public class SaveProfilesCommand implements StackCommand {
    private static final String BEGIN = "commands.save-profiles.begin";
    private static final String SUCCESS = "commands.save-profiles.success";

    @Override
    public void register() {
        new CommandAPICommand("save-profiles")
                .withPermission(StackPun.cmdPerm("save-profiles"))
                .executes((sender, args) -> {
                    StackCommand.sendMessageLoc(sender, BEGIN);
                    StackPun.api().profileManager().save();
                    StackCommand.sendMessageLoc(sender, SUCCESS);
                })
                .register();
    }
}
