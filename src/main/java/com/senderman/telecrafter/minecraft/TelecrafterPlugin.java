package com.senderman.telecrafter.minecraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.senderman.telecrafter.InjectorConfig;
import com.senderman.telecrafter.telegram.TelecrafterBot;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class TelecrafterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ApiContextInitializer.init();
        InjectorConfig injectorConfig = new InjectorConfig(this);
        Injector injector = Guice.createInjector(injectorConfig);
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(injector.getInstance(TelecrafterBot.class));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            return;
        }
        getServer().getPluginManager().registerEvents(injector.getInstance(EventListener.class), this);


    }
}
