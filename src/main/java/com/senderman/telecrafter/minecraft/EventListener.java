package com.senderman.telecrafter.minecraft;

import com.senderman.telecrafter.telegram.TelegramProvider;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Optional;

public class EventListener implements Listener {

    private final TelegramProvider telegram;

    public EventListener(TelegramProvider telegram) {
        this.telegram = telegram;
    }


    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        telegram.sendMessageToMainChat("▶️ <b>" + playerName + " зашел на сервер!</b>");
    }

    @EventHandler
    void onLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        telegram.sendMessageToMainChat("◀️ <b>" + playerName + " ушел с сервера!</b>");
    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        String message = Optional.ofNullable(event.getDeathMessage())
                .orElseGet(() -> event.getEntity().getName() + "умер");
        telegram.sendMessageToMainChat("☠️ " + message);
    }

    @EventHandler
    void onPlayerMessage(AsyncChatEvent event) {
        var player = event.getPlayer();
        if (!player.hasPermission("telecrafter.send"))
            return;
        // this cast is safe as AsyncChatEvent contains TextComponent
        String message = ((TextComponent) event.message()).content();
        String name = player.getName();
        telegram.sendMessageToMainChat(String.format("\uD83D\uDCAC <b>[%s]</b>: %s", name, message));
    }

    @EventHandler
    void onEntityDeath(EntityDeathEvent event) {
        String text;
        switch (event.getEntityType()) {
            case ENDER_DRAGON:
                text = "\uD83C\uDFC6 Дракон Края побежден";
                break;
            case WITHER:
                text = "\uD83D\uDE31 Иссушитель побежден";
                break;
            case ELDER_GUARDIAN:
                text = "\uD83D\uDC21 Древний страж побежден";
                break;
            default:
                return;
        }
        var killer = event.getEntity().getKiller();
        if (killer == null) {
            text += "!";
        } else {
            text += " игроком " + killer.getName() + "!";
        }
        telegram.sendMessageToMainChat(text);

    }

    @EventHandler
    void onMobSpawn(CreatureSpawnEvent event) {
        switch (event.getEntity().getType()) {
            case ENDER_DRAGON -> telegram.sendMessageToMainChat("😎 Сейчас будет сражение с Драконом Края!");
            case WITHER -> telegram.sendMessageToMainChat("😱 Сейчас будет сражение с Иссушителем!");
        }
    }

    @EventHandler
    void onServerMessage(BroadcastMessageEvent event) {
        // this cast is safe as BroadcastMessageEvent contains TextComponent
        String message = ((TextComponent) event.message()).content();
        telegram.sendMessageToMainChat("💬 " + message);
    }

    @EventHandler
    void onServerLoad(ServerLoadEvent event) {
        telegram.sendMessageToMainChat("✅ Сервер запущен!");
    }

}
