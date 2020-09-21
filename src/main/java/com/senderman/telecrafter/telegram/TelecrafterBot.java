package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.minecraft.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.api.TelegramApiWrapper;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.api.entity.Update;
import com.senderman.telecrafter.telegram.command.CommandExecutor;
import com.senderman.telecrafter.telegram.command.CommandKeeper;
import org.bukkit.command.CommandException;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public class TelecrafterBot {

    private final Config config;
    private final MinecraftProvider minecraft;
    private final CommandKeeper commandKeeper;
    private final TelegramApiWrapper api;

    @Inject
    public TelecrafterBot(
            Config config,
            MinecraftProvider minecraft,
            ServerPropertiesProvider serverProperties,
            PluginManager pluginManager,
            TelegramApiWrapper telegramApiWrapper
    ) {
        this.config = config;
        this.minecraft = minecraft;
        this.api = telegramApiWrapper;
        this.commandKeeper = new CommandKeeper(this, minecraft, serverProperties, pluginManager);
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
                    sendMessage(chatId, "Команду можно использовать только в лс");
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
                    result -> sendMessage(chatId, result ? "Команда отправлена серверу!" : "Такой команды нет!"));
        } catch (CommandException e) {
            sendMessage(chatId, "<b>Ошибка выполнения команды!</b>\n\n" + exceptionToString(e));
        }
    }

    public void sendMessageAsync(long chatId, String text) {
        api.sendMessageAsync(chatId, text);
    }

    public void sendMessage(long chatId, String text) {
        api.sendMessage(chatId, text);
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

    public com.senderman.telecrafter.telegram.api.entity.File getFile(String fileId) {
        return api.getFile(fileId);
    }

    public Message sendDocument(long chatId, java.io.File file) {
        return api.sendDocument(chatId, file);
    }

    public File downloadFile(com.senderman.telecrafter.telegram.api.entity.File file, File output) throws IOException {
        String filePath = file.getFilePath();
        URL url = new URL("https://api.telegram.org/file/bot" + config.getBotToken() + "/" + filePath);
        try (
                FileOutputStream fos = new FileOutputStream(output);
                InputStream in = url.openConnection().getInputStream()
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1)
                fos.write(buffer, 0, length);
        }
        return output;
    }

    public String getBotUsername() {
        return config.getBotName();
    }

}
