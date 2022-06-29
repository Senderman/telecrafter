package com.senderman.telecrafter.telegram.command.alias;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import org.bukkit.command.CommandException;

public class AliasExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    public AliasExecutor(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
    }

    public void execute(Alias alias, Message message) {

        long chatId = message.getChatId();

        try {
            minecraft.runCommand(alias.getCommand(),
                    successful -> telegram.sendMessage(chatId, successful ? "Команда отправлена серверу!" : "Такой команды нет!"));
        } catch (CommandException e) {
            telegram.sendMessage(chatId, "Ошибка выполнения команды. См. логи для подробностей");
        }
    }
}
