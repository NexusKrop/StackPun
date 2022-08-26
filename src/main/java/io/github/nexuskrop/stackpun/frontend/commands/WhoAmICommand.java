/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.util.Common;

public class WhoAmICommand implements StackCommand {
    private static final String LINE_1 = "commands.whoami.line1";
    private static final String LINE_2 = "commands.whoami.line2";
    private static final String LINE_3 = "commands.whoami.line3";
    private static final String LINE_4 = "commands.whoami.line4";

    @Override
    public void register() {
        new CommandAPICommand("whoami")
                .withPermission("stackpun.commands.whoami")
                .withFullDescription("See your information")
                .executesPlayer((sender, args) -> {
                    StackCommand.sendMessageLoc(sender, LINE_1);
                    StackCommand.sendMessageVal(sender, LINE_2, Common.getEntityRepresent(sender));
                    StackCommand.sendMessageVal(sender, LINE_3, StackCommand.getMuted(sender));
                    StackCommand.sendMessageVal(sender, LINE_4, sender.getClientBrandName());
                }).register();
    }
}
