package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class ListPlugins implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final PluginManager pluginManager;

    public ListPlugins(TelecrafterBot telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/plugins";
    }

    @Override
    public String getDescription() {
        return "посмотреть список плагинов";
    }

    @Override
    public void execute(Message message) {
        String plugins = pluginManager.listPlugins();
        telegram.sendMessage(message.getChatId(), "<b>Плагины на сервере:</b>\n\n" + plugins);
    }
}
