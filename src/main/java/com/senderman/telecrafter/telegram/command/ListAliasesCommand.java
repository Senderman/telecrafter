package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import com.senderman.telecrafter.telegram.command.alias.Alias;

import java.util.Map;

public class ListAliasesCommand implements CommandExecutor {

    private final TelegramProvider telegram;
    private final Config config;

    public ListAliasesCommand(TelegramProvider telegram, Config config) {
        this.telegram = telegram;
        this.config = config;
    }

    @Override
    public String getCommand() {
        return "/aliases";
    }

    @Override
    public String getDescription() {
        return "список алиасов (кастомных команд)";
    }

    @Override
    public void execute(Message message) {
        var chatId = message.getChatId();
        Map<String, Alias> aliases = config.getAliases();
        if (aliases.isEmpty()) {
            telegram.sendMessage(chatId, "Алиасов не задано! Админы могут задать их в конфиг-файле");
            return;
        }
        StringBuilder builder = new StringBuilder("\uD83D\uDCA1 <b>Алиасы сервера:</b>\n\n");
        for (Map.Entry<String, Alias> entry : aliases.entrySet()) {
            builder.append(formatAlias(entry.getKey(), entry.getValue())).append("\n");
        }

        telegram.sendMessage(chatId, builder.toString());

    }

    private String formatAlias(String name, Alias alias) {
        String forAdmin = alias.isAdminOnly() ? " (админам)" : "";
        return "/" + name + forAdmin + " - " + alias.getDescription();
    }
}
