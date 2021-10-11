package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

import java.util.Set;

public class Help implements CommandExecutor {

    private final TelegramProvider telegram;
    private final Set<CommandExecutor> commands;

    public Help(TelegramProvider telegram, Set<CommandExecutor> commands) {
        this.telegram = telegram;
        this.commands = commands;
    }

    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "справка по командам";
    }

    @Override
    public void execute(Message message) {
        StringBuilder builder = new StringBuilder("<b>Справка по командам</b>\n\n");
        for (CommandExecutor command : commands) {
            builder.append(formatExecutor(command)).append("\n");
        }
        telegram.sendMessage(message.getChatId(), builder.toString());
    }

    private String formatExecutor(CommandExecutor executor) {
        String adminOnly = executor.adminsOnly() ? " (админам)" : " ";
        String pmOnly = executor.pmOnly() ? " (PM)" : " ";
        String options = (adminOnly + pmOnly).trim();
        return executor.getCommand() + " " + options + " - " + executor.getDescription();
    }
}
