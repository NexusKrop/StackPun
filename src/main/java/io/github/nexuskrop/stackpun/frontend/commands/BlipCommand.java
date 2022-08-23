/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlipCommand implements StackCommand {
    @Override
    public void register() {
        new CommandAPICommand("blip")
                .withPermission(StackPun.cmdPerm("blip"))
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::executePlayer)
                .register();
    }

    public void executePlayer(Player player, Object[] args) {
        if (StackPun.api().profileManager().getProfile(player).muted) {
            player.sendMessage(Component.text("You are currently muted").color(NamedTextColor.RED));
            return;
        }

        execute(player, args);
    }

    public void execute(CommandSender sender, Object[] args) {
        var player = (Player) args[0];
        player.sendMessage(sender.name().append(Component.text(" blips you.")));
    }
}
