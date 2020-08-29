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
        return "включить плагин";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        String[] params = message.getText().split("\\s+", 2);
        if (params.length < 2) {
            telegram.sendMessage(getCommand() + " pluginName");
            return;
        }
        String pluginName = params[1];
        if (pluginManager.enablePlugin(pluginName))
            telegram.sendMessage("Плагин " + pluginName + " успешно включен!");
        else
            telegram.sendMessage("Плагин " + pluginName + " не найден/уже выключен!");
    }
}
