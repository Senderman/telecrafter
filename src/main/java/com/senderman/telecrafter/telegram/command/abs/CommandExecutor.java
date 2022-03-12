package com.senderman.telecrafter.telegram.command.abs;

import com.senderman.telecrafter.telegram.api.entity.Message;

public interface CommandExecutor {

    String getCommand();

    String getDescription();

    default boolean adminOnly() {
        return false;
    }

    default boolean pmOnly() {
        return false;
    }

    void execute(Message message);

}
