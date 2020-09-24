package com.senderman.telecrafter.minecraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.senderman.telecrafter.InjectorConfig;
import com.senderman.telecrafter.minecraft.command.RestartCommand;
import com.senderman.telecrafter.minecraft.command.StopCommand;
import com.senderman.telecrafter.telegram.TelegramPolling;
import com.senderman.telecrafter.telegram.TelegramProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TelecrafterPlugin extends JavaPlugin {

    private TelegramPolling telegramPolling;
    private TelegramProvider telegram;

    @Override
    public void onEnable() {
        Injector injector;
        try {
            injector = Guice.createInjector(new InjectorConfig(this));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        telegramPolling = injector.getInstance(TelegramPolling.class);
        telegram = injector.getInstance(TelegramProvider.class);
        telegramPolling.startPolling();
        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);
        Objects.requireNonNull(getCommand("tstop")).setExecutor(injector.getInstance(StopCommand.class));
        Objects.requireNonNull(getCommand("trestart")).setExecutor(injector.getInstance(RestartCommand.class));

    }

    @Override
    public void onDisable() {
        telegramPolling.stopPolling();
        telegram.sendMessageToMainChat("⚠️ Внимание, обнаружена <b>возможная</b> остановка/перезагрузка сервера");
        telegram.sendMessageToMainChat("⭕️ Работа плагина завершена");
    }
}
