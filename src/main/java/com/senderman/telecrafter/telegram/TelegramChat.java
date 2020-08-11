package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;

public class TelegramChat {

    private final TelecrafterBot bot;

    @Inject
    public TelegramChat(TelecrafterBot bot) {
        this.bot = bot;
    }

    public void sendMessage(String message) {
        bot.sendMessage(message);
    }

}
