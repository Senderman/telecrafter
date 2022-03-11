package com.senderman.telecrafter.telegram.command.abs;

import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.Role;

import java.util.EnumSet;

public interface CommandExecutor {

    String getCommand();

    String getDescription();

    default EnumSet<Role> roles() {
        return EnumSet.of(Role.USER);
    }

    default boolean pmOnly() {
        return false;
    }

    void execute(Message message);

}
