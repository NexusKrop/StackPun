/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.util.Common;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface StackCommand {
    void register();

    String MESSAGE_MUTED = "commands.generic.muted";
    String MESSAGE_MUTED_YES = "commands.generic.muted.yes";
    String MESSAGE_MUTED_NO = "commands.generic.muted.no";

    static void sendError(CommandSender target, Component error) {
        target.sendMessage(error.color(NamedTextColor.RED));
    }

    static void sendErrorLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id).color(NamedTextColor.RED));
    }

    static void sendMessageLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id));
    }

    static void sendMessageVal(CommandSender target, String id, String val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Placeholder.unparsed("value", val)));
    }

    static void sendMessageVal(CommandSender target, String id, Component val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Placeholder.component("value", val)));
    }

    static void sendMessageVal(CommandSender target, String id, Entity val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Placeholder.component("value", Common.getEntityRepresent(val))));
    }

    static Component getMuted(Player player) {
        var profile = StackPun.api().profileManager().getProfile(player);

        if (profile.muted) {
            return MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(MESSAGE_MUTED_YES));
        } else {
            return MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(MESSAGE_MUTED_NO));
        }
    }
}
