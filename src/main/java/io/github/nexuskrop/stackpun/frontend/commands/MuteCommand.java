/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements StackCommand {

    private static final String ALREADY_MUTED = "commands.mute.already_muted";
    private static final String SUCCESS = "commands.mute.success";

    @Override
    public void register() {
        new CommandAPICommand("mute")
                .withPermission(StackPun.cmdPerm("mute"))
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::execute)
                .register();
    }

    public void execute(CommandSender sender, Object[] args) {
        var player = (Player) args[0];
        var profile = StackPun.api().profileManager().getProfile(player);

        if (profile.muted) {
            StackCommand.sendError(sender, StackPun.api().messageManager().getComp(ALREADY_MUTED));
        } else {
            profile.muted = true;
            StackPun.api().profileManager().putProfile(player, profile);
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    StackPun.api().messageManager().get(SUCCESS),
                    Placeholder.component("victim", player.displayName())));
        }
    }
}
