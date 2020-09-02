package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class EnablePlugin implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final PluginManager pluginManager;

    public EnablePlugin(TelecrafterBot telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/enplugin";
    }

    @Override
    public String getDescription() {
        return "отдать команду включения плагина";
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
        if (pluginManager.enablePlugin(pluginName))
            telegram.sendMessage(chatId, "Плагин " + pluginName + " скоро будет включен!");
        else
            telegram.sendMessage(chatId, "Плагин " + pluginName + " не найден/уже включен!");
    }
}
