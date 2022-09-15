/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;
import x.nexuskrop.stackpun.util.IReloadable;
import x.nexuskrop.stackpun.util.models.PlayerSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager implements Listener, IReloadable {
    private final Logger logger;

    private final Map<UUID, PlayerSettings> settingsMap = new HashMap<>();

    public PlayerManager(Logger self) {
        logger = self;
    }

    public void init(StackPun self) {
        self.getServer().getPluginManager().registerEvents(this, self);
        reload();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        logger.info("Player {} joined with client {}", player.getName(), player.getClientBrandName());
        // 创建资料
        StackPun.api().profileManager().ensure(player);
    }

    /**
     * Gets the settings entries of the specified player.
     *
     * @param uuid The unique identifier of the specified player.
     * @return The value.
     */
    public PlayerSettings getSettings(UUID uuid) {
        if (!settingsMap.containsKey(uuid)) {
            settingsMap.put(uuid, new PlayerSettings());
        }

        return settingsMap.get(uuid);
    }

    /**
     * Gets the settings entries of the specified player.
     *
     * @param player The player.
     * @return The value.
     * @apiNote If you already have UUID, you should call {@link PlayerManager#getSettings(UUID)}.
     */
    public PlayerSettings getSettings(Player player) {
        return getSettings(player.getUniqueId());
    }

    @EventHandler
    public void onOptionChange(PlayerClientOptionsChangeEvent event) {
        var uuid = event.getPlayer().getUniqueId();

        var settings = getSettings(uuid);

        // 开始设置所有值
        settings.chatVisibility(event.getChatVisibility());
        // 结束设置

        settingsMap.put(uuid, settings);
    }

    /**
     * Sends a chat message to the target. If target is deafened or either source or target blocked
     * each other, the method fails silently.
     *
     * @param source  The source of the message.
     * @param target  The target of the message.
     * @param message The message.
     * @deprecated In favour of {@link x.nexuskrop.stackpun.services.StackChatRenderer}
     */
    @Deprecated(since = "0.1.4", forRemoval = true)
    public void sendChatMessage(Player source, Player target, Component message) {
        if (source == target) return;

        var selfProfile = StackPun.api().profileManager().get(target);
        var settings = getSettings(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (settings.chatVisibility() == ClientOption.ChatVisibility.FULL && !selfProfile.isDeafened()) {
            target.sendMessage(message);
        }
    }

    /**
     * @deprecated In favour of {@link x.nexuskrop.stackpun.services.StackChatRenderer}
     */
    @Deprecated(since = "0.1.4", forRemoval = true)
    public void sendIdentifiedMessage(Player source, Player target, Component message, Identity identity) {
        if (source == target) return;

        var selfProfile = StackPun.api().profileManager().get(target);
        var settings = getSettings(target);

        // 是否没有被拉黑，接收者也没有deafened
        if (settings.chatVisibility() == ClientOption.ChatVisibility.FULL && !selfProfile.isDeafened()) {
            target.sendMessage(identity, message);
        }
    }

    @Override
    public void reload() {
        // nothing to do
    }
}
