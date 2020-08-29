package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DisablePlugin implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final PluginManager pluginManager;

    public DisablePlugin(TelecrafterBot telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/displugin";
    }

    @Override
    public String getDescription() {
        return "выключить плагин";
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
        if (pluginManager.disablePlugin(pluginName))
            telegram.sendMessage("Плагин " + pluginName + " успешно выключен!");
        else
            telegram.sendMessage("Плагин " + pluginName + " не найден/уже выключен!");
    }
}
