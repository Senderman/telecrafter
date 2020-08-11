package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelecrafterBot extends TelegramLongPollingBot {

    private final Config config;
    private final MinecraftProvider minecraft;

    @Inject
    public TelecrafterBot(Config config, MinecraftProvider minecraft) {
        this.config = config;
        this.minecraft = minecraft;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        if (!message.isCommand()) return;

        String text = message.getText();
        String command = text
                .split("\\s+", 2)[0]
                .toLowerCase()
                .replaceAll("@" + getBotUsername(), "");

        if (command.contains("@")) return; // skip other's bot commands

        switch (command) {
            case "/mchat": {
                String name = message.getFrom().getFirstName();
                String[] params = text.split("\\s+", 2);
                if (params.length != 2) return;
                String msg = params[1];
                String messageToSend = String.format("%s (TG): %s", name, msg);
                minecraft.sendMessage(messageToSend);
                break;
            }

            case "/mnow": {
                String messageToSend = "<b>Игроки на сервере:</b>\n\n" + minecraft.getOnlinePlayersNames();
                sendMessage(messageToSend);
                break;
            }
        }


    }

    public void sendMessage(String text) {
        try {
            execute(new SendMessage(config.getChatId(), text).enableHtml(true));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
