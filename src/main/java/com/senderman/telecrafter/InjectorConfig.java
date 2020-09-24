package com.senderman.telecrafter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.senderman.telecrafter.minecraft.*;
import com.senderman.telecrafter.minecraft.command.RestartCommand;
import com.senderman.telecrafter.minecraft.command.StopCommand;
import com.senderman.telecrafter.minecraft.crafty.CraftyWrapper;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.TelegramApiWrapper;
import com.senderman.telecrafter.telegram.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class InjectorConfig extends AbstractModule {

    private final JavaPlugin plugin;

    public InjectorConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        Multibinder<CommandExecutor> commandsBinder = Multibinder.newSetBinder(binder(), CommandExecutor.class);
        addBindings(commandsBinder,
                MineChat.class,
                MineNow.class,
                DeletePlugin.class,
                DisablePlugin.class,
                EnablePlugin.class,
                GetLogs.class,
                GetProp.class,
                InstallPlugin.class,
                ListPlugins.class,
                SetProp.class
//                Help.class
        );

        ObjectMapper mapper = new YAMLMapper();
        bind(Plugin.class).toInstance(plugin);
        bind(Listener.class).to(EventListener.class);
        bind(TelegramApiWrapper.class);
        bind(TelegramPolling.class);
        try {
            bind(Config.class).toInstance(Config.load(plugin, mapper));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bind(RestartCommand.class);
        bind(StopCommand.class);
        bind(ServerStopDelayer.class);
        bind(CraftyWrapper.class);
        bind(ServerPropertiesProvider.class);
        bind(PluginManager.class);
        bind(MinecraftProvider.class);
        bind(TelecrafterBot.class);
        bind(TelegramProvider.class);
    }

    @SafeVarargs
    private final <T> void addBindings(Multibinder<T> binder, Class<? extends T>... bindings) {
        for (Class<? extends T> binding : bindings) {
            binder.addBinding().to(binding);
        }
    }
}
