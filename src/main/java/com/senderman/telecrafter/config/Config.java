package com.senderman.telecrafter.config;

import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public interface Config {
    String getBotToken();

    String getBotName();

    Long getChatId();

    Set<Long> getAdmins();

    boolean isAdmin(long userId);

    boolean isAllowForeignChats();

    Map<String, Alias> getAliases();
}
