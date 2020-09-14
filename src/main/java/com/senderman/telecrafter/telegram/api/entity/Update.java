package com.senderman.telecrafter.telegram.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    @JsonProperty("update_id")
    private int updateId;
    private Message message;

    public Message getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public int getUpdateId() {
        return updateId;
    }
}
