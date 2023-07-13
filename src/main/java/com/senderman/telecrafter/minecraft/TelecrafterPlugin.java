package com.senderman.telecrafter.minecraft;

import com.senderman.telecrafter.InjectionConfig;
import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.config.ConfigLoader;
import com.senderman.telecrafter.config.ConfigProvider;
import com.senderman.telecrafter.minecraft.command.TgCommandExecutor;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TelecrafterPlugin extends JavaPlugin {

    private TelegramPolling telegramPolling;
    private TelegramProvider telegram;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            throw new RuntimeException("No " + configFile + " was found. Default config was created, please fill it in and restart server");
        }
        Config config;
        try {
            config = ConfigLoader.load(getConfig());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }

        ConfigProvider configProvider = new ConfigProvider(config);
        InjectionConfig injector = new InjectionConfig(this, configProvider, configFile);
        telegramPolling = injector.getInstance(TelegramPolling.class);
        telegram = injector.getInstance(TelegramProvider.class);
        telegramPolling.startPolling();

        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);
        Objects.requireNonNull(getCommand("tg")).setExecutor(injector.getInstance(TgCommandExecutor.class));
    }

    @Override
    public void onDisable() {
        // null if no config was loaded
        if (telegramPolling == null || telegram == null) {
            return;
        }
        telegramPolling.stopPolling();
        telegram.sendMessageToMainChat("⭕️ Работа плагина завершена");
    }
}
