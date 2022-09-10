/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.util.Common;
import org.bukkit.command.CommandSender;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

public class StackPunCommand implements StackCommand {
    private static final String RELOAD_BEGIN = "commands.stackpun.reload_begin";
    private static final String RELOAD_SUCCESS = "commands.stackpun.reload_success";
    private static final String VERSION_APP_NAME = "commands.version.appName";
    private static final String VERSION_ATTRIBUTE = "commands.version.attribute";
    private static final String VERSION_VER = "commands.version.ver";
    private static final String VERSION_NO_PRODUCT = "commands.version.no_product";

    @Override
    public void register() {
        new CommandAPICommand("stackpun")
                .withHelp("StackPun command", "The general configuration command for StackPun")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("stackpun.commands.reload")
                        .withHelp("Reloads the config and messages", "Reloads all configuration and messages file.")
                        .executes(this::reloadExecute))
                .withSubcommand(new CommandAPICommand("version")
                        .withPermission("stackpun.commands.version")
                        .executes(this::versionExecute))
                .register();
    }

    private void versionExecute(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        var product = Common.productData();

        if (product == null) {
            StackCommand.failLoc(VERSION_NO_PRODUCT);
            return;
        }

        StackCommand.sendMessageVal(sender, VERSION_APP_NAME, Common.APP_NAME);
        StackCommand.sendMessageVal(sender, VERSION_ATTRIBUTE, Common.AUTHOR);
        StackCommand.sendMessageVal(sender, VERSION_VER, product.version);
    }

    private void reloadExecute(CommandSender sender, Object[] args) {
        StackCommand.sendMessageLoc(sender, RELOAD_BEGIN);
        StackPun.api().configManager().reload();
        StackPun.api().messageManager().reload();
        CommandSenders.sendSuccess(sender, RELOAD_SUCCESS);
    }
}
