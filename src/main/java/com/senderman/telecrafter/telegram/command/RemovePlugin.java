package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class RemovePlugin implements CommandExecutor {

    private final TelegramProvider telegram;
    private final PluginManager pluginManager;

    public RemovePlugin(TelegramProvider telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/rmplugin";
    }

    @Override
    public String getDescription() {
        return "удалить плагин. " + getCommand() + " имя-плагина";
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        var chatId = message.getChatId();
        String[] args = message.getText().split("\\s+", 2);
        if (args.length < 2) {
            telegram.sendMessage(chatId, "Использование: " + getCommand() + " имя-плагина");
            return;
        }
        var pluginName = args[1];
        var plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) {
            telegram.sendMessage(chatId, "Такого плагина нет!");
            return;
        }
        pluginManager.deletePluginFile((JavaPlugin) plugin);
        telegram.sendMessage(chatId,
                "Плагин " + pluginName + " успешно удален! Перезагрузите сервер для применения изменений");
    }
}
