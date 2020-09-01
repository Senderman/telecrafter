package com.senderman.telecrafter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Config {

    private static final String configFileName = "config.yaml";
    private String botToken;
    private String botName;
    private Long chatId;
    private Set<Integer> admins;

    public Config() {
    }

    @Inject
    public Config(Plugin plugin, ObjectMapper objectMapper) throws IOException {
        this(objectMapper.readValue(new File(plugin.getDataFolder(), configFileName), Config.class));
    }

    public Config(Config config) {
        this.botToken = config.botToken;
        this.botName = config.botName;
        this.chatId = config.chatId;
        this.admins = config.admins;
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
