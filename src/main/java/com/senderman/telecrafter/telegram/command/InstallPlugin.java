package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.PluginManager;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Document;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class InstallPlugin implements CommandExecutor {

    private final TelegramProvider telegram;
    private final PluginManager pluginManager;

    public InstallPlugin(TelegramProvider telegram, PluginManager pluginManager) {
        this.telegram = telegram;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getCommand() {
        return "/install";
    }

    @Override
    public String getDescription() {
        return "установить/обновить плагин (реплаем на .jar файл)";
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    @Override
    public void execute(Message message) {
        var chatId = message.getChatId();
        if (!validateMessage(message)) {
            telegram.sendMessage(chatId, "Ответьте этой командой на jar-файл с плагином!");
            return;
        }
        File pluginFile;
        try {
            pluginFile = downloadDocument(message.getReply().getDocument());
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Не удалось получить документ: " + e);
            return;
        }
        PluginDescriptionFile newDesc;
        try {
            newDesc = pluginManager.getPluginDescription(pluginFile);
        } catch (InvalidDescriptionException e) {
            telegram.sendMessage(chatId, "Не удалось загрузить описание плагина: " + e);
            pluginFile.delete();
            return;
        }

        var oldPlugin = pluginManager.getPlugin(newDesc.getName());

        if (oldPlugin == null) { // if there's no older version of this plugin
            try {
                pluginManager.installPluginFile(pluginFile);
                String updateText = String.format(
                        "Плагин %s %s успешно установлен! Перезагрузите сервер для применения изменений",
                        newDesc.getName(), newDesc.getVersion());
                telegram.sendMessage(chatId, updateText);
            } catch (IOException e) {
                telegram.sendMessage(chatId, "Ошибка во время установки файла плагина: " + e);
                pluginFile.delete();
            }
        } else {
            pluginManager.deletePluginFile((JavaPlugin) oldPlugin);
            try {
                pluginManager.installPluginFile(pluginFile);
            } catch (IOException e) {
                telegram.sendMessage(
                        chatId,
                        "Старый плагин был удален, но произошла ошибка во время установки файла нового плагина: " + e
                );
                return;
            }
            var oldDesc = oldPlugin.getDescription();
            String updateText = String.format(
                    "Плагин %s успешно обновлен с %s до %s! Перезагрузите сервер для применения изменений",
                    newDesc.getName(), oldDesc.getVersion(), newDesc.getVersion());
            telegram.sendMessage(chatId, updateText);
        }

    }

    private boolean validateMessage(Message message) {
        return message.isReply() &&
                message.getReply().hasDocument() &&
                message.getReply().getDocument().getFileName().endsWith(".jar");
    }

    private File downloadDocument(Document doc) throws IOException {
        com.senderman.telecrafter.telegram.api.entity.File file = telegram.getFile(doc.getFileId());
        File newFile = new File(doc.getFileName());
        return telegram.downloadFile(file, newFile);
    }
}
