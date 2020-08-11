package com.senderman.telecrafter;

import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private final Properties properties = new Properties();
    private final String configFileName = "telecrafter.properties";
    private final String botToken;
    private final String botName;
    private final Long chatId;

    @Inject
    public Config(JavaPlugin plugin) {
        File dataFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + configFileName);

        try (FileInputStream in = new FileInputStream(dataFile)) {
            properties.load(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("You must create and fill in " + dataFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.botToken = getProperty("bot.token");
        this.botName = getProperty("bot.name");
        this.chatId = Long.parseLong(getProperty("bot.chatId"));
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public Long getChatId() {
        return chatId;
    }

    private String getProperty(String key) {
        String result = properties.getProperty(key);
        if (result == null)
            throw new RuntimeException("You must define " + key + " in " + configFileName + "!");

        return result.trim();
    }


}
