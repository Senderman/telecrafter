package com.senderman.telecrafter.telegram.command.alias;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alias {

    @JsonProperty(required = true)
    private String command;
    private String description;
    @JsonProperty(defaultValue = "false")
    private boolean adminOnly;

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }
}
