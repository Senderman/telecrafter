package com.senderman.telecrafter.telegram.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> {

    private boolean ok;
    private T result;

    public boolean isOk() {
        return ok;
    }

    public T getResult() {
        return result;
    }
}
