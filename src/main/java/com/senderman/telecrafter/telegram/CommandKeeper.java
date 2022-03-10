package com.senderman.telecrafter.telegram;

import com.senderman.telecrafter.telegram.command.Help;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandKeeper {

    private final Map<String, CommandExecutor> commands;

    public CommandKeeper(TelegramProvider telegram, Set<CommandExecutor> commandExecutors) {
        commands = new HashMap<>();
        for (CommandExecutor exe : commandExecutors)
            register(exe);
        register(new Help(telegram, commandExecutors));
    }

    @Nullable
    public CommandExecutor getExecutor(String command) {
        return commands.get(command);
    }

    private void register(CommandExecutor commandExecutor) {
        commands.put(commandExecutor.getCommand(), commandExecutor);
    }

}
