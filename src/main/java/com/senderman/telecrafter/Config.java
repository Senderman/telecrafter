package com.senderman.telecrafter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Config {

    private static final String configFileName = "config.yaml";
    private String botToken;
    private String botName;
    private Long chatId;
    private Set<Integer> admins;

    public static Config load(File configDir) {
        File configFile = new File(configDir, configFileName);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(configFile, Config.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading config file");
        }
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

    public Set<Integer> getAdmins() {
        return admins;
    }

    public boolean isAdmin(int userId) {
        return admins.contains(userId);
    }
}
