package com.senderman.telecrafter.minecraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.InjectionConfig;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public class TelecrafterPlugin extends JavaPlugin {

    private TelegramPolling telegramPolling;
    private TelegramProvider telegram;

    @Override
    public void onEnable() {
        Config config;
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            throw new RuntimeException("No " + configFile + " was found. Default config was created, please fill it in and restart server");
        } else {
            try {
                config = loadConfig(configFile);
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while trying to read contents from" + configFile, e);
            }
        }

        InjectionConfig injector = new InjectionConfig(this, config);
        telegramPolling = injector.getInstance(TelegramPolling.class);
        telegram = injector.getInstance(TelegramProvider.class);
        telegramPolling.startPolling();
        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);
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

    /**
     * Load config from file
     *
     * @param configFile yml file with config
     * @return config object mapped from given file
     * @throws IOException if error occurred while reading from given file
     */
    private Config loadConfig(File configFile) throws IOException {
        ObjectMapper mapper = new YAMLMapper();
        return mapper.readValue(configFile, Config.class);
    }
}
