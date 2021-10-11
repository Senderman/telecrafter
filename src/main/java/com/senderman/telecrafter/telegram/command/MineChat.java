package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class MineChat implements CommandExecutor {

    private final MinecraftProvider minecraft;


    public MineChat(MinecraftProvider minecraft) {
        this.minecraft = minecraft;
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
    public void execute(Message message) {
        String name = message.getFrom().getFirstName();
        String[] params = message.getText().split("\\s+", 2);
        if (params.length != 2) return;
        String msg = params[1];
        String messageToSend = String.format("%s (TG): %s", name, msg);
        minecraft.sendMessage(messageToSend);
    }
}
