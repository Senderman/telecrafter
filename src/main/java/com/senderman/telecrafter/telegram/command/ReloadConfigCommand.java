package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.config.ConfigLoader;
import com.senderman.telecrafter.config.ConfigProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class ReloadConfigCommand implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ConfigProvider configProvider;
    private final FileConfiguration fileConfiguration;

    public ReloadConfigCommand(TelegramProvider telegram, ConfigProvider configProvider, FileConfiguration fileConfiguration) {
        this.telegram = telegram;
        this.configProvider = configProvider;
        this.fileConfiguration = fileConfiguration;
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
        if (fileConfiguration == null) {
            telegram.sendMessage(chatId, "❌ Конфиг-файл не найден!");
            return;
        }
        Config config;
        try {
            config = ConfigLoader.load(fileConfiguration);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "В процессе загрузки конфига произошла ошибка. См. логи для подробностей");
            e.printStackTrace();
            return;
        }
        configProvider.setCurrentConfig(config);
        telegram.sendMessage(chatId, "✅ Конфиг успешно обновлен!");
    }
}
