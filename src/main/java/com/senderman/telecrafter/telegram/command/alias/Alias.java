package com.senderman.telecrafter.telegram.command.alias;

public class Alias {

    private final String command;

    private final String description;

    private final boolean adminOnly;

    public Alias(String command, String description, boolean adminOnly) {
        this.command = command;
        this.description = description;
        this.adminOnly = adminOnly;
    }

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
