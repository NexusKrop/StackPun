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
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
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

    static void sendSuccess(CommandSender target, Component component) {
        if (isFeedbackEnabled()) {
            target.sendMessage(component);
            broadcastSuccess(target, component);
        }
    }

    static void sendSuccessLoc(CommandSender target, String id) {
        sendSuccess(target, StackPun.api().messageManager().getMini(id));
    }

    static void broadcastSuccess(CommandSender source, Component component) {
        var msg = Component.translatable("chat.type.admin")
                .args(source.name(), component).color(NamedTextColor.GRAY)
                .decorate(TextDecoration.ITALIC);

        if (StackPun.api().isGameRuleEnabled(GameRule.SEND_COMMAND_FEEDBACK, true)) {
            for (var player :
                    Bukkit.getServer().getOnlinePlayers()) {
                if (player != source && player.hasPermission("stackpun.commands.generic.broadcast")) {
                    player.sendMessage(msg);
                }
            }

            Bukkit.getServer().sendMessage(msg);
        }
    }

    static boolean isFeedbackEnabled() {
        var ow = StackPun.api().overWorld();
        if (ow != null) {
            return Boolean.TRUE.equals(ow.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK));
        } else {
            return true;
        }
    }

    static void sendErrorLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id).color(NamedTextColor.RED));
    }

    static void sendMessageLoc(CommandSender target, String id) {
        target.sendRichMessage(StackPun.api().messageManager().get(id));
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
