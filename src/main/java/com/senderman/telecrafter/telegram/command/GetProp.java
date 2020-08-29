package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class GetProp implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final ServerPropertiesProvider serverProperties;

    public GetProp(TelecrafterBot telegram, ServerPropertiesProvider serverProperties) {
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
        String[] params = message.getText().split("\\s+");
        if (params.length < 2) {
            telegram.sendMessage("Неверное количество аргументов! " + getCommand() + " key");
            return;
        }

        String key = params[1];
        String value = serverProperties.getProperty(key);
        if (value == null)
            telegram.sendMessage("Нет значения для " + key);
        else
            telegram.sendMessage(key + "=" + value);
    }
}
