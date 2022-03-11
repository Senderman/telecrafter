package com.senderman.telecrafter.config;

import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;
import java.util.Set;

public class ConfigProvider implements Config {

    private Config currentConfig;

    public ConfigProvider(Config currentConfig) {
        this.currentConfig = currentConfig;
    }

    @Override
    public String getBotToken() {
        return currentConfig.getBotToken();
    }

    @Override
    public String getBotName() {
        return currentConfig.getBotName();
    }

    @Override
    public Long getChatId() {
        return currentConfig.getChatId();
    }

    @Override
    public Set<Long> getAdmins() {
        return currentConfig.getAdmins();
    }

    @Override
    public boolean isAdmin(long userId) {
        return currentConfig.isAdmin(userId);
    }

    @Override
    public boolean isAllowForeignChats() {
        return currentConfig.isAllowForeignChats();
    }

    @Override
    public Map<String, Alias> getAliases() {
        return currentConfig.getAliases();
    }

    public Config getCurrentConfig() {
        return currentConfig;
    }

    public void setCurrentConfig(Config currentConfig) {
        this.currentConfig = currentConfig;
    }
}