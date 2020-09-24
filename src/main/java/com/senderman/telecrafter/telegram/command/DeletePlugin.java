package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class DeletePlugin implements CommandExecutor {

    private final TelegramProvider telegram;
    private final PluginManager pluginManager;

    @Inject
    public DeletePlugin(TelegramProvider telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/rmplugin";
    }

    @Override
    public String getDescription() {
        return "удалить плагин";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        String[] params = message.getText().split("\\s+", 2);
        if (params.length < 2) {
            telegram.sendMessage(chatId, "Неверное использование команды! " + getCommand() + " plugin.jar");
            return;
        }

        String pluginFileName = params[1];
        if (pluginManager.deletePlugin(pluginFileName))
            telegram.sendMessage(chatId, "Плагин " + pluginFileName + " скоро будет выгружен. Полное удаление после запуска релоада");
        else
            telegram.sendMessage(chatId, "Не удалось удалить плагин " + pluginFileName);

    }
}
