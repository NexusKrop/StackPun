/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.worlds;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

// TODO 注册

public class GameEventsHandler implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        // TODO 可配置
        if (event.getEntityType() == EntityType.SLIME) {
            event.setCancelled(true);
        }
    }
}
