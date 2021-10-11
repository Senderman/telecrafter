package com.senderman.telecrafter;

import com.senderman.telecrafter.minecraft.EventListener;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.TelegramApi;
import com.senderman.telecrafter.telegram.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InjectionConfig {

    private final Map<Class<?>, Object> instances;

    public InjectionConfig(JavaPlugin plugin, Config config) {
        this.instances = new HashMap<>();
        save(plugin);
        save(config);

        save(new TelegramApi(config));
        TelegramProvider telegram = new TelegramProvider(getInstance(TelegramApi.class), config);
        MinecraftProvider minecraft = new MinecraftProvider(plugin);
        save(telegram);
        save(minecraft);

        save(new EventListener(getInstance(TelegramProvider.class)));
        save(new ServerPropertiesProvider(plugin));

        Set<CommandExecutor> commandExecutors = new HashSet<>();
        commandExecutors.add(new ClearDrop(telegram, minecraft));
        commandExecutors.add(new GetLogs(telegram, minecraft));
        commandExecutors.add(new ListPlugins(telegram, minecraft));
        commandExecutors.add(new MineChat(minecraft));
        commandExecutors.add(new Respack(telegram, getInstance(ServerPropertiesProvider.class)));
        commandExecutors.add(new RunCommand(telegram, minecraft));
        commandExecutors.add(new ZadroTop(telegram, minecraft));
        commandExecutors.add(new Help(telegram, commandExecutors));

        save(new CommandKeeper(telegram, commandExecutors));
        save(new TelecrafterBot(config, getInstance(CommandKeeper.class), telegram));
        save(new TelegramPolling(getInstance(TelegramApi.class), getInstance(TelecrafterBot.class)));
    }

    public <T> T getInstance(Class<T> tClass) {
        return (T) instances.get(tClass);
    }

    private void save(Object object) {
        instances.put(object.getClass(), object);
    }

}
