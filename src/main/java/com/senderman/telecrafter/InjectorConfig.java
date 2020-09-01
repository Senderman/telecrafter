package com.senderman.telecrafter;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.AbstractModule;
import com.senderman.telecrafter.minecraft.*;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.TelegramChat;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class InjectorConfig extends AbstractModule {

    private final JavaPlugin plugin;

    public InjectorConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(plugin);
        bind(ObjectMapper.class).to(YAMLMapper.class);
        bind(Listener.class).to(EventListener.class);
        bind(Config.class);
        bind(ServerStopDelayer.class);
        bind(ServerPropertiesProvider.class);
        bind(PluginManager.class);
        bind(MinecraftProvider.class);
        bind(TelecrafterBot.class);
        bind(TelegramChat.class);
    }
}
