package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class GetProp implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ServerPropertiesProvider serverProperties;

    @Inject
    public GetProp(TelegramProvider telegram, ServerPropertiesProvider serverProperties) {
        this.telegram = telegram;
        this.serverProperties = serverProperties;
    }

    @Override
    public String getCommand() {
        return "/getprop";
    }

    @Override
    public String getDescription() {
        return "прочитать проперти из server.properties";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        String[] params = message.getText().split("\\s+");
        if (params.length < 2) {
            telegram.sendMessage(chatId, "Неверное количество аргументов! " + getCommand() + " key");
            return;
        }

        String key = params[1];
        String value = serverProperties.getProperty(key);
        if (value == null)
            telegram.sendMessage(chatId, "Нет значения для " + key);
        else
            telegram.sendMessage(chatId, key + "=" + value);
    }
}
