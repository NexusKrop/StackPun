/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

/**
 * Represents a command {@code /anvil} that opens the anvil GUI.
 */
@PunCommandInfo(name = "anvil")
public class AnvilCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.executesPlayer(((sender, args) -> {
            sender.openAnvil(null, true);
        }));
    }
}
