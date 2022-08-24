/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnCommand implements StackCommand {

    private static final String SUCCESS = "commands.spawn.success";

    @Override
    public void register() {
        new CommandAPICommand("spawn")
                .withFullDescription("Teleports to spawn")
                .withPermission("stackpun.commands.spawn")
                .executesPlayer((sender, args) -> {
                    sender.teleport(sender.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                    StackCommand.sendSuccessLoc(sender, SUCCESS);
                })
                .register();
    }
}
