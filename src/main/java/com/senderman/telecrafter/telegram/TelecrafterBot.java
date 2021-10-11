package com.senderman.telecrafter.telegram;

import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.api.entity.Update;
import com.senderman.telecrafter.telegram.command.CommandExecutor;
import com.senderman.telecrafter.telegram.command.CommandKeeper;

import java.util.Optional;

public class TelecrafterBot {

    private final Config config;
    private final CommandKeeper commandKeeper;
    private final TelegramProvider telegram;


    public TelecrafterBot(Config config, CommandKeeper commandKeeper, TelegramProvider telegram) {
        this.config = config;
        this.commandKeeper = commandKeeper;
        this.telegram = telegram;
    }


    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) return;

        Message message = update.getMessage();

        if (message.getDate() + 120 < System.currentTimeMillis() / 1000) return;

        if (!message.hasText()) return;
        String text = message.getText();
        long chatId = message.getChatId();

        if (!message.isCommand()) return;

        String command = text
                .split("\\s+", 2)[0]
                .toLowerCase()
                .replaceAll("@" + getBotUsername(), "");

        if (command.contains("@")) return; // skip other's bot commands

        Optional.ofNullable(commandKeeper.getExecutor(command)).ifPresent(executor -> {
            if (userHasPermission(executor, message.getFrom().getId())) {
                if (executor.pmOnly() && !message.isUserMessage()) {
                    telegram.sendMessage(chatId, "Команду можно использовать только в лс");
                    return;
                }
                executor.execute(message);
            }
        });

    }

    private boolean userHasPermission(CommandExecutor executor, int userId) {
        return !executor.adminsOnly() || config.isAdmin(userId);
    }

    public String getBotUsername() {
        return config.getBotName();
    }

}
