/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import x.nexuskrop.stackpun.commands.intf.CommandSenders;

/**
 * A legacy command allowing an operator to mute a player.
 */
public class MuteCommand implements StackCommand {

    private static final String ALREADY_MUTED = "commands.mute.already_muted";
    private static final String SUCCESS = "commands.mute.success";

    @Override
    public void register() {
        new CommandAPICommand("mute")
                .withPermission("stackpun.commands.mute")
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::execute)
                .register();
    }

    public void execute(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        // 获取玩家和资料文件
        var player = (Player) args[0];
        var profile = StackPun.api().profileManager().get(player);

        // 检查是否已禁言
        if (profile.isMuted()) {
            // 如果已经禁言，提示并返回
            StackCommand.failLoc(ALREADY_MUTED);
        } else {
            // 设置资料内禁言为 true
            profile.setMuted(true);
            StackPun.api().profileManager().put(player, profile);
            CommandSenders.sendSuccess(sender, MiniMessage.miniMessage().deserialize(
                    StackPun.api().messageManager().get(SUCCESS),
                    Placeholder.component("victim", player.displayName())));
        }
    }
}
