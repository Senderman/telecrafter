package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import com.senderman.telecrafter.telegram.api.entity.Document;
import com.senderman.telecrafter.telegram.api.entity.Message;

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
        return "установить/обновить плагин. При обновлении релоадит сервер";
    }

    @Override
    public boolean adminsOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        if (!validate(message)) {
            telegram.sendMessage(chatId, "Ответьте этой командой на jar-файл с плагином!");
            return;
        }

        Document telegramDoc = message.getReply().getDocument();
        File pluginFile;
        try {
            pluginFile = downloadDocument(telegramDoc);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Не удалось получить документ: " + e.toString());
            return;
        }

        try {
            if (pluginManager.installPlugin(pluginFile))
                telegram.sendMessage(chatId, "Плагин успешно установлен!");
            else
                telegram.sendMessage(chatId, "Ошибка установки плагина: неверный формат файла плагина!");
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Ошибка установки плагина: " + e.toString());
        } finally {
            pluginFile.delete();
        }
    }

    private File downloadDocument(Document document) throws IOException {
        File pluginFile = new File(document.getFileName());
        return telegram.downloadFile(telegram.getFile(document.getFileId()), pluginFile);
    }

    private boolean validate(Message message) {
        return message.isReply() &&
                message.getReply().hasDocument() &&
                message.getReply().getDocument().getFileName().endsWith(".jar");
    }
}
