package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.config.ConfigLoader;
import com.senderman.telecrafter.config.ConfigProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

import java.io.File;
import java.io.IOException;

public class ReloadConfigCommand implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ConfigProvider configProvider;
    private final File configFile;

    public ReloadConfigCommand(TelegramProvider telegram, ConfigProvider configProvider, File configFile) {
        this.telegram = telegram;
        this.configProvider = configProvider;
        this.configFile = configFile;
    }

    @Override
    public String getCommand() {
        return "/confrel";
    }

    @Override
    public String getDescription() {
        return "релоаднуть конфиг с диска";
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        if (!configFile.exists()) {
            telegram.sendMessage(chatId, "❌ Конфиг-файл не найден!");
            return;
        }
        Config config;
        try {
            config = ConfigLoader.load(configFile);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "В процессе загрузки конфига произошла ошибка. См. логи для подробностей");
            e.printStackTrace();
            return;
        }
        configProvider.setCurrentConfig(config);
        telegram.sendMessage(chatId, "✅ Конфиг успешно обновлен!");
    }
}
