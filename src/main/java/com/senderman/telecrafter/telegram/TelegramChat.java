package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;

public class TelegramChat {

    private final TelecrafterBot bot;
    private final Config config;

    @Inject
    public TelegramChat(TelecrafterBot bot, Config config) {
        this.bot = bot;
        this.config = config;
    }

    public void sendMessage(String message) {
        bot.sendMessageAsync(config.getChatId(), message);
    }

}
