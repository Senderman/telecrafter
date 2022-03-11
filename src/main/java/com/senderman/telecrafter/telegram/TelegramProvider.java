package com.senderman.telecrafter.telegram;

import com.senderman.telecrafter.config.Config;
import com.senderman.telecrafter.telegram.api.TelegramApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TelegramProvider {

    private final TelegramApi api;
    private final Config config;


    public TelegramProvider(TelegramApi api, Config config) {
        this.api = api;
        this.config = config;
    }

    public void sendMessage(long chatId, String text) {
        api.sendMessage(chatId, text);
    }

    public void sendMessageToMainChat(String message) {
        sendMessage(config.getChatId(), message);
    }

    public void sendDocument(long chatId, File file) {
        api.sendDocument(chatId, file);
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

}
