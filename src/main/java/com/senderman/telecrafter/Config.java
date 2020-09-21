package com.senderman.telecrafter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Config {

    private static final String configFileName = "config.yml";

    @JsonProperty
    private String botToken;
    @JsonProperty
    private String botName;
    @JsonProperty
    private Long chatId;
    @JsonProperty
    private String craftyToken;
    @JsonProperty
    private Integer craftyPort;
    @JsonProperty
    private Integer craftyServerId;
    @JsonProperty
    private Set<Integer> admins;

    public static Config load(Plugin plugin, ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(new File(plugin.getDataFolder(), configFileName), Config.class);
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

    public String getCraftyToken() {
        return craftyToken;
    }

    public int getCraftyPort() {
        return craftyPort;
    }

    public int getCraftyServerId() {
        return craftyServerId;
    }

    public Set<Integer> getAdmins() {
        return admins;
    }

    public boolean isAdmin(int userId) {
        return admins.contains(userId);
    }
}
