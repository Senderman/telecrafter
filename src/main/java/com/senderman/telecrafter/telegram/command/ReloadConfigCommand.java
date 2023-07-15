package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.config.ConfigLoader;
import com.senderman.telecrafter.config.ConfigProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class ReloadConfigCommand implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ConfigProvider configProvider;
    private final Plugin plugin;

    public ReloadConfigCommand(TelegramProvider telegram, ConfigProvider configProvider, Plugin plugin) {
        this.telegram = telegram;
        this.configProvider = configProvider;
        this.plugin = plugin;
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
        plugin.reloadConfig();
        Config config;
        try {
            config = ConfigLoader.load(plugin.getConfig());
        } catch (Exception e) {
            telegram.sendMessage(chatId, "В процессе загрузки конфига произошла ошибка. См. логи для подробностей");
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return;
        }
        configProvider.setCurrentConfig(config);
        telegram.sendMessage(chatId, "✅ Конфиг успешно обновлен!");
    }
}
