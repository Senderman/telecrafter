package com.senderman.telecrafter.minecraft

import com.google.inject.Inject
import com.senderman.telecrafter.telegram.TelegramChat
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Animals
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PigZombieAngerEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.event.server.BroadcastMessageEvent
import org.bukkit.event.server.ServerLoadEvent

class EventListener @Inject constructor(private val telegram: TelegramChat) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val playerName = event.player.name
        telegram.sendMessage("⛏ <b>$playerName зашел на сервер!</b>")
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val playerName = event.player.name
        telegram.sendMessage("⛏ <b>$playerName ушел с сервера!</b>")
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        telegram.sendMessage("⛏ ☠️" + (event.deathMessage ?: "${event.entity.name} умер"))
    }

    @EventHandler
    fun onPlayerMessage(event: AsyncPlayerChatEvent) {
        val message = event.message
        val playerName = event.player.name
        telegram.sendMessage("⛏ \uD83D\uDCAC <b>[$playerName]</b>: $message")
    }

    @EventHandler
    fun onBossDeath(event: EntityDeathEvent): Unit = when (event.entityType) {
        EntityType.ENDER_DRAGON -> telegram.sendMessage("⛏ \uD83C\uDFC6 Дракон Края побежден!")
        EntityType.WITHER -> telegram.sendMessage("⛏ \uD83D\uDE31 Иссушитель побежден!")
        EntityType.ELDER_GUARDIAN -> telegram.sendMessage("⛏ \uD83D\uDC21 Древний страж побежден!")
        else -> { // Do nothing
        }
    }

    @EventHandler
    fun onServerMessage(event: BroadcastMessageEvent) {
        val message = event.message
        telegram.sendMessage("⛏ \uD83D\uDCAC [Сервер]: $message")
    }

    @EventHandler
    fun onServerLoad(event: ServerLoadEvent) {
        telegram.sendMessage("⛏ ✅ Сервер запущен!")
    }
}