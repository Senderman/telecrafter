package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.provider.ServerPropertiesProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;

public class Respack implements CommandExecutor {

    private final TelegramProvider telegram;
    private final ServerPropertiesProvider serverProperties;

    public Respack(TelegramProvider telegram, ServerPropertiesProvider serverProperties) {
        this.telegram = telegram;
        this.serverProperties = serverProperties;
    }

    @Override
    public String getCommand() {
        return "/respack";
    }

    @Override
    public String getDescription() {
        return "посмотреть или установить ресурспак";
    }

    @Override
    public EnumSet<Role> roles() {
        return EnumSet.of(Role.ADMIN);
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        String[] args = message.getText().split("\\s+");
        if (args.length == 1)
            showResourcePackUrl(chatId);
        else
            setResourcePackUrl(chatId, args[1]);
    }

    private void showResourcePackUrl(long chatId) {
        String value = serverProperties.getProperty("resource-pack");
        if (value == null)
            telegram.sendMessage(chatId, "На сервере нет ресурспака!");
        else
            telegram.sendMessage(chatId, "Ссылка на ресурспак: " + value);
    }

    private void setResourcePackUrl(long chatId, String url) {
        try {
            String sha1 = getSha1FromUrl(url);
            serverProperties.setProperty("resource-pack-sha1", sha1);
        } catch (IOException e) {
            telegram.sendMessage(chatId, "Не удалось получить доступ к файлу!");
            return;
        }
        serverProperties.setProperty("resource-pack", url);
        telegram.sendMessage(chatId, "Изменения будут применены после перезагрузки сервера");
    }

    private String getSha1FromUrl(String url) throws IOException {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        InputStream in = connection.getInputStream();
        byte[] buffer = new byte[2048];
        int length;
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // should never happen
            e.printStackTrace();
            return null;
        }
        while ((length = in.read(buffer)) != -1) {
            sha1.update(buffer, 0, length);
        }
        in.close();
        return DatatypeConverter.printHexBinary(sha1.digest());
    }
}
