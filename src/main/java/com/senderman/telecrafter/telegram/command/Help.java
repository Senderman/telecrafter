package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public class Help implements CommandExecutor {

    private final TelecrafterBot telecrafterBot;
    private final Map<String, CommandExecutor> commands;

    public Help(TelecrafterBot telecrafterBot, Map<String, CommandExecutor> commands) {
        this.telecrafterBot = telecrafterBot;
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
        for (CommandExecutor command : commands.values()) {
            builder.append(formatExecutor(command)).append("\n");
        }
        telecrafterBot.sendMessage(message.getChatId(), builder.toString());
    }

    private String formatExecutor(CommandExecutor executor) {
        String adminOnly = executor.adminsOnly() ? " (админам)" : " ";
        String pmOnly = executor.pmOnly() ? " (PM)" : " ";
        String options = (adminOnly + pmOnly).trim();
        return executor.getCommand() + " " + options + " - " + executor.getDescription();
    }
}
