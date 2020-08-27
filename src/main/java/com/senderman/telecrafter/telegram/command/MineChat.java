package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import org.telegram.telegrambots.meta.api.objects.Message;

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
    public void execute(Message message) {
        String name = message.getFrom().getFirstName();
        String[] params = message.getText().split("\\s+", 2);
        if (params.length != 2) return;
        String msg = params[1];
        String messageToSend = String.format("%s (TG): %s", name, msg);
        minecraft.sendMessage(messageToSend);
    }
}
