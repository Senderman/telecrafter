package com.senderman.telecrafter;

import com.google.inject.AbstractModule;
import com.senderman.telecrafter.minecraft.EventListener;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
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
        bind(Config.class);
        bind(JavaPlugin.class).toInstance(plugin);
        bind(EventListener.class);
        bind(MinecraftProvider.class);
        bind(TelecrafterBot.class);
        bind(TelegramChat.class);
    }
}
