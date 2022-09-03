/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerManager implements Listener {
    private final StackPun plugin;

    public PlayerManager(StackPun self) {
        plugin = self;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        plugin.getSLF4JLogger().info("Player {} joined with client {}", player.getName(), player.getClientBrandName());
        // 创建资料
        var profile = plugin.profileManager().getProfile(player);

        if (!profile.hadWelcomed) {
            profile.hadWelcomed = true;
            StackCommand.sendMessageLoc(player, "frontend.welcome");
        }
    }

    /**
     * Sends a chat message to the target. If target is deafened or either source or target blocked
     * each other, the method fails silently.
     *
     * @param source  The source of the message.
     * @param target  The target of the message.
     * @param message The message.
     */
    public void sendChatMessage(Player source, Player target, Component message) {
        if (source == target) return; // See STAP-17

        var profile = StackPun.api().profileManager().getProfile(source);
        var selfProfile = StackPun.api().profileManager().getProfile(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (!selfProfile.deafened || !profile.blockedPlayers.contains(target.getUniqueId())
                || !selfProfile.blockedPlayers.contains(source.getUniqueId())) {
            target.sendMessage(message);
        }
    }

    public void sendIdentifiedMessage(Player source, Player target, Component message, Identity identity) {
        if (source == target) return; // See STAP-17

        var profile = StackPun.api().profileManager().getProfile(source);
        var selfProfile = StackPun.api().profileManager().getProfile(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (!selfProfile.deafened || !profile.blockedPlayers.contains(target.getUniqueId())
                || !selfProfile.blockedPlayers.contains(source.getUniqueId())) {
            target.sendMessage(identity, message);
        }
    }
}
