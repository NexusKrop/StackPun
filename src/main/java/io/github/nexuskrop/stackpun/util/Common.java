/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Common {
    private Common() {
    }

    public static @NotNull String getEntityDimText(@NotNull Entity entity) {
        return Objects.requireNonNull(entity).getWorld().getEnvironment().name();
    }

    public static @NotNull Component getEntityRepresent(@NotNull Entity entity) {
        return Component.selector(Objects.requireNonNull(entity).getUniqueId().toString());
    }
}
