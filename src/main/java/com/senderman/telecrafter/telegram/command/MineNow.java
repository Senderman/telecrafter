package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MineNow implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final MinecraftProvider minecraft;

    public MineNow(TelecrafterBot telegram, MinecraftProvider minecraft) {
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
        telegram.sendMessage(messageToSend);
    }
}
