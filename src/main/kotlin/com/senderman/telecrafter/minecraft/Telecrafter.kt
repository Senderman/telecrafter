package com.senderman.telecrafter.minecraft

import com.google.inject.Guice
import com.senderman.telecrafter.InjectorConfig
import com.senderman.telecrafter.telegram.TelecrafterBot
import org.bukkit.plugin.java.JavaPlugin
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi

class Telecrafter : JavaPlugin() {

    override fun onEnable() {
        ApiContextInitializer.init()
        val injectorConfig = InjectorConfig(this)
        val injector = Guice.createInjector(injectorConfig)
        val botsApi = TelegramBotsApi()
        botsApi.registerBot(injector.getInstance(TelecrafterBot::class.java))
        server.pluginManager.registerEvents(injector.getInstance(EventListener::class.java), this)
    }

}