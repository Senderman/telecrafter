package com.senderman.telecrafter

import com.google.inject.Inject
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileInputStream
import java.util.*

class Config @Inject constructor(plugin: JavaPlugin) {

    private val props = Properties()
    private val configFileName = "telecrafter.properties"

    init {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists())
            throw Exception("Create ${dataFolder.absolutePath}/$configFileName")

        FileInputStream(File("${dataFolder.absolutePath}/$configFileName")).use { props.load(it) }
    }

    val botToken: String
        get() = props.getProperty("bot.token")
            ?: throw Exception("You must define bot.token in $configFileName!")

    val botName: String
        get() = props.getProperty("bot.name")
            ?: throw Exception("You must define bot.name in $configFileName!")

    val chatId: Long
        get() = props.getProperty("bot.chatId")?.trim()?.toLong()
            ?: throw Exception("You must define bot.chatId in $configFileName!")
}