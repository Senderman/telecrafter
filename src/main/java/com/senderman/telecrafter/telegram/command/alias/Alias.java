package com.senderman.telecrafter.telegram.command.alias;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senderman.telecrafter.telegram.command.Role;

import java.util.EnumSet;

public class Alias {

    @JsonProperty(required = true)
    private String command;
    private String description;
    private EnumSet<Role> permissions;

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public EnumSet<Role> getPermissions() {
        return permissions;
    }

}
