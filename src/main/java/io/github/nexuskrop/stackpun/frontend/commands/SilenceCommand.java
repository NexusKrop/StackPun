/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;

public class SilenceCommand implements StackCommand {
    private static final String SUCCESS_ON = "commands.silence.success_on";
    private static final String SUCCESS_OFF = "commands.silence.success_off";

    @Override
    public void register() {
        new CommandAPICommand("silence")
                .withPermission("stackpun.commands.silence")
                .withHelp("Silences yourself", "Silences yourself.")
                .executesPlayer((sender, args) -> {
                    var profile = StackPun.api().profileManager().getProfile(sender);

                    if (profile.silenced) {
                        profile.silenced = false;
                        StackCommand.sendSuccessLoc(sender, SUCCESS_OFF);
                    } else {
                        profile.silenced = true;
                        StackCommand.sendSuccessLoc(sender, SUCCESS_ON);
                    }

                    StackPun.api().profileManager().putProfile(sender, profile);
                });
    }
}
