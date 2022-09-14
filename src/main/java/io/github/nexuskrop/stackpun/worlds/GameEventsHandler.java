/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.worlds;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class GameEventsHandler implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        // TODO 可配置
        if (event.getEntityType() == EntityType.SLIME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPrime(ExplosionPrimeEvent event) {
        var power = event.getRadius();

        event.setCancelled(true);
        event.getEntity().getWorld().createExplosion(event.getEntity(), power, false, false);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
        event.getBlock().getWorld().createExplosion(event.getBlock().getLocation(), 3f, false, false);
    }
}
