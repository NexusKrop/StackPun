/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.data;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import io.github.nexuskrop.stackpun.StackPun;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private Map<UUID, PlayerProfile> profiles;
    private final Map<UUID, PlayerProfile> newProfiles = new HashMap<>();
    private File profileFolder;

    /**
     * Indicates whether it is prohibited to write profiles to disk. Usually a {@code true} value
     * indicates that there were an error while attempting to initialise and the manager decides
     * it will be unable to write to the profiles file.
     */
    private boolean prohibitWrite;

    private final Gson serializer = new Gson();

    public ProfileManager(StackPun self) {
        plugin = self;
    }

    public void init() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir()) {
            plugin.getSLF4JLogger().error("No data folder available. Profiles will not be saved!");
            prohibitWrite = true;
        }

        profileFolder = new File(plugin.getDataFolder(), "profiles");
        if (!profileFolder.exists() && !profileFolder.mkdir()) {
            plugin.getSLF4JLogger().error("Failed to create profiles folder!");
            prohibitWrite = true;
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

    public @NotNull PlayerProfile get(@NotNull Player player) {
        var uuid = Objects.requireNonNull(player).getUniqueId();

        if (newProfiles.containsKey(uuid)) {
            return newProfiles.get(uuid);
        }

        var file = new File(profileFolder, String.format("pf_%s.json", uuid));

        if (!file.exists()) {
            var result = new PlayerProfile(player.getName());
            saveProfile(file, result);
            newProfiles.put(uuid, result);
            return result;
        } else {
            var result = loadProfile(file);
            if (result == null) {
                result = new PlayerProfile(player.getName());
                saveProfile(file, result);
                return result;
            }
            return result;
        }
    }

    public @Nullable PlayerProfile loadProfile(@NotNull File file) {
        if (file.exists()) {
            try (var input = new FileReader(file)) {
                var type = PlayerProfile.class;
                return serializer.fromJson(input, type);
            } catch (FileNotFoundException fnex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Path exists but read failed - check permissions and if it is a directory?");
                return null;
            } catch (IOException ioex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Failed to read", ioex);
                return null;
            } catch (JsonSyntaxException jse) {
                plugin.getSLF4JLogger().warn("Invalid file syntax. Will override it.", jse);
                return null;
            } catch (JsonIOException jioe) {
                plugin.getSLF4JLogger().error("Failed to parse", jioe);
                return null;
            }
        } else {
            return null;
        }
    }

    public void saveProfile(@NotNull File file, @NotNull PlayerProfile profile) {
        try (var output = new FileWriter(Objects.requireNonNull(file))) {
            var type = profile.getClass();
            serializer.toJson(profile, type, output);
        } catch (IOException ioex) {
            // 标志错误，禁止后续写入操作
            plugin.getSLF4JLogger().error("Failed to write to", ioex);
        } catch (JsonIOException jioe) {
            plugin.getSLF4JLogger().error("Failed to write profile", jioe);
        }
    }

    /**
     * Gets an old profile.
     *
     * @param player The player to get the profile.
     * @return The old profile.
     * @deprecated In favour of {@link ProfileManager#get(Player)}
     */
    @Deprecated(forRemoval = true)
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

        for (var entry : newProfiles.entrySet()) {
            var file = new File(profileFolder, String.format("pf_%s.json", entry.getKey()));
            saveProfile(file, entry.getValue());
        }
    }
}
