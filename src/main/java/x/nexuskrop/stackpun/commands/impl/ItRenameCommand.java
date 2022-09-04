/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.PunCommand;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

@PunCommandInfo(name = "itrename")
public class ItRenameCommand implements PunCommand {
    private static final String NO_ITEM = "commands.itrename.no_item";
    private static final String NAME_TOO_LONG = "commands.itrename.name_too_long";
    private static final String SUCCESS = "commands.itrename.success";

    @Override
    public void initialise(CommandAPICommand command) {
        command.withArguments(new TextArgument("newName")).executesPlayer(this::execute);
    }

    private void execute(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        var target = (String) args[0];
        if (target.length() > 20) {
            StackCommand.failLoc(NAME_TOO_LONG);
        }

        var handStack = sender.getInventory().getItemInMainHand();
        if (handStack.getType() == Material.AIR) {
            StackCommand.failLoc(NO_ITEM);
            return;
        }

        handStack.getItemMeta().displayName(LegacyComponentSerializer.legacy('&')
                .deserialize(target));
        CommandSenders.sendSuccess(sender, SUCCESS);
    }
}
