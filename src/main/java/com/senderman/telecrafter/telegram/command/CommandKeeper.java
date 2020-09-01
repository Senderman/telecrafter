package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CommandKeeper {

    private final Map<String, CommandExecutor> commands;

    public CommandKeeper(
            TelecrafterBot telegram,
            MinecraftProvider minecraft,
            ServerPropertiesProvider serverProperties,
            PluginManager pluginManager
    ) {
        commands = new HashMap<>();
        register(
                // add commands here
                new MineChat(minecraft, telegram),
                new MineNow(telegram, minecraft),
                new SetProp(telegram, serverProperties),
                new GetProp(telegram, serverProperties),
                new ListPlugins(telegram, pluginManager),
                new InstallPlugin(telegram, pluginManager),
                new DeletePlugin(telegram, pluginManager),
                new EnablePlugin(telegram, pluginManager),
                new DisablePlugin(telegram, pluginManager),
                new Help(telegram, commands),
                new GetLogs(telegram, minecraft)
        );
    }

    @Nullable
    public CommandExecutor getExecutor(String command) {
        return commands.get(command);
    }

    private void register(CommandExecutor... commandExecutors) {
        for (CommandExecutor ce : commandExecutors) {
            commands.put(ce.getCommand(), ce);
        }
    }

}
