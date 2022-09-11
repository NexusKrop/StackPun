/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.net;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides methods and properties to manipulate and access network packets.
 */
public class NetworkManager {
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private final Logger logger;

    public NetworkManager(Logger self) {
        logger = self;
    }

    public void sendDemoScreen(Player victim) {
        var packet = new PacketContainer(PacketType.Play.Server.WORLD_EVENT);
        packet.getIntegers().write(0, 5);
        packet.getFloat().write(1, 0f);
        try {
            protocolManager.sendServerPacket(victim, packet);
        } catch (InvocationTargetException e) {
            logger.warn("Failed to process invocation target", e);
        }
    }
}
