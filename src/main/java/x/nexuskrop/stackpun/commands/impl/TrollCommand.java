/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

@PunCommandInfo(name = "troll")
public class TrollCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.withArguments(new PlayerArgument("victim"))
                .executes((sender, args) -> {
                    var player = (Player) args[0];
                    StackPun.api().networkManager().sendDemoScreen(player);

                    StackCommand.sendSuccessLoc(sender, "commands.generic.success");
                });
    }
}
