package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetLogs implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final File logsDir;

    public GetLogs(TelecrafterBot telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.logsDir = minecraft.getLogsDirectory();
    }

    @Override
    public String getCommand() {
        return "/logs";
    }

    @Override
    public String getDescription() {
        return "получить логи сервера. " +
                getCommand() + " list для получения списка логов, " +
                getCommand() + " fileName для получения конкретного лога";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public boolean pmOnly() {
        return true;
    }

    @Override
    public void execute(Message message) throws TelegramApiException {
        long chatId = message.getChatId();
        String[] params = message.getText().split("\\s+", 2);
        String logName = params.length == 1 ? "latest.log" : params[1];
        if (logName.equals("list")) {
            sendLogList(chatId);
            return;
        }
        File logsToSend = new File(logsDir, logName);
        if (!logsToSend.exists()) {
            telegram.sendMessage(chatId, "Файл не найден!");
            return;
        }
        SendDocument sendDocument = new SendDocument()
                .setChatId(chatId)
                .setDocument(logsToSend);
        telegram.execute(sendDocument);
    }

    private void sendLogList(long chatId) throws TelegramApiException {
        String logList = listFiles();
        File logListFile = new File("LogList.txt");
        try (Writer writer = new BufferedWriter(new FileWriter(logListFile))) {
            writer.write(logList);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Не удалось создать файл со списком");
            return;
        }
        SendDocument sendDocument = new SendDocument()
                .setChatId(chatId)
                .setDocument(logListFile)
                .setCaption("Список логов сервера");
        telegram.execute(sendDocument);
        logListFile.delete();
    }

    private String listFiles() {
        return Stream.of(Objects.requireNonNull(logsDir.listFiles()))
                .map(File::getName)
                .collect(Collectors.joining("\n"));
    }
}
