/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Player;

/**
 * A legacy command to allow users to ignore another player.
 *
 * @deprecated The command is deprecated.
 */
@Deprecated(forRemoval = true, since = "0.1.4-alpha")
public class IgnoreCommand implements StackCommand {
    @Override
    public void register() {
        new CommandAPICommand("ignore")
                .withShortDescription("Deprecated.")
                .withFullDescription("Deprecated. Previously, this command adds or removes players from your ignore list.")
                .withPermission("stackpun.commands.ignore")
                .withArguments(new PlayerArgument("victim"))
                .executesPlayer(this::execute);
    }

    public void execute(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        throw CommandAPI.fail("Command deprecated");
    }
}
