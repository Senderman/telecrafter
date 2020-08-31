package com.senderman.telecrafter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.senderman.telecrafter.minecraft.EventListener;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.TelegramChat;
import org.bukkit.plugin.java.JavaPlugin;

public class InjectorConfig extends AbstractModule {

    private final JavaPlugin plugin;

    public InjectorConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
        bind(ObjectMapper.class).toInstance(objectMapper);

        bind(JavaPlugin.class)
                .toInstance(plugin);
        bind(Config.class)
                .toInstance(Config.load(plugin.getDataFolder(), objectMapper));
        bind(PluginManager.class)
                .toInstance(new PluginManager(plugin));
        bind(ServerPropertiesProvider.class)
                .toInstance(new ServerPropertiesProvider(plugin.getDataFolder().getParentFile().getParentFile()));

        bind(EventListener.class);
        bind(MinecraftProvider.class);
        bind(TelecrafterBot.class);
        bind(TelegramChat.class);
    }
}
