/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.command.CommandSender;

public class StackPunCommand implements StackCommand {
    private static final String RELOAD_BEGIN = "commands.stackpun.reload_begin";
    private static final String RELOAD_SUCCESS = "commands.stackpun.reload_success";

    @Override
    public void register() {
        new CommandAPICommand("stackpun")
                .withHelp("StackPun command", "The general configuration command for StackPun")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("stackpun.commands.reload")
                        .withHelp("Reloads the config and messages", "Reloads all configuration and messages file.")
                        .executes(this::reloadExecute))
                .register();
    }

    private void reloadExecute(CommandSender sender, Object[] args) {
        StackCommand.sendMessageLoc(sender, RELOAD_BEGIN);
        StackPun.api().configManager().reload();
        StackPun.api().messageManager().reload();
        StackCommand.sendSuccessLoc(sender, RELOAD_SUCCESS);
    }
}
