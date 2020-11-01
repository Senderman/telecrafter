package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class Respack implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ServerPropertiesProvider serverProperties;

    @Inject
    public Respack(TelegramProvider telegram, ServerPropertiesProvider serverProperties) {
        this.telegram = telegram;
        this.serverProperties = serverProperties;
    }

    @Override
    public String getCommand() {
        return "/respack";
    }

    @Override
    public String getDescription() {
        return "посмотреть или установить ресурспак";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        String[] args = message.getText().split("\\s+");
        if (args.length == 1)
            showResourcePackUrl(chatId);
        else
            setResourcePackUrl(chatId, args[1]);
    }

    private void showResourcePackUrl(long chatId) {
        String value = serverProperties.getProperty("resource-pack");
        if (value == null)
            telegram.sendMessage(chatId, "На сервере нет ресурспака!");
        else
            telegram.sendMessage(chatId, "Ссылка на ресурспак: " + value);
    }

    private void setResourcePackUrl(long chatId, String url) {
        serverProperties.setProperty("resource-pack", url);
        telegram.sendMessage(chatId, "Изменения будут применены после перезагрузки сервера");
    }
}
