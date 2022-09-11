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

    /**
     * This sender may have unexpected behaviour.
     *
     * @param target    The target of the message.
     * @param component The component to send.
     * @deprecated In favour of {@link x.nexuskrop.stackpun.commands.intf.CommandSenders#sendSuccess(CommandSender, String)}
     */
    @Deprecated(since = "0.1.1-alpha", forRemoval = true)
    static void sendSuccess(CommandSender target, Component component) {
        if (isFeedbackEnabled()) {
            target.sendMessage(component);
            broadcastSuccess(target, component);
        }
    }

    /**
     * @deprecated In favour of {@link x.nexuskrop.stackpun.commands.intf.CommandSenders#sendSuccess(CommandSender, Component)}
     */
    @Deprecated(since = "0.1.1-alpha", forRemoval = true)
    static void sendSuccessVal(CommandSender target, String id, Component val) {
        if (isFeedbackEnabled()) {
            var msg = MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                    Common.valueComponent(val));

            target.sendMessage(msg);
            broadcastSuccess(target, msg);
        }
    }

    /**
     * @param target The target of the message.
     * @param id     The ID of the localised message to send.
     * @deprecated In favour of {@link x.nexuskrop.stackpun.commands.intf.CommandSenders#sendSuccess(CommandSender, String)}
     */
    @Deprecated(since = "0.1.1-alpha", forRemoval = true)
    static void sendSuccessLoc(CommandSender target, String id) {
        sendSuccess(target, StackPun.api().messageManager().getMini(id));
    }

    static void broadcastSuccess(CommandSender source, Component component) {
        // 合成消息（chat.type.admin）
        var msg = Component.translatable("chat.type.admin")
                .args(source.name(), component).color(NamedTextColor.GRAY)
                .decorate(TextDecoration.ITALIC);

        // 检查游戏规则 sendCommandFeedback 是否启用
        if (StackPun.api().isGameRuleEnabled(GameRule.SEND_COMMAND_FEEDBACK, true)) {
            // 如果启用，遍历所有人
            for (var player :
                    Bukkit.getServer().getOnlinePlayers()) {
                // 有权限且不是命令的执行者
                if (!player.getName().equals(source.getName()) && player.hasPermission("stackpun.commands.generic.broadcast")) {
                    // 发送消息
                    player.sendMessage(msg);
                }
            }

            if (!(source instanceof ConsoleCommandSender)) {
                Bukkit.getServer().sendMessage(msg);
            }
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
