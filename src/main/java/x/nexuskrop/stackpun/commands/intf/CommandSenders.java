/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.intf;

import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.util.Worlds;

public final class CommandSenders {
    private CommandSenders() {
    }

    private static final String SEND_COMMAND_FEEDBACK = GameRule.SEND_COMMAND_FEEDBACK.getName();

    /**
     * Sends a message to the sender indicating a command success.
     *
     * @param target The target to send the message.
     * @param id     The identifier of the message.
     */
    public static void sendSuccess(CommandSender target, @NotNull String id) {
        sendSuccess(target, StackPun.api().messageManager().getMini(id));
    }

    public static void sendSuccess(CommandSender target, @NotNull Component component) {
        if (Worlds.overworld().isGameRule(SEND_COMMAND_FEEDBACK)) {
            target.sendMessage(component);

            var msg = Component.translatable("chat.type.admin")
                    .args(target.name(), component).color(NamedTextColor.GRAY)
                    .decorate(TextDecoration.ITALIC);
            ComponentLogger.logger().info(msg);
        }
    }

    public static void sendSuccess(CommandSender target, @NotNull String id, @NotNull Component value) {
        if (Worlds.overworld().isGameRule(SEND_COMMAND_FEEDBACK)) {
            var msg = MiniMessage.miniMessage().deserialize(StackPun.api().messageManager().get(id),
                    Placeholder.component("value", value));

            target.sendMessage(msg);
            var trg = Component.translatable("chat.type.admin")
                    .args(target.name(), msg).color(NamedTextColor.GRAY)
                    .decorate(TextDecoration.ITALIC);
            ComponentLogger.logger().info(trg);
        }
    }
}
