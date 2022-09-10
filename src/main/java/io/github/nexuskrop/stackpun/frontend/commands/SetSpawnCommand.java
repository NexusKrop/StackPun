/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

/**
 * A legacy command to allow a user to set the world spawn point to the current location of the user.
 */
public class SetSpawnCommand implements StackCommand {
    private static final String SUCCESS = "commands.setspawn.success";

    @Override
    public void register() {
        new CommandAPICommand("setspawn")
                .withFullDescription("Sets the spawn point to the current position of player")
                .withPermission("stackpun.commands.setspawn")
                .executesPlayer((sender, args) -> {
                    sender.getWorld().setSpawnLocation(sender.getLocation());
                    CommandSenders.sendSuccess(sender, SUCCESS);
                }).register();
    }
}
