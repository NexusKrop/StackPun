/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import io.github.nexuskrop.stackpun.util.Common;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public final class ChatManager implements Listener {
    private static final String SILENCED = "chat.silenced";
    private String formatCache;
    private Boolean passIdentityCache;

    private final StackPun plugin;

    public ChatManager(StackPun self) {
        plugin = self;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private String getFormat() {
        if (formatCache == null) {
            formatCache = Objects.requireNonNull(StackPun.api().configManager().getConfig().getString("chat.format", "[<player_name>] <message>"));
        }

        return formatCache;
    }

    private boolean passIdentity() {
        if (passIdentityCache == null) {
            passIdentityCache = StackPun.api().configManager().getConfig().getBoolean("chat.pass-identity", false);
        }

        return passIdentityCache;
    }

    public void clearCache() {
        formatCache = null;
        passIdentityCache = null;
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        // TODO 解除 chatFormat 硬编码
        var player = event.getPlayer();
        var profile = plugin.profileManager().getProfile(player);

        if (profile.muted) {
            StackCommand.sendErrorLoc(player, StackCommand.MESSAGE_MUTED);
            event.setCancelled(true);
            return;
        }

        if (profile.silenced) {
            StackCommand.sendErrorLoc(player, SILENCED);
            event.setCancelled(true);
            return;
        }

        var comp = MiniMessage.miniMessage().deserialize(getFormat(),
                Placeholder.unparsed("dim", Common.getEntityDimText(event.getPlayer())),
                Placeholder.component("player_name", player.displayName()),
                Placeholder.component("message", event.message()));

        var manager = StackPun.api().playerManager();

        for (var target :
                Bukkit.getServer().getOnlinePlayers()) {

            if (passIdentity()) {
                manager.sendIdentifiedMessage(player, target, comp, player.identity());
            } else {
                manager.sendChatMessage(player, target, comp);
            }

            manager.sendChatMessage(player, target, comp);
        }

        Bukkit.getServer().sendMessage(comp);

        event.setCancelled(true);
    }
}
