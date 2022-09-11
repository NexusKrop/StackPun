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
 * Manages the player profiles.
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

    /**
     * Initialises a new instance of the {@link ProfileManager} class.
     *
     * @param self The plugin.
     */
    public ProfileManager(StackPun self) {
        plugin = self;
    }

    /**
     * Initialises this instance.
     */
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

    /**
     * Gets a player profile.
     *
     * @param player The player.
     * @return An instance of {@link PlayerProfile} representing the user profile of the {@code player}.
     */
    public @NotNull PlayerProfile get(@NotNull Player player) {
        var uuid = Objects.requireNonNull(player).getUniqueId();

        if (newProfiles.containsKey(uuid)) {
            return newProfiles.get(uuid);
        }

        var file = new File(profileFolder, String.format("pf_%s.json", uuid));

        if (!file.exists()) {
            var result = new PlayerProfile();
            saveProfile(file, result);
            newProfiles.put(uuid, result);
            return result;
        } else {
            var result = loadProfile(file);
            if (result == null) {
                result = new PlayerProfile();
                saveProfile(file, result);
                return result;
            }
            return result;
        }
    }

    /**
     * Loads a profile from disk.
     *
     * @param file The file to read.
     * @return An instance of {@link PlayerProfile} loaded from the file specified.
     */
    public @Nullable PlayerProfile loadProfile(@NotNull File file) {
        if (file.exists()) {
            try (var input = new FileReader(file)) {
                var type = PlayerProfile.class;
                return serializer.fromJson(input, type);
            } catch (FileNotFoundException ex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Path exists but read failed - check permissions and if it is a directory?");
                return null;
            } catch (IOException ex) {
                // 标志错误，禁止后续写入操作
                plugin.getSLF4JLogger().error("Failed to read", ex);
                return null;
            } catch (JsonSyntaxException jse) {
                plugin.getSLF4JLogger().warn("Invalid file syntax. Will override it.", jse);
                return null;
            } catch (JsonIOException ex) {
                plugin.getSLF4JLogger().error("Failed to parse", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Saves the specified profile to the specified file.
     *
     * @param file    The file to save to.
     * @param profile The profile to save.
     */
    public void saveProfile(@NotNull File file, @NotNull PlayerProfile profile) {
        try (var output = new FileWriter(Objects.requireNonNull(file))) {
            var type = profile.getClass();
            serializer.toJson(profile, type, output);
        } catch (IOException ex) {
            // 标志错误，禁止后续写入操作
            plugin.getSLF4JLogger().error("Failed to write to", ex);
        } catch (JsonIOException ex) {
            plugin.getSLF4JLogger().error("Failed to write profile", ex);
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

        return profiles.computeIfAbsent(uuid, (key -> {
            var result = new PlayerProfile();
            profiles.put(uuid, result);
            return result;
        }));
    }

    /**
     * Sets the profile of the specified player.
     *
     * @param player  The player to set profile.
     * @param profile The profile to set.
     */
    public void putProfile(@NotNull Player player, @NotNull PlayerProfile profile) {
        profiles.put(Objects.requireNonNull(player).getUniqueId(), Objects.requireNonNull(profile));
    }

    /**
     * Saves all profiles.
     */
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
