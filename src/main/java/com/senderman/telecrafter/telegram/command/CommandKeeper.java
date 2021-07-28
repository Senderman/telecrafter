package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.telegram.TelegramProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class CommandKeeper {

    private final Map<String, CommandExecutor> commands;

    @Inject
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
