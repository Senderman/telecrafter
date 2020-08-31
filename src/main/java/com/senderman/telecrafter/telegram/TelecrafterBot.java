package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.command.CommandExecutor;
import com.senderman.telecrafter.telegram.command.CommandKeeper;
import org.bukkit.command.CommandException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class TelecrafterBot extends TelegramLongPollingBot {

    private final Config config;
    private final MinecraftProvider minecraft;
    private final CommandKeeper commandKeeper;

    @Inject
    public TelecrafterBot(
            Config config,
            MinecraftProvider minecraft,
            ServerPropertiesProvider serverProperties,
            PluginManager pluginManager
    ) {
        this.config = config;
        this.minecraft = minecraft;
        this.commandKeeper = new CommandKeeper(this, minecraft, serverProperties, pluginManager);
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage()) return;

        Message message = update.getMessage();

        if (!message.getChatId().equals(config.getChatId())) return;
        if (message.getDate() + 120 < System.currentTimeMillis() / 1000) return;

        String text = message.getText();

        if (text.startsWith("!") && config.isAdmin(message.getFrom().getId())) {
            String command = text.replaceFirst("!", "");
            runCommandFromText(command);
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
                try {
                    executor.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean userHasPermission(CommandExecutor executor, int userId) {
        return !executor.adminsOnly() || config.isAdmin(userId);
    }

    private void runCommandFromText(String text) {
        try {
            minecraft.runCommand(text.replaceFirst("!", ""),
                    result -> sendMessage(result ? "Команда отправлена серверу!" : "Такой команды нет!"));
        } catch (CommandException e) {
            sendMessage("<b>Ошибка выполнения команды!</b>\n\n" + exceptionToString(e));
        }
    }

    public void sendMessage(String text) {
        try {
            execute(new SendMessage(config.getChatId(), "⛏ " + text).enableHtml(true));
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
