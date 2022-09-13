/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import com.destroystokyo.paper.ClientOption;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

/**
 * A legacy command to allow a user to deafen themselves.
 */
public class DeafenCommand implements StackCommand {
    private static final String SUCCESS_ON = "commands.deafen.success_on";
    private static final String SUCCESS_OFF = "commands.deafen.success_off";
    private static final String CHAT_OFF = "commands.deafen.chat_off";

    @Override
    public void register() {
        new CommandAPICommand("deaf")
                .withPermission("stackpun.commands.deaf")
                .withHelp("Deafen yourself", "Deafens yourself.")
                .executesPlayer((sender, args) -> {
                    var profile = StackPun.api().profileManager().get(sender);

                    if (profile.getChatVisibility() != ClientOption.ChatVisibility.FULL) {
                        StackCommand.failLoc(CHAT_OFF);
                        return;
                    }

                    if (profile.isDeafened()) {
                        profile.setDeafened(false);
                        CommandSenders.sendSuccess(sender, SUCCESS_OFF);
                    } else {
                        profile.setDeafened(true);
                        CommandSenders.sendSuccess(sender, SUCCESS_ON);
                    }

                    StackPun.api().profileManager().put(sender, profile);
                });
    }
}
