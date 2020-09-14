package com.senderman.telecrafter.telegram.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int id;
    @JsonProperty("is_bot")
    private boolean isBot;
    @JsonProperty("first_name")
    private String firstName;

    public int getId() {
        return id;
    }

    public boolean isBot() {
        return isBot;
    }

    public String getFirstName() {
        return firstName;
    }
}
