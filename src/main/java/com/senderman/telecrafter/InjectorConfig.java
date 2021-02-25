package com.senderman.telecrafter;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.senderman.telecrafter.minecraft.EventListener;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.TelegramApi;
import com.senderman.telecrafter.telegram.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class InjectorConfig extends AbstractModule {

    private final JavaPlugin plugin;
    private final Config config;

    public InjectorConfig(JavaPlugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    protected void configure() {
        Multibinder<CommandExecutor> commandsBinder = Multibinder.newSetBinder(binder(), CommandExecutor.class);
        addBindings(commandsBinder,
                MineChat.class,
                MineNow.class,
                GetLogs.class,
                Respack.class,
                ListPlugins.class,
                ClearDrop.class,
                RunCommand.class,
                ZadroTop.class
//                Help.class
        );


        bind(Plugin.class).toInstance(plugin);
        bind(Config.class).toInstance(config);
        bind(Listener.class).to(EventListener.class);
        bind(TelegramApi.class);
        bind(TelegramPolling.class);
        bind(ServerPropertiesProvider.class);
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
