package com.senderman.telecrafter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Config {

    @JsonProperty
    private String botToken;
    @JsonProperty
    private String botName;
    @JsonProperty
    private Long chatId;
    @JsonProperty
    private Set<Integer> admins;

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
