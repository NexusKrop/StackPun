/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.nexuskrop.stackpun.StackPun;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

@PunCommandInfo(name = "preference")
public class PreferenceCommand implements PunCommand {
    @Override
    public void initialise(CommandAPICommand command) {
        command.withArguments(new StringArgument("key"),
                        new BooleanArgument("value"))
                .executesPlayer((sender, args) -> {
                    var profile = StackPun.api().profileManager().get(sender);
                    profile.setPreferences((String) args[0], (boolean) args[1]);
                });
    }
}
