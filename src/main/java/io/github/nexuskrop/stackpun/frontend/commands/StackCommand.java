/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.util.Common;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface StackCommand {
    void register();

    String MESSAGE_MUTED = "commands.generic.muted";
    String MESSAGE_MUTED_YES = "commands.generic.muted.yes";
    String MESSAGE_MUTED_NO = "commands.generic.muted.no";
    String MESSAGE_SPECIFY_SELF_FAILURE = "commands.generic.self_specify_failure";

    static void sendErrorLoc(CommandSender target, String id) {
        target.sendMessage(StackPun.api().messageManager().getComp(id).color(NamedTextColor.RED));
    }

    static void failLoc(String id) throws WrapperCommandSyntaxException {
        throw CommandAPI.fail(loc(id));
    }

    static String loc(String id) {
        return StackPun.api().messageManager().get(id);
    }

    static void sendMessageLoc(CommandSender target, String id) {
        target.sendRichMessage(StackPun.api().messageManager().get(id));
    }

    static void sendMessageVal(CommandSender target, String id, String val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Common.valueLiteral(val)));
    }

    static void sendMessageVal(CommandSender target, String id, Component val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Common.valueComponent(val)));
    }

    static void sendMessageVal(CommandSender target, String id, Entity val) {
        target.sendMessage(MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                Common.valueComponent(Common.getEntityRepresent(val))));
    }

    static Component getMuted(Player player) {
        var profile = StackPun.api().profileManager().get(player);

        if (profile.isMuted()) {
            return MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(MESSAGE_MUTED_YES));
        } else {
            return MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(MESSAGE_MUTED_NO));
        }
    }
}
