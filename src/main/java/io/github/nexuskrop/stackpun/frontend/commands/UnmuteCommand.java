/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

public class UnmuteCommand implements StackCommand {
    private static final String UNMUTE_SELF_PERM = StackPun.cmdPerm("unmute.self");
    private static final String UNMUTE_SELF_DENIED = "commands.unmute.self_denied";
    private static final String SUCCESS = "commands.unmute.success";
    private static final String NOT_MUTED = "commands.ummute.not_muted";

    @Override
    public void register() {
        new CommandAPICommand("unmute")
                .withPermission(StackPun.cmdPerm("unmute"))
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::execute)
                .register();
    }

    public void execute(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        var player = (Player) args[0];
        var profile = StackPun.api().profileManager().get(player);

        if (!profile.isMuted()) {
            StackCommand.failLoc(NOT_MUTED);
            return;
        }

        if (player == sender || !player.hasPermission(UNMUTE_SELF_PERM)) {
            StackCommand.failLoc(UNMUTE_SELF_DENIED);
            return;
        }

        profile.setMuted(false);
        StackPun.api().profileManager().putProfile(player, profile);

        var feedback = MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(SUCCESS),
                Placeholder.component("victim", player.displayName()));
        CommandSenders.sendSuccess(sender, feedback);
    }
}
