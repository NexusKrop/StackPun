/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;

public class DeafenCommand implements StackCommand {
    private static final String SUCCESS_ON = "commands.deafen.success_on";
    private static final String SUCCESS_OFF = "commands.deafen.success_off";

    @Override
    public void register() {
        new CommandAPICommand("deaf")
                .withPermission("stackpun.commands.deaf")
                .withHelp("Deafen yourself", "Deafens yourself.")
                .executesPlayer((sender, args) -> {
                    var profile = StackPun.api().profileManager().getProfile(sender);

                    if (profile.deafened) {
                        profile.deafened = false;
                        StackCommand.sendSuccessLoc(sender, SUCCESS_OFF);
                    } else {
                        profile.deafened = true;
                        StackCommand.sendSuccessLoc(sender, SUCCESS_ON);
                    }

                    StackPun.api().profileManager().putProfile(sender, profile);
                });
    }
}
