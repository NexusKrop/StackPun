/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.services;

import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.util.Common;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.util.IReloadable;

import java.util.Objects;

public class StackChatRenderer implements ChatRenderer, IReloadable {
    private String formatCache;

    @Override
    public void reload() {
        formatCache = null;
    }

    private String getFormat() {
        if (formatCache == null) {
            formatCache = Objects.requireNonNull(StackPun.api().configManager().getConfig().getString("chat.format", "[<player_name>] <message>"));
        }

        return formatCache;
    }


    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component component, @NotNull Component component1, @NotNull Audience audience) {
        return MiniMessage.miniMessage().deserialize(getFormat(),
                Placeholder.unparsed("dim", Common.getEntityDimText(player)),
                Placeholder.component("player_name", component),
                Placeholder.component("message", component1));
    }
}
