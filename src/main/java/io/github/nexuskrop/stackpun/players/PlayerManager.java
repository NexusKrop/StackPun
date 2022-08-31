/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public final class PlayerManager implements Listener {
    private static final String MODDED_WARNING_1 = "frontend.modded_client_line1";
    private static final String MODDED_WARNING_2 = "frontend.modded_client_line2";
    private static final String MODDED_WARNING_3 = "frontend.modded_client_line3";
    private static final String MODDED_WARNING_4 = "frontend.modded_client_line4";
    private static final String MODDED_WARNING_5 = "frontend.modded_client_line5";

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

        if (profile.wasModded && Objects.equals(player.getClientBrandName(), "vanilla")) {
            plugin.getSLF4JLogger().info("Player {} client no longer modded", player.getName());
            profile.wasModded = false;
            profile.wasNotifiedForModded = false;
        } else if (!Objects.equals(player.getClientBrandName(), "vanilla")) {
            plugin.getSLF4JLogger().info("Player {} was joined using a modded client", player.getName());
            profile.wasModded = true;

            if (!profile.wasNotifiedForModded) {
                notifyModded(player);
                profile.wasNotifiedForModded = true;
            }
        }
    }

    private void notifyModded(Player player) {
        // nullsubbed due to some serious issues
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
        var profile = StackPun.api().profileManager().getProfile(source);
        var selfProfile = StackPun.api().profileManager().getProfile(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (!selfProfile.deafened || !profile.blockedPlayers.contains(target.getUniqueId())
                || !selfProfile.blockedPlayers.contains(source.getUniqueId())) {
            target.sendMessage(message);
        }
    }
}
