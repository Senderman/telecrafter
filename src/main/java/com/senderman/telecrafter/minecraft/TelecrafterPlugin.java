package com.senderman.telecrafter.minecraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.senderman.telecrafter.InjectorConfig;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

public class TelecrafterPlugin extends JavaPlugin {

    private BotSession botSession;
    private TelecrafterBot telegram;

    @Override
    public void onEnable() {
        ApiContextInitializer.init();
        Injector injector = Guice.createInjector(new InjectorConfig(this));
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            telegram = injector.getInstance(TelecrafterBot.class);
            botSession = botsApi.registerBot(telegram);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            return;
        }
        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);
    }

    @Override
    public void onDisable() {
        telegram.sendMessage("⚠️ Внимание, обнаружена <b>возможная</b> остановка/перезагрузка сервера");
        telegram.sendMessage("\uD83D\uDD04 Завершение работы плагина Telecrafter...");
        botSession.stop();
        while (botSession.isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        telegram.sendMessage("⭕️ Работа плагина завершена");
    }
}
