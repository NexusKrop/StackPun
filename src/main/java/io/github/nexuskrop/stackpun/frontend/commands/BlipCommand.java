/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlipCommand implements StackCommand {
    private static final String BLIPPED = "commands.blip.blipped";
    private static final Sound BLIPPED_SOUND = Sound.sound(Key.key("minecraft",
                    "entity.experience_orb.pickup"),
            Sound.Source.MASTER, 1f, 1f);

    @Override
    public void register() {
        new CommandAPICommand("blip")
                .withPermission(StackPun.cmdPerm("blip"))
                .withArguments(new PlayerArgument("target"))
                .executesConsole(this::execute)
                .executesPlayer(this::executePlayer)
                .register();
    }

    public void executePlayer(Player player, Object[] args) {
        if (StackPun.api().profileManager().getProfile(player).muted) {
            StackCommand.sendErrorLoc(player, StackCommand.MESSAGE_MUTED);
            return;
        }

        execute(player, args);
    }

    public void execute(CommandSender sender, Object[] args) {
        var player = (Player) args[0];
        player.sendMessage(sender.name().append(Component.text(" blips you.")));

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                StackPun.api().messageManager().get(BLIPPED),
                Placeholder.component("source", sender.name())));
        player.playSound(BLIPPED_SOUND);
    }
}
