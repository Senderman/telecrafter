package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class SetProp implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final ServerPropertiesProvider serverProperties;

    public SetProp(TelecrafterBot telegram, ServerPropertiesProvider serverProperties) {
        this.telegram = telegram;
        this.serverProperties = serverProperties;
    }

    @Override
    public String getCommand() {
        return "/setprop";
    }

    @Override
    public String getDescription() {
        return "выставить проперти в server.properties";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        String[] params = message.getText().split("\\s+", 3);
        if (params.length < 3) {
            telegram.sendMessage(chatId, "Неверный формат! " + getCommand() + " key value");
            return;
        }
        if (serverProperties.setProperty(params[1], params[2]))
            telegram.sendMessage(chatId, "Новое значение для " + params[1] + " установлено!");
        else
            telegram.sendMessage(chatId, "Такого ключа нет!");
    }
}
