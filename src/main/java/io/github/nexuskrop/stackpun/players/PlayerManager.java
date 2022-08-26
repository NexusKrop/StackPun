/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
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

        // 创建资料
        var profile = plugin.profileManager().getProfile(player);

        if (profile.wasModded && Objects.equals(player.getClientBrandName(), "vanilla")) {
            profile.wasModded = false;
            profile.wasNotifiedForModded = false;
        } else if (!Objects.equals(player.getClientBrandName(), "vanilla")) {
            profile.wasModded = true;

            if (!profile.wasNotifiedForModded) {
                notifyModded(player);
                profile.wasNotifiedForModded = true;
            }
        }
    }

    private void notifyModded(Player player) {
        StackCommand.sendMessageLoc(player, MODDED_WARNING_1);
        StackCommand.sendMessageLoc(player, MODDED_WARNING_2);
        StackCommand.sendMessageVal(player, MODDED_WARNING_3, player.getClientBrandName());
        StackCommand.sendMessageLoc(player, MODDED_WARNING_4);
        StackCommand.sendMessageLoc(player, MODDED_WARNING_5);
    }
}
