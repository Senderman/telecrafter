package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

public class InstallPlugin implements CommandExecutor {

    private final TelecrafterBot telegram;
    private final PluginManager pluginManager;

    public InstallPlugin(TelecrafterBot telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/install";
    }

    @Override
    public String getDescription() {
        return "установить/обновить плагин";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        if (!validate(message)) {
            telegram.sendMessage("Ответьте этой командой на jar-файл с плагином!");
        }
        Document telegramDoc = message.getReplyToMessage().getDocument();
        File pluginFile;
        try {
            pluginFile = downloadDocument(telegramDoc);
        } catch (TelegramApiException e) {
            telegram.sendMessage("Не удалось получить документ: " + e.toString());
            return;
        }

        try {
            if (pluginManager.installPlugin(pluginFile))
                telegram.sendMessage("Плагин успешно установлен!");
            else
                telegram.sendMessage("Ошибка установки плагина: неверный формат файла плагина!");
        } catch (IOException e) {
            telegram.sendMessage("Ошибка установки плагина: " + e.toString());
        } finally {
            pluginFile.delete();
        }
    }

    private File downloadDocument(Document document) throws TelegramApiException {
        GetFile getFile = new GetFile()
                .setFileId(document.getFileId());
        org.telegram.telegrambots.meta.api.objects.File tgFile = telegram.execute(getFile);
        File pluginFile = new File(document.getFileName());
        return telegram.downloadFile(tgFile, pluginFile);
    }

    private boolean validate(Message message) {
        return message.isReply() &&
                message.getReplyToMessage().hasDocument() &&
                message.getReplyToMessage().getDocument().getFileName().endsWith(".jar");
    }
}
