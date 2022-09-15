/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.players;

import com.destroystokyo.paper.ClientOption;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.services.StackChatRenderer;

public final class ChatManager implements Listener {
    private static final String SILENCED = "chat.silenced";
    private static final String VISIBILITY_OFF = "chat.visibility_off";
    private final StackChatRenderer renderer = new StackChatRenderer();

    public void init(@NotNull StackPun self) {
        self.getSLF4JLogger().info("Initialising chat manager");
        self.getServer().getPluginManager().registerEvents(this, self);
        StackPun.api().configManager().addMonitored(renderer);
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

        event.renderer(renderer);
    }
}
