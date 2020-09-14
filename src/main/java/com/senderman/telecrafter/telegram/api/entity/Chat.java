package com.senderman.telecrafter.telegram.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat {

    private long id;
    private String type;

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public boolean isPrivate() {
        return type.equals("private");
    }
}
