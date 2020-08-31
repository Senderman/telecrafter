package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MineChat implements CommandExecutor {

    private final MinecraftProvider minecraft;
    private final TelecrafterBot telegram;


    public MineChat(MinecraftProvider minecraft, TelecrafterBot telegram) {
        this.minecraft = minecraft;
        this.telegram = telegram;
    }


    @Override
    public String getCommand() {
        return "/mc";
    }

    @Override
    public String getDescription() {
        return "отправить сообщение в игру";
    }

    @Override
    public void execute(Message message) throws TelegramApiException {
        String name = message.getFrom().getFirstName();
        String[] params = message.getText().split("\\s+", 2);
        if (params.length != 2) return;
        String msg = params[1];
        String messageToSend = String.format("%s (TG): %s", name, msg);
        minecraft.sendMessage(messageToSend);
        telegram.execute(new DeleteMessage(message.getChatId(), message.getMessageId()));
    }
}
