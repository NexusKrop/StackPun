/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import com.destroystokyo.paper.ClientOption;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import io.github.nexuskrop.stackpun.util.Common;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.util.IReloadable;
import x.nexuskrop.stackpun.util.Worlds;

import java.util.Objects;

public final class ChatManager implements Listener, IReloadable {
    private static final String SILENCED = "chat.silenced";
    private static final String VISIBILITY_OFF = "chat.visibility_off";
    private String formatCache;
    private Boolean passIdentityCache;

    public void init(@NotNull StackPun self) {
        self.getSLF4JLogger().info("Initialising chat manager");
        self.getServer().getPluginManager().registerEvents(this, self);
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

    public void reload() {
        formatCache = null;
        passIdentityCache = null;
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        var player = event.getPlayer();
        var profile = StackPun.api().profileManager().get(player);

        if (profile.chatVisibility() != ClientOption.ChatVisibility.FULL) {
            StackCommand.sendErrorLoc(player, VISIBILITY_OFF);
            event.setCancelled(true);
            return;
        }

        if (profile.isMuted()) {
            StackCommand.sendErrorLoc(player, StackCommand.MESSAGE_MUTED);
            event.setCancelled(true);
            return;
        }

        if (profile.isSilenced()) {
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
        }

        Bukkit.getServer().sendMessage(comp);

        event.setCancelled(true);
    }

    /**
     * Broadcasts a success message.
     *
     * @param source    The source.
     * @param component The component.
     * @deprecated Because of unfixable issue of sending a message. You can still use it but no more support will be provided for it.
     */
    @Deprecated(since = "0.1.1", forRemoval = false)
    public void broadcastSuccess(CommandSender source, Component component) {
        // 合成消息（chat.type.admin）
        var msg = Component.translatable("chat.type.admin")
                .args(source.name(), component).color(NamedTextColor.GRAY)
                .decorate(TextDecoration.ITALIC);

        // 检查游戏规则 sendCommandFeedback 是否启用
        if (Worlds.overworld().isGameRule("sendCommandFeedback")) {
            // 如果启用，遍历所有人
            for (var player :
                    Bukkit.getServer().getOnlinePlayers()) {
                if (source instanceof Player ply && ply.equals(player)) {
                    // 如果是玩家且是命令执行者，跳过玩家
                    continue;
                }

                // 有权限且不是命令的执行者
                if (!source.equals(player) && player.hasPermission("stackpun.commands.generic.broadcast")) {
                    // 发送消息
                    player.sendMessage(msg);
                }
            }

            if (!(source instanceof ConsoleCommandSender)) {
                Bukkit.getServer().sendMessage(msg);
            }
        }
    }
}
