/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

@PunCommandInfo(name = "troll")
public class TrollCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.withArguments(new PlayerArgument("victim"))
                .executes((sender, args) -> {
                    var player = (Player) args[0];
                    StackPun.api().networkManager().sendDemoScreen(player);

                    CommandSenders.sendSuccess(sender, "commands.generic.success");
                });
    }
}
