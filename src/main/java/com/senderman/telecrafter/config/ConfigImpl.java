package com.senderman.telecrafter.config;

import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public class ConfigImpl implements Config {

    private final String botToken;

    private final String botName;

    private final Long chatId;

    private final boolean allowForeignChats;

    private final Set<Long> admins;

    private final Set<Long> ignoredUsers;

    private final Set<String> forceAdminCommands;

    private final Map<String, Alias> aliases;

    public ConfigImpl(
            String botToken,
            String botName,
            Long chatId,
            boolean allowForeignChats,
            Set<Long> admins,
            Set<Long> ignoredUsers,
            Set<String> forceAdminCommands,
            Map<String, Alias> aliases) {
        this.botToken = botToken;
        this.botName = botName;
        this.chatId = chatId;
        this.allowForeignChats = allowForeignChats;
        this.admins = admins;
        this.ignoredUsers = ignoredUsers;
        this.forceAdminCommands = forceAdminCommands;
        this.aliases = aliases;
    }


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
