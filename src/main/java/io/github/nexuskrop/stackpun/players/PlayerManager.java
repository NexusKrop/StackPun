/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.api.IStackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;

import x.nexuskrop.stackpun.util.IReloadable;

public final class PlayerManager implements Listener, IReloadable {
    private final IStackPun plugin = StackPun.api();
    private final Logger logger;
    private Component listHeader;

    public PlayerManager(StackPun self) {
        logger = self.getSLF4JLogger();
        self.getServer().getPluginManager().registerEvents(this, self);
        reload();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        logger.info("Player {} joined with client {}", player.getName(), player.getClientBrandName());
        // 创建资料
        plugin.profileManager().ensure(player);

        player.sendPlayerListHeader(listHeader);
    }

    @EventHandler
    public void onOptionChange(PlayerClientOptionsChangeEvent event) {
        var player = event.getPlayer();
        var profile = StackPun.api().profileManager().get(player);

        profile.setChatVisibility(event.getChatVisibility());
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
        if (source == target) return;

        var profile = StackPun.api().profileManager().get(source);
        var selfProfile = StackPun.api().profileManager().get(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (selfProfile.getChatVisibility() == ClientOption.ChatVisibility.FULL && !selfProfile.isDeafened() && !profile.getBlockedPlayers().contains(target.getUniqueId())
                && !selfProfile.getBlockedPlayers().contains(source.getUniqueId())) {
            target.sendMessage(message);
        }
    }

    public void sendIdentifiedMessage(Player source, Player target, Component message, Identity identity) {
        if (source == target) return;

        var profile = StackPun.api().profileManager().get(source);
        var selfProfile = StackPun.api().profileManager().get(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (selfProfile.getChatVisibility() == ClientOption.ChatVisibility.FULL && !selfProfile.isDeafened() && !profile.getBlockedPlayers().contains(target.getUniqueId())
                && !selfProfile.getBlockedPlayers().contains(source.getUniqueId())) {
            target.sendMessage(identity, message);
        }
    }

    @Override
    public void reload() {
        // nothing to do
    }
}
