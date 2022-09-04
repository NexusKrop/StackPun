/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.intf;

import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class CommandSenders {
    private CommandSenders() {
    }

    /**
     * Sends a message to the sender indicating a command success.
     *
     * @param target The target to send the message.
     * @param id     The identifier of the message.
     */
    public static void sendSuccess(CommandSender target, @NotNull String id) {
        if (StackPun.api().isGameRuleEnabled(GameRule.SEND_COMMAND_FEEDBACK, true)) {
            var msg = StackPun.api().messageManager().getMini(id);

            target.sendMessage(msg);
            StackPun.api().chatManager().broadcastSuccess(target, msg);
        }
    }
}
