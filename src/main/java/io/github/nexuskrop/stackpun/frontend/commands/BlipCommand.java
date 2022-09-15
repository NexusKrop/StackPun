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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A legacy command to allow a player to blip (ping) another player.
 *
 * @author WithLithum
 */
public class BlipCommand implements StackCommand {
    private static final String BLIPPED = "commands.blip.blipped";
    private static final Sound BLIPPED_SOUND = Sound.sound(Key.key("minecraft",
                    "entity.experience_orb.pickup"),
            Sound.Source.MASTER, 1f, 1f);
    private static final String DEAF = "commands.blip.deafened";
    private static final String SILENCED = "commands.blip.silenced";

    @Override
    public void register() {
        new CommandAPICommand("blip")
                .withPermission("stackpun.commands.blip")
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::executePlayer)
                .register();
    }

    public void executePlayer(Player player, Object[] args) throws WrapperCommandSyntaxException {
        var profile = StackPun.api().profileManager().get(player);

        // 检查玩家是否被禁言
        if (profile.isMuted()) {
            // 如果被禁言，拦截操作并提示
            StackCommand.failLoc(StackCommand.MESSAGE_MUTED);
        }

        if (profile.isSilenced()) {
            StackCommand.failLoc(SILENCED);
        }

        execute(player, args);
    }

    public void execute(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        var player = (Player) args[0];

        var profile = StackPun.api().profileManager().get(player);

        if (profile.isDeafened()) {
            StackCommand.failLoc(DEAF);
        }

        // 发送提示消息
        player.sendMessage(MiniMessage.miniMessage().deserialize(
                StackPun.api().messageManager().get(BLIPPED),
                Placeholder.component("source", sender.name())));

        // 播放提示音
        player.playSound(BLIPPED_SOUND);
    }
}
