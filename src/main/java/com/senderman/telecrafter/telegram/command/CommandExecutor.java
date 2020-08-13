package com.senderman.telecrafter.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandExecutor {

    String getCommand();

    default boolean adminsOnly() {
        return false;
    }

    void execute(Message message);

}
