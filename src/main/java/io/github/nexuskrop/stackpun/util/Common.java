/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.util;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import x.nexuskrop.stackpun.util.ProductData;

import java.io.InputStreamReader;
import java.util.Objects;

public final class Common {
    public static final String AUTHOR = "NexusKrop Project";
    public static final String APP_NAME = "StackPun";

    public static final String VALUE_PLACEHOLDER = "value";

    private Common() {
    }

    private static ProductData productData;

    public static TagResolver valueComponent(Component component) {
        return Placeholder.component(VALUE_PLACEHOLDER, component);
    }

    public static TagResolver valueLiteral(String text) {
        return Placeholder.unparsed(VALUE_PLACEHOLDER, text);
    }

    public static @NotNull String getEntityDimText(@NotNull Entity entity) {
        return Objects.requireNonNull(entity).getWorld().getName();
    }

    public static @NotNull Component getEntityRepresent(@NotNull Entity entity) {
        return entity.name().hoverEvent(entity.asHoverEvent());
    }

    public static ProductData productData() {
        if (productData != null) {
            return productData;
        }

        var gson = new Gson();

        try (var stream = Common.class.getResourceAsStream("/product.json")) {
            assert stream != null;
            var result = gson.fromJson(new InputStreamReader(stream), ProductData.class);
            productData = result;
            return result;
        } catch (Exception ex) {
            Bukkit.getServer().getLogger().warning("Something failed to get product");
            return null;
        }
    }
}
