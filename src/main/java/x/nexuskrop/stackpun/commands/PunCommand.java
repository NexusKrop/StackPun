/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * Represents a Version 2 command.
 *
 * <p>A version 2 command has several advantages over the old legacy command {@link io.github.nexuskrop.stackpun.frontend.commands.StackCommand}:
 *  <ul>
 *      <li>Help messages are automatically defined; just define in {@code msg.properties} file then the system will load it.</li>
 *      <li>Permissions are automatically defined too.</li>
 *      <li>What command to enable and to disable are controlled by {@code product.json} - you do not need to edit {@link io.github.nexuskrop.stackpun.StackPun}, aka the
 *      main class to control what to register and what not to.</li>
 *  </ul>
 * </p>
 * <p>Newer commands should implement this interface rather than the legacy command interface.</p>
 */
public interface PunCommand {
    /**
     * Implements the specified command.
     *
     * @param command The provided command.
     * @implNote Do not call {@link CommandAPICommand#register()} as it is automatically handled by the {@link CommandManager}.
     */
    void initialise(CommandAPICommand command);
}
