package com.senderman.telecrafter.minecraft;

import com.senderman.telecrafter.telegram.TelegramProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TgCommandExecutor implements CommandExecutor {

    private final TelegramProvider telegram;

    public TgCommandExecutor(TelegramProvider telegram) {
        this.telegram = telegram;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("telecrafter.command")) {
            sender.sendMessage("У вас нет прав на использование данной команды!");
            return true;
        }
        if (args.length == 0)
            return false;

        telegram.sendMessageToMainChat("\uD83D\uDCAC [" + sender.getName() + "] " + String.join(" ", args));
        return true;
    }
}
