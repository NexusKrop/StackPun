/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.util;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.util.ProductData;

import java.io.InputStreamReader;
import java.util.Objects;

public final class Common {
    private Common() {
    }

    private static ProductData productData;

    public static @NotNull String getEntityDimText(@NotNull Entity entity) {
        return Objects.requireNonNull(entity).getWorld().getName();
    }

    public static @NotNull Component getEntityRepresent(@NotNull Entity entity) {
        return Component.selector(Objects.requireNonNull(entity).getUniqueId().toString());
    }

    public static ProductData productData() {
        if (productData != null) {
            return productData;
        }

        var gson = new Gson();

        try (var stream = Common.class.getResourceAsStream("/product.json")) {
            var result = gson.fromJson(new InputStreamReader(stream), ProductData.class);
            productData = result;
            return result;
        } catch (Exception ex) {
            Bukkit.getServer().getLogger().warning("Something failed to get product");
            return null;
        }
    }
}
