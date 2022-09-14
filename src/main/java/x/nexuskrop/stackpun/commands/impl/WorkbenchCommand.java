/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

@PunCommandInfo(name = "workbench")
public class WorkbenchCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.withAliases("wb", "ct", "craft")
                .executesPlayer(this::execute);
    }

    public int execute(Player sender, Object[] args) {
        sender.openWorkbench(null, true);
        return 0;
    }
}
