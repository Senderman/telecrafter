package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DeletePlugin implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final PluginManager pluginManager;

    public DeletePlugin(TelecrafterBot telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/rmplugin";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        String[] params = message.getText().split("\\s+", 2);
        if (params.length < 2) {
            telegram.sendMessage("Неверное использование команды! " + getCommand() + " plugin.jar");
            return;
        }

        String pluginFileName = params[1];
        if (pluginManager.deletePlugin(pluginFileName))
            telegram.sendMessage("Плагин " + pluginFileName + " успешно удален!");
        else
            telegram.sendMessage("Не удалось удалить плагин " + pluginFileName);

    }
}
