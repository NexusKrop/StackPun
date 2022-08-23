/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public interface StackCommand {
    void register();

    String MESSAGE_MUTED = "commands.generic.muted";

    static void sendError(CommandSender target, Component error) {
        target.sendMessage(error.color(NamedTextColor.RED));
    }

    static void sendErrorLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id).color(NamedTextColor.RED));
    }

    static void sendMessageLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id));
    }
}
