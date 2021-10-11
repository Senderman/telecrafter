package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetLogs implements CommandExecutor {

    private final TelegramProvider telegram;
    private final File logsDir;

    public GetLogs(TelegramProvider telegram, MinecraftProvider minecraft) {
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
    public void execute(Message message) {
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
        telegram.sendDocument(chatId, logsToSend);
    }

    private void sendLogList(long chatId) {
        String logList = listFiles();
        File logListFile = new File("LogList.txt");
        try (Writer writer = new BufferedWriter(new FileWriter(logListFile))) {
            writer.write(logList);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Не удалось создать файл со списком");
            return;
        }
        telegram.sendDocument(chatId, logListFile);
        logListFile.delete();
    }

    private String listFiles() {
        return Stream.of(Objects.requireNonNull(logsDir.listFiles()))
                .map(File::getName)
                .collect(Collectors.joining("\n"));
    }
}
