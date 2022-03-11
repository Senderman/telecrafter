package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.command.CommandException;

import java.util.EnumSet;

public class RunCommand implements CommandExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    public RunCommand(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
    }

    @Override
    public String getCommand() {
        return "/rc";
    }

    @Override
    public String getDescription() {
        return "отправить команду на сервер";
    }

    @Override
    public EnumSet<Role> roles() {
        return EnumSet.of(Role.ADMIN);
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();

        String[] args = message.getText().split("\\s+", 2);
        if (args.length < 2) {
            telegram.sendMessage(chatId, "Использование: " + getCommand() + " команда");
            return;
        }

        String command = args[1];

        try {
            minecraft.runCommand(command,
                    successful -> telegram.sendMessage(chatId, successful ? "Команда отправлена серверу!" : "Такой команды нет!"));
        } catch (CommandException e) {
            telegram.sendMessage(chatId, "Ошибка выполнения команды. См. логи для подробностей");
        }
    }
}
