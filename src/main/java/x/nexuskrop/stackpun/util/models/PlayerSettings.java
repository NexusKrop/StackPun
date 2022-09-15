/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.util.models;

import com.destroystokyo.paper.ClientOption;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class PlayerSettings implements Serializable {
    private ClientOption.ChatVisibility chatVisibility;

    /**
     * Gets the chat visibility of the player associated with this instance.
     *
     * @return Chat visibility of the player associated with this instance.
     */
    @NotNull
    public ClientOption.ChatVisibility chatVisibility() {
        return chatVisibility;
    }

    /**
     * Sets the chat visibility of the player associated with this instance.
     *
     * @param value The value to set to.
     */
    public void chatVisibility(@NotNull ClientOption.ChatVisibility value) {
        chatVisibility = value;
    }
}
