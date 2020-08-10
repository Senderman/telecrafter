package com.senderman.telecrafter

import com.google.inject.Inject
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileInputStream
import java.util.*

class Config @Inject constructor(plugin: JavaPlugin) {

    private val props = Properties()
    private val configFileName = "telecrafter.properties"

    val botToken: String = getProperty("bot.token")
    val botName: String = getProperty("bot.name")
    val chatId: Long = getProperty("bot.chatId").trim().toLong()

    init {
        val dataFile = File("${plugin.dataFolder.absolutePath}/$configFileName")
        if (!dataFile.exists())
            throw Exception("Create and fill in ${dataFile.absolutePath}!")

        FileInputStream(dataFile).use { props.load(it) }
    }

    private fun getProperty(key: String): String = props.getProperty(key)?.trim()
        ?: throw RuntimeException("You must define $key in $configFileName!")
}