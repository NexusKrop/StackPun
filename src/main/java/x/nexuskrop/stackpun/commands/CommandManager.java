/*
 * Copyright (c) 2022.
 * This file is part of StackPun.
 * Licensed under GNU AGPL version 3 or later.
 */

package x.nexuskrop.stackpun.commands;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.nexuskrop.stackpun.StackPun;
import io.github.nexuskrop.stackpun.frontend.commands.StackCommand;
import io.github.nexuskrop.stackpun.util.Common;
import org.slf4j.Logger;
import x.nexuskrop.stackpun.commands.annotations.PunCommandInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager {
    private final Logger log;

    public CommandManager(Logger logger) {
        log = logger;
    }

    public final List<PunCommand> commandList = new ArrayList<>();

    public void addLegacy(Class<?> command) {
        if (!StackCommand.class.isAssignableFrom(Objects.requireNonNull(command))) {
            complain("it is not a legacy command", command);
            return;
        }

        try {
            var legacyCommand = (StackCommand) command.getConstructor().newInstance();
            legacyCommand.register();
            log.info("Registered legacy command {}", command.getSimpleName());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
            complain("the legacy command does not have a default public constructor", command);
        } catch (InvocationTargetException e) {
            log.warn("Failed to create command {}", command.getSimpleName());
            log.warn("Unknown error", e);
        }
    }

    public void addLegacy(Class<?>... command) {
        for (var cmd : command) {
            addLegacy(cmd);
        }
    }

    public void add(Class<?> command) {
        // 检查参数非空和目标有 PunCommandInfo 注解
        var anno = Objects.requireNonNull(command).getAnnotation(PunCommandInfo.class);
        if (anno == null) {
            // 如果没有，日志警告返回
            complain("without PunCommandInfo annotation", command);
            return;
        }

        // 检查是否实现 PunCommand
        if (!PunCommand.class.isAssignableFrom(command)) {
            // 如果没有，日志警告返回
            complain("but it is not a command", command);
            return;
        }

        var msgManager = StackPun.api().messageManager();

        // 创建 CommandAPI 实例
        var apiCommand = new CommandAPICommand(anno.name())
                .withPermission(String.format("stackpun.commands.%s", anno.name()))
                .withHelp(msgManager.get(String.format("stackpun.commands.%s.help_short", anno.name())),
                        msgManager.get(String.format("stackpun.commands.%s.help_full", anno.name())));

        try {
            // 创建选中命令的实例
            var instance = (PunCommand) command.getConstructor().newInstance();
            // 命令执行初始化
            instance.initialise(apiCommand);
            // 注册
            apiCommand.register();
            log.info("Registered gen2 command {}", command.getSimpleName());
            commandList.add(instance);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException sme) {
            complain("without default public constructor", command);
        } catch (InstantiationException | InvocationTargetException ite) {
            log.error("Failed to add new command {}", command.getSimpleName());
            log.error(ite.toString());
        } catch (Exception ex) {
            log.error("Un-excepted exception caught when adding new command", ex);
        }
    }

    public void addFromProject() {
        var data = Common.productData();

        if (data == null) {
            log.warn("Cannot get product data. Are you sure you are using this correctly?");
            return;
        }

        // 遍历所有产品注册记录上的命令
        for (var command :
                data.commands) {
            // 拼接字符串取得全目录
            var fullStr = data.commandPackage + '.' + command;
            Class<?> type;
            try {
                type = Class.forName(fullStr);
            } catch (ClassNotFoundException e) {
                // 如果不存在则提示并检查下一个
                log.warn("Command {} does not exist", command);
                continue;
            }

            if (!PunCommand.class.isAssignableFrom(type)) {
                // 如果不是第二代命令，检查下一个
                log.warn("Command {} is not a gen2 command", command);
                continue;
            }

            add(type);
        }
    }

    private void complain(String reason, Class<?> type) {
        log.warn("Got a PunCommand {}: {}", reason, type.getSimpleName());
    }
}
