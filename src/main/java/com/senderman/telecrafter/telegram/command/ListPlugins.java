package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

public class ListPlugins implements CommandExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    public ListPlugins(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
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
        String plugins = minecraft.getPlugins();
        telegram.sendMessage(message.getChatId(), "<b>Плагины на сервере:</b>\n\n" + plugins);
    }
}
