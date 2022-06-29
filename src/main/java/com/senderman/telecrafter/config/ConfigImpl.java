package com.senderman.telecrafter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public class ConfigImpl implements Config {

    @JsonProperty
    private String botToken;

    @JsonProperty
    private String botName;

    @JsonProperty
    private Long chatId;

    @JsonProperty(defaultValue = "false")
    private boolean allowForeignChats;

    @JsonProperty
    private Set<Long> admins;

    @JsonProperty
    private Set<String> forceAdminCommands;

    @JsonProperty
    private Map<String, Alias> aliases;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotName() {
        return botName;
    }

    @Override
    public Long getChatId() {
        return chatId;
    }

    @Override
    public boolean isAllowForeignChats() {
        return allowForeignChats;
    }

    @Override
    public Set<Long> getAdmins() {
        return admins;
    }

    @Override
    public boolean isAdmin(long userId) {
        return admins.contains(userId);
    }

    @Override
    public Set<String> getForceAdminCommands() {
        return forceAdminCommands;
    }

    @Override
    public boolean isForcedAdminCommand(String command) {
        return forceAdminCommands.contains(command);
    }

    @Override
    public Map<String, Alias> getAliases() {
        return aliases;
    }
}
