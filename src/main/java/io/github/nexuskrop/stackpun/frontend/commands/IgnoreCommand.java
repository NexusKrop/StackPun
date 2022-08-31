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
import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.entity.Player;

public class IgnoreCommand implements StackCommand {
    private static final String OP_DENIED = "commands.ignore.op_denied";
    private static final String SUCCESS_REMOVE = "commands.ignore.success.remove";
    private static final String SUCCESS_ADD = "commands.ignore.success.add";

    @Override
    public void register() {
        new CommandAPICommand("ignore")
                .withShortDescription("Ignore or un-ignore player")
                .withFullDescription("Adds or removes players from your ignore list.")
                .withPermission("stackpun.commands.ignore")
                .withArguments(new PlayerArgument("victim"))
                .executesPlayer(this::execute);
    }

    public void execute(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        var player = (Player) args[0];
        var profile = StackPun.api().profileManager().getProfile(sender);

        // 禁止执行者自我拉黑
        if (player == sender) {
            StackCommand.failLoc(MESSAGE_SPECIFY_SELF_FAILURE);
            return;
        }

        // 检查执行者是否有拉黑管理员权限
        if (!sender.hasPermission("stackpun.commands.ignore.ops") && player.hasPermission("stackpun.commands.ignore.as-op")) {
            throw CommandAPI.fail(String.format(StackCommand.loc(OP_DENIED), sender.getName()));
        }

        // 获取 UUID
        var uuid = player.getUniqueId();

        // 检查是否目标已经拉黑
        if (profile.blockedPlayers.contains(uuid)) {
            // 如果已经拉黑，移出黑名单
            profile.blockedPlayers.remove(uuid);
            StackCommand.sendSuccessVal(sender, SUCCESS_REMOVE, player.displayName());
        } else {
            // 否则便加入黑名单
            profile.blockedPlayers.add(uuid);
            StackCommand.sendSuccessVal(sender, SUCCESS_ADD, player.displayName());
        }
    }
}
