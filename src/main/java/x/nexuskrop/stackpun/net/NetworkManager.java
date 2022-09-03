/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.net;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.nexuskrop.stackpun.StackPun;

/**
 * Provides methods and properties to manipulate and access network packets.
 */
public class NetworkManager {
    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private final StackPun plugin;

    public NetworkManager(StackPun self) {
        plugin = self;
    }

    public void hook() {
    }
}
