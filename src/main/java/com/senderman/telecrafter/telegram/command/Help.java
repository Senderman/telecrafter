package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

import java.util.Set;

public class Help implements CommandExecutor {

    private final TelegramProvider telegram;
    private final Set<CommandExecutor> commands;
    private final Config config;

    public Help(TelegramProvider telegram, Set<CommandExecutor> commands, Config config) {
        this.telegram = telegram;
        this.commands = commands;
        this.config = config;
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

    private String formatExecutor(CommandExecutor exe) {
        String adminOnly = exe.adminOnly() || config.isForcedAdminCommand(exe.getCommand()) ? " (админам)" : " ";
        String pmOnly = exe.pmOnly() ? " (PM)" : " ";
        String options = (adminOnly + pmOnly).trim();
        return exe.getCommand() + " " + options + " - " + exe.getDescription();
    }
}
