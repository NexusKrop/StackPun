/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.util.Common;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ChatManager implements Listener {
    private final StackPun plugin;
    private final String chatFormat = "<dark_gray>[<dim>]</dark_gray> [<playerName>] <gray><message>";
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer
            .legacy(ChatColor.COLOR_CHAR);

    public ChatManager(StackPun self) {
        plugin = self;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        // TODO 解除 chatFormat 硬编码
        var player = event.getPlayer();

        if (plugin.profileManager().getProfile(player).muted) {
            player.sendMessage(Component.text("You are currently MUTED.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        var comp = MiniMessage.miniMessage().deserialize(chatFormat,
                Placeholder.unparsed("dim", Common.getEntityDimText(event.getPlayer())),
                Placeholder.component("playerName", player.displayName()),
                Placeholder.component("message", event.message()));

        plugin.getServer().getLogger().info(serializer.serialize(comp));
        Bukkit.getServer().broadcast(comp);
        event.setCancelled(true);
    }
}
