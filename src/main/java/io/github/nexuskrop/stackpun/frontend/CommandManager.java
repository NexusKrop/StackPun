package io.github.nexuskrop.stackpun.frontend;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class CommandManager {
    private final CommandAPICommand blipCommand;

    public CommandManager() {
        blipCommand = new CommandAPICommand("blip");

        blipCommand.withArguments(new PlayerArgument("target"))
                .executesPlayer((sender, args) -> {
                    var player = (Player)args[0];
                    player.sendMessage(sender.displayName().append(Component.text(" blips you.")));
                })
                .register();
    }
}
