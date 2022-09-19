/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.event;

import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.PortalCreateEvent;
import x.nexuskrop.stackpun.util.IReloadable;

import java.util.List;

public class WorldListener implements Listener, IReloadable {
    private List<String> prohibitedBlocks;

    public WorldListener() {
        reload();
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (prohibitedBlocks.contains(event.getBlock().getType().name())) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(StackPun.api().messageManager().getComp("block.place.prohibited"));
            return;
        }
    }

    @Override
    public void reload() {
        prohibitedBlocks = StackPun.api().configManager().getConfig().getStringList("prohibited-blocks");
    }
}
