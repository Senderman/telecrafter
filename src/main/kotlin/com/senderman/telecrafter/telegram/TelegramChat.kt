package com.senderman.telecrafter.telegram

import com.google.inject.Inject
import com.senderman.telecrafter.Config
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class TelegramChat @Inject constructor(
    config: Config,
    private val bot: TelecrafterBot
) {
    private val chatId: Long = config.chatId

    fun sendMessage(message: String) {
        bot.execute(SendMessage(chatId, message).enableHtml(true))
    }
}