package com.senderman.telecrafter.telegram.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

public class ClearDrop implements CommandExecutor {

    private final MinecraftProvider minecraftProvider;
    private final TelegramProvider telegram;

    @Inject
    public ClearDrop(MinecraftProvider minecraftProvider, TelegramProvider telegram) {
        this.minecraftProvider = minecraftProvider;
        this.telegram = telegram;
    }

    @Override
    public String getCommand() {
        return "/cleardrop";
    }

    @Override
    public String getDescription() {
        return "очистить весь дроп из всех миров";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        telegram.sendMessageToMainChat("Начато удаление дропа из всех миров, ожидайте...");
        minecraftProvider.removeAndCountDrop(
                result -> telegram.sendMessageToMainChat("Удалено " + result + " предметов")
        );
    }
}
