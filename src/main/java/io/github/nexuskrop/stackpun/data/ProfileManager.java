/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A manager to manage player profile information.
 *
 * @author WithLithum
 */
public final class ProfileManager {
    private final StackPun plugin;
    private static final String PROFILES_JSON = "profiles.json";
    private Map<UUID, PlayerProfile> profiles;
    private File profileFile;

    /**
     * Indicates whether it is prohibited to write profiles to disk. Usually a {@code true} value
     * indicates that there were an error while attempting to initialise and the manager decides
     * it will be unable to write to the profiles file.
     */
    private boolean prohibitWrite;

    private final Gson serializer = new GsonBuilder().enableComplexMapKeySerialization().create();

    public ProfileManager(StackPun self) {
        plugin = self;
    }

    /**
     * Initialises this instance. If existing profiles file is on disk, attempt to read it.
     */
    public void init() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir()) {
            plugin.getSLF4JLogger().error("Failed to create data folder.");
            prohibitWrite = true;
        }

        profileFile = new File(plugin.getDataFolder(), PROFILES_JSON);

        if (profileFile.exists()) {
            try (var input = new FileReader(profileFile)) {
                var type = new TypeToken<Map<UUID, PlayerProfile>>() {
                }.getType();
                profiles = serializer.fromJson(input, type);
            } catch (FileNotFoundException fnex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Path exists but read failed - check permissions and if it is a directory?");
                profiles = new HashMap<>();
                prohibitWrite = true;
            } catch (IOException ioex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Failed to read", ioex);
                profiles = new HashMap<>();
                prohibitWrite = true;
            } catch (JsonSyntaxException jse) {
                plugin.getSLF4JLogger().warn("Invalid file syntax. Will override it.", jse);
                profiles = new HashMap<>();
            } catch (JsonIOException jioe) {
                plugin.getSLF4JLogger().error("Failed to parse", jioe);
                profiles = new HashMap<>();
                prohibitWrite = true;
            }
        } else {
            profiles = new HashMap<>();
        }
    }

    /**
     * Sets a value indicating whether this instance can write profiles to disk.
     *
     * @param value {@code true} if this instance can write profiles to disk; otherwise, {@code false}.
     */
    @Contract(mutates = "this")
    public void prohibitWrite(boolean value) {
        prohibitWrite = value;
    }

    /**
     * Gets a value indicating whether this instance can write profiles to disk.
     *
     * @return {@code true} if this instance can write profiles to disk; otherwise, {@code false}.
     */
    public boolean prohibitWrite() {
        return prohibitWrite;
    }

    public PlayerProfile getProfile(@NotNull Player player) {
        // 提前获取UUID
        var uuid = Objects.requireNonNull(player).getUniqueId();

        if (!profiles.containsKey(uuid)) {
            // 如果没有现存实例，创建新的实例
            var result = new PlayerProfile(player.getName());
            profiles.put(uuid, result);
            return result;
        }

        return profiles.get(uuid);
    }

    public void putProfile(@NotNull Player player, @NotNull PlayerProfile profile) {
        profiles.put(Objects.requireNonNull(player).getUniqueId(), Objects.requireNonNull(profile));
    }

    public void save() {
        if (prohibitWrite) {
            // 如果禁止，直接返回
            plugin.getSLF4JLogger().warn("Write has been prohibited due to previous error.");
            return;
        }

        try (var output = new FileWriter(profileFile)) {
            var type = new TypeToken<Map<UUID, PlayerProfile>>() {
            }.getType();
            serializer.toJson(profiles, type, output);
        } catch (IOException ioex) {
            // 标志错误，禁止后续写入操作
            plugin.getSLF4JLogger().error("Failed to write to", ioex);
            prohibitWrite = true;
        } catch (JsonIOException jioe) {
            plugin.getSLF4JLogger().error("Failed to export profiles", jioe);
            profiles = new HashMap<>();
            prohibitWrite = true;
        }
    }
}
