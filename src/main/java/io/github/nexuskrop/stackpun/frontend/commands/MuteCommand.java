/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements StackCommand {

    @Override
    public void register() {
        new CommandAPICommand("mute")
                .withPermission(CommandPermission.OP)
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::execute)
                .register();
    }

    public void execute(CommandSender sender, Object[] args) {
        var player = (Player) args[0];
        var profile = StackPun.instance().profileManager().getProfile(player);

        if (profile.muted) {
            sender.sendMessage(Component.text("The user is already muted!").color(NamedTextColor.RED));
        } else {
            profile.muted = true;
            StackPun.instance().profileManager().putProfile(player, profile);
            sender.sendMessage(Component.text("Muted ").append(player.displayName()));
        }
    }
}
