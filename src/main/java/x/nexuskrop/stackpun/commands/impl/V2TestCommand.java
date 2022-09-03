/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

@PunCommandInfo(name = "v2test")
public class V2TestCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.executes((sender, args) -> {
            sender.sendMessage("Hello World!");
        });
    }
}
