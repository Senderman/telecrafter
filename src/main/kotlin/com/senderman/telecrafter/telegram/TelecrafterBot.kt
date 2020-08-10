package com.senderman.telecrafter.telegram

import com.google.inject.Inject
import com.senderman.telecrafter.Config
import com.senderman.telecrafter.minecraft.MinecraftProvider
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

class TelecrafterBot @Inject constructor(
    private val config: Config,
    private val minecraft: MinecraftProvider
) : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {

        val message = update.message ?: return
        if (!message.isCommand) return
        if (message.chatId != config.chatId) return

        val text = message.text?.trim() ?: return

        val command = text.split("\\s+".toRegex(), 2)[0]
            .toLowerCase(Locale.ENGLISH)
            .replace("@$botUsername", "")
        if ("@" in command) return

        when (command) {
            "/mchat" -> minecraft.sendMessage("[TG] [${message.from.firstName}]: ${text.split(Regex("\\s+"), 2)[1]}")
            "/mnow" -> execute(
                SendMessage(config.chatId, "<b>Игроки на сервере:</b>\n\n${minecraft.getOnlinePlayersNames()}")
                    .enableHtml(true)
            )

        }


    }

    override fun getBotUsername(): String = config.botName

    override fun getBotToken(): String = config.botToken
}