package com.senderman.telecrafter.minecraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.senderman.telecrafter.InjectorConfig;
import com.senderman.telecrafter.telegram.TelegramChat;
import com.senderman.telecrafter.telegram.TelegramPolling;
import org.bukkit.plugin.java.JavaPlugin;

public class TelecrafterPlugin extends JavaPlugin {

    private TelegramPolling telegramPolling;
    private TelegramChat telegram;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new InjectorConfig(this));
        telegramPolling = injector.getInstance(TelegramPolling.class);
        telegram = injector.getInstance(TelegramChat.class);
        telegramPolling.startPolling();
        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);
    }

    @Override
    public void onDisable() {
        telegramPolling.stopPolling();
        telegram.sendMessage("⚠️ Внимание, обнаружена <b>возможная</b> остановка/перезагрузка сервера");
        telegram.sendMessage("⭕️ Работа плагина завершена");
    }
}
