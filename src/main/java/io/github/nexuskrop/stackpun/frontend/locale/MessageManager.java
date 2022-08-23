/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package io.github.nexuskrop.stackpun.frontend.locale;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public final class MessageManager {
    private final Properties messages = new Properties();
    private final Logger logger;
    private final File messagesFile;

    public MessageManager(File msgFile, Logger selfLogger) {
        logger = selfLogger;
        logger.info("MessageManager instantiated");

        messagesFile = msgFile;

        tryInit();
    }

    /**
     * Gets the localised string from the specified ID.
     *
     * @param id The localised string ID.
     * @return A localised string if found; otherwise, the ID.
     */
    public String get(@NotNull String id) {
        if (!messages.containsKey(Objects.requireNonNull(id))) {
            return id;
        }

        return messages.get(id).toString();
    }

    public void tryInit() {
        logger.info("Initialising MessageManager");

        if (messagesFile.exists()) {
            loadMessagesFromDisk();
        } else {
            loadMessagesFromResource();
            saveMessagesToDisk();
        }
    }

    public void saveMessagesToDisk() {
        try (var output = new FileWriter(messagesFile)) {
            messages.store(output, "Messages file");
        } catch (IOException e) {
            logger.error("Failed to save messages", e);
        }
    }

    public void loadMessagesFromDisk() {
        try (var stream = new FileReader(messagesFile)) {
            messages.load(stream);
        } catch (Exception ex) {
            logger.error("Failed to load messages", ex);
        }
    }

    public void loadMessagesFromResource() {
        try (var stream = MessageManager.class.getResourceAsStream("msg.properties")) {
            messages.load(stream);
        } catch (Exception ex) {
            logger.error("Failed to load messages", ex);
        }
    }
}