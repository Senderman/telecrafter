package com.senderman.telecrafter.telegram.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    @JsonProperty("message_id")
    private int messageId;
    private User from;
    private int date;
    @JsonProperty("reply_to_message")
    private Message reply;
    private String text;
    private Document document;
    private Chat chat;

    public int getMessageId() {
        return messageId;
    }

    public User getFrom() {
        return from;
    }

    public int getDate() {
        return date;
    }

    public Message getReply() {
        return reply;
    }

    public String getText() {
        return text;
    }

    public Document getDocument() {
        return document;
    }

    public boolean isReply() {
        return reply != null;
    }

    public boolean hasDocument() {
        return document != null;
    }

    public boolean hasText() {
        return text != null;
    }

    public Chat getChat() {
        return chat;
    }

    public long getChatId() {
        return chat.getId();
    }

    public boolean isCommand() {
        return text.startsWith("/");
    }

    public boolean isUserMessage() {
        return chat.isPrivate();
    }
}
