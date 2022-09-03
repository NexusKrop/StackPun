/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.net;

import io.github.nexuskrop.stackpun.StackPun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.slf4j.Logger;
import x.nexuskrop.stackpun.util.IReloadable;

public class StatusListener implements Listener, IReloadable {
    private final Logger log;
    private Component motd;

    public StatusListener(Logger logger) {
        log = logger;
        reload();
    }

    public void reload() {
        var raw = (String) StackPun.api().configManager().getConfig().get("server-list.motd");
        if (raw == null) {
            raw = "StackServer";
            log.warn("Missing configuration \"server-list.motd\"");
        }

        motd = MiniMessage.miniMessage().deserialize(raw);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (motd == null) return;
        event.motd(motd);
    }
}
