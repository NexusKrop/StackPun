package io.github.nexuskrop.stackpun.util;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Common {
    private Common() {}

    public static @NotNull String getEntityDimText(@NotNull Entity entity) {
        return Objects.requireNonNull(entity).getWorld().getEnvironment().name();
    }
}
