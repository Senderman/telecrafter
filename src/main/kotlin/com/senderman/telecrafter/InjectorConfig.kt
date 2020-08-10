package com.senderman.telecrafter

import com.google.inject.AbstractModule
import com.senderman.telecrafter.minecraft.EventListener
import com.senderman.telecrafter.minecraft.MinecraftProvider
import com.senderman.telecrafter.telegram.TelecrafterBot
import com.senderman.telecrafter.telegram.TelegramChat
import org.bukkit.plugin.java.JavaPlugin

class InjectorConfig(private val plugin: JavaPlugin) : AbstractModule() {

    override fun configure() {
        bind(Config::class.java)
        bind(JavaPlugin::class.java).toInstance(plugin)
        bind(EventListener::class.java)
        bind(MinecraftProvider::class.java)
        bind(TelecrafterBot::class.java)
        bind(TelegramChat::class.java)
    }
}