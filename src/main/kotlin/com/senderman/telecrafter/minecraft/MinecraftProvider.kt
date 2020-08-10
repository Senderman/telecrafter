package com.senderman.telecrafter.minecraft

import com.google.inject.Inject
import org.bukkit.attribute.Attribute
import org.bukkit.plugin.java.JavaPlugin

class MinecraftProvider @Inject constructor(
    private val plugin: JavaPlugin
) {

    fun sendMessage(message: String) {
        plugin.server.broadcastMessage(message)
    }

    fun getOnlinePlayersNames(): String = plugin
        .server
        .onlinePlayers.joinToString("\n") {
            String.format(
                "\uD83D\uDC64 %s (%d/%d ❤)",
                it.name,
                it.health.toInt(),
                it.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value.toInt()
            )
        }
}