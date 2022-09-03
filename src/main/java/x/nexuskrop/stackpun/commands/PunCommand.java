/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands;

import dev.jorel.commandapi.CommandAPICommand;

public interface PunCommand {
    void initialise(CommandAPICommand command);
}
