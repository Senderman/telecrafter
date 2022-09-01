package com.senderman.telecrafter.config;

import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public interface Config {

    String getBotToken();

    String getBotName();

    Long getChatId();

    boolean isAllowForeignChats();

    Set<Long> getAdmins();

    boolean isAdmin(long userId);

    Set<String> getForceAdminCommands();

    Set<Long> getIgnoredUsers();

    boolean isIgnored(long userId);

    boolean isForcedAdminCommand(String command);

    Map<String, Alias> getAliases();

}
