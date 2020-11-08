package com.senderman.telecrafter.minecraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.InjectorConfig;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;


public class TelecrafterPlugin extends JavaPlugin {

    private TelegramPolling telegramPolling;
    private TelegramProvider telegram;

    @Override
    public void onEnable() {

        Config config;
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) {
            try {
                config = loadConfig(configFile);
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while trying to read contents from" + configFile);
            }
        } else {
            // copy default config to config dir and do not turn plugin on
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            try {
                createDefaultConfig(configFile);
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while trying to write to " + configFile);
            }
            throw new RuntimeException("No " + configFile + " was found. Default config was created, please fill it in and restart server");
        }

        Injector injector;
        try {
            injector = Guice.createInjector(new InjectorConfig(this, config));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
     * Create default config from classpath resource in given location
     *
     * @param configDestination where to create default config
     * @throws IOException if unable to write to file
     */
    private void createDefaultConfig(File configDestination) throws IOException {
        try (
                InputStream in = getClass().getResourceAsStream("/default-config.yml");
                OutputStream out = new FileOutputStream(configDestination)
        ) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        }
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
