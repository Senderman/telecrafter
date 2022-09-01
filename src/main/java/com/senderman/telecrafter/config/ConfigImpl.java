package com.senderman.telecrafter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public class ConfigImpl implements Config {

    @JsonProperty(required = true)
    private String botToken;

    @JsonProperty(required = true)
    private String botName;

    @JsonProperty(required = true)
    private Long chatId;

    @JsonProperty(defaultValue = "false")
    private boolean allowForeignChats;

    @JsonProperty(required = true)
    private Set<Long> admins;

    @JsonProperty(required = true)
    private Set<Long> ignoredUsers;

    @JsonProperty(required = true)
    private Set<String> forceAdminCommands;

    @JsonProperty(required = true)
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
    public Set<Long> getIgnoredUsers() {
        return ignoredUsers;
    }

    @Override
    public boolean isIgnored(long userId) {
        return ignoredUsers.contains(userId);
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
