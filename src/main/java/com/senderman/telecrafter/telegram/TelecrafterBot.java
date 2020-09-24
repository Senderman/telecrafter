package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.api.entity.Update;
import com.senderman.telecrafter.telegram.command.CommandExecutor;
import com.senderman.telecrafter.telegram.command.CommandKeeper;
import org.bukkit.command.CommandException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class TelecrafterBot {

    private final Config config;
    private final MinecraftProvider minecraft;
    private final CommandKeeper commandKeeper;
    private final TelegramProvider telegram;

    @Inject
    public TelecrafterBot(
            Config config,
            MinecraftProvider minecraft,
            CommandKeeper commandKeeper,
            TelegramProvider telegram
    ) {
        this.config = config;
        this.minecraft = minecraft;
        this.commandKeeper = commandKeeper;
        this.telegram = telegram;
    }


    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) return;

        Message message = update.getMessage();

        if (message.getDate() + 120 < System.currentTimeMillis() / 1000) return;

        if (!message.hasText()) return;
        String text = message.getText();
        long chatId = message.getChatId();

        if (text.startsWith("!") && config.isAdmin(message.getFrom().getId())) {
            String command = text.replaceFirst("!", "");
            runCommandFromText(chatId, command);
            return;
        }

        if (!message.isCommand()) return;

        String command = text
                .split("\\s+", 2)[0]
                .toLowerCase()
                .replaceAll("@" + getBotUsername(), "");

        if (command.contains("@")) return; // skip other's bot commands

        Optional.ofNullable(commandKeeper.getExecutor(command)).ifPresent(executor -> {
            if (userHasPermission(executor, message.getFrom().getId())) {
                if (executor.pmOnly() && !message.isUserMessage()) {
                    telegram.sendMessage(chatId, "Команду можно использовать только в лс");
                    return;
                }
                executor.execute(message);
            }
        });

    }

    private boolean userHasPermission(CommandExecutor executor, int userId) {
        return !executor.adminsOnly() || config.isAdmin(userId);
    }

    private void runCommandFromText(long chatId, String text) {
        try {
            minecraft.runCommand(text.replaceFirst("!", ""),
                    result -> telegram.sendMessage(chatId, result ? "Команда отправлена серверу!" : "Такой команды нет!"));
        } catch (CommandException e) {
            telegram.sendMessage(chatId, "<b>Ошибка выполнения команды!</b>\n\n" + exceptionToString(e));
        }
    }

    private String exceptionToString(Exception e) {
        try (
                StringWriter string = new StringWriter();
                PrintWriter pw = new PrintWriter(string)
        ) {
            e.printStackTrace(pw);
            return string.toString();
        } catch (IOException ioException) {
            throw new IllegalStateException(ioException);
        }
    }

    public String getBotUsername() {
        return config.getBotName();
    }

}
