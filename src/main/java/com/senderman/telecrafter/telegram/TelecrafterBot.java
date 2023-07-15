package com.senderman.telecrafter.telegram;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.api.entity.Update;
import com.senderman.telecrafter.telegram.command.CommandKeeper;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import com.senderman.telecrafter.telegram.command.alias.Alias;
import com.senderman.telecrafter.telegram.command.alias.AliasExecutor;

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

        if (config.isIgnored(message.getFrom().getId())) return;

        if (!message.hasText()) return;
        long chatId = message.getChatId();

        if (!message.isCommand()) return;

        String command = extractCommand(message);
        if (command.contains("@")) return; // skip other's bots commands

        var executor = commandKeeper.getExecutor(command);
        if (executor != null) {
            if (!userHasPermission(executor, message)) {
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
            if (!userHasPermission(alias, message)) {
                telegram.sendMessage(chatId, "Простите, но вы не можете использовать эту команду.");
                return;
            }
            aliasExecutor.execute(alias, message);
        }

    }

    /**
     * Extract bot command from Message
     *
     * @param message Telegram Message object
     * @return extracted /command. If the command refers to another bot (bot name is different from botName in the config),
     * will return /command@botname
     */
    private String extractCommand(Message message) {
        return message.getText()
                .split("\\s+", 2)[0]
                .toLowerCase()
                .replaceAll("@" + getBotUsername().toLowerCase(), "");
    }

    private boolean userHasPermission(CommandExecutor executor, Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        if (executor.adminOnly() || config.isForcedAdminCommand(executor.getCommand())) {
            return config.isAdmin(userId);
        }
        return config.isAdmin(userId) ||
                config.isAllowForeignChats() ||
                chatId == config.getChatId();
    }

    private boolean userHasPermission(Alias alias, Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        if (alias.isAdminOnly()) {
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
