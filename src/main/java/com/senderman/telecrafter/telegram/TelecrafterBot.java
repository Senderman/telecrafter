package com.senderman.telecrafter.telegram;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.api.entity.Update;
import com.senderman.telecrafter.telegram.command.Role;
import com.senderman.telecrafter.telegram.command.alias.Alias;
import com.senderman.telecrafter.telegram.command.alias.AliasExecutor;

import java.util.EnumSet;

public class TelecrafterBot {

    private final Config config;
    private final CommandKeeper commandKeeper;
    private final AliasExecutor aliasExecutor;
    private final TelegramProvider telegram;


    public TelecrafterBot(Config config, CommandKeeper commandKeeper, AliasExecutor aliasExecutor, TelegramProvider telegram) {
        this.config = config;
        this.commandKeeper = commandKeeper;
        this.aliasExecutor = aliasExecutor;
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

        var executor = commandKeeper.getExecutor(command);
        if (executor != null) {
            if (!userHasPermission(executor.roles(), message)) {
                telegram.sendMessage(chatId, "Простите, но вы не можете использовать эту команду.");
                return;
            }
            if (executor.pmOnly() && !message.isUserMessage()) {
                telegram.sendMessage(chatId, "Команду можно использовать только в лс");
                return;
            }
            executor.execute(message);
        } else { // try to get and execute alias
            Alias alias = config.getAliases().get(command.replaceFirst("/", ""));
            if (alias == null) return;
            aliasExecutor.execute(alias,
                    roles -> userHasPermission(roles, message),
                    callback -> telegram.sendMessage(message.getChatId(), callback)
            );
        }

    }

    private boolean userHasPermission(EnumSet<Role> roles, Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        if (roles.contains(Role.ADMIN)) {
            return config.isAdmin(userId);
        }
        return config.isAdmin(userId) ||
                config.isAllowForeignChats() ||
                chatId == config.getChatId();
    }

    public String getBotUsername() {
        return config.getBotName();
    }

}
