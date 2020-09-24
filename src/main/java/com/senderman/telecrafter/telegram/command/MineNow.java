package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class MineNow implements CommandExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    @Inject
    public MineNow(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
    }

    @Override
    public String getCommand() {
        return "/mnow";
    }

    @Override
    public String getDescription() {
        return "посмотреть текущий онлайн";
    }

    @Override
    public void execute(Message message) {
        String messageToSend = "<b>Игроки на сервере:</b>\n\n" + minecraft.getOnlinePlayersNames();
        telegram.sendMessage(message.getChatId(), messageToSend);
    }
}
