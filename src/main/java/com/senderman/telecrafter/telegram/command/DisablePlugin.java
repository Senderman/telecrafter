package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class DisablePlugin implements CommandExecutor {

    private final TelegramProvider telegram;
    private final PluginManager pluginManager;

    @Inject
    public DisablePlugin(TelegramProvider telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/displugin";
    }

    @Override
    public String getDescription() {
        return "отдать команду выключения плагина";
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
            telegram.sendMessage(chatId, getCommand() + " pluginName");
            return;
        }
        String pluginName = params[1];
        if (pluginManager.disablePlugin(pluginName))
            telegram.sendMessage(chatId, "Плагин " + pluginName + " скоро будет выключен!");
        else
            telegram.sendMessage(chatId, "Плагин " + pluginName + " не найден/уже выключен!");
    }
}
