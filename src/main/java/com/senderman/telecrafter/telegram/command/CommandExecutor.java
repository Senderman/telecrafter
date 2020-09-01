package com.senderman.telecrafter.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface CommandExecutor {

    String getCommand();

    String getDescription();

    default boolean adminsOnly() {
        return false;
    }

    default boolean pmOnly() {return false;}

    void execute(Message message) throws TelegramApiException;

}
