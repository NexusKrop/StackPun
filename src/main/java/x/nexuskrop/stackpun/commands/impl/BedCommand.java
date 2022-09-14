/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

/**
 * Represents a command ({@code /bed}) that takes the player to their personal spawn point.
 */
@PunCommandInfo(name = "bed")
public class BedCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.executesPlayer(this::execute);
    }


    public int execute(Player sender, Object[] args) {
        var loc = sender.getBedSpawnLocation();
        if (loc == null) {
            sender.sendMessage(Component.translatable("block.minecraft.spawn.not_valid").color(NamedTextColor.RED));
            return 0;
        }

        sender.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
        return 1;
    }
}
