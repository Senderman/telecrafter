package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import com.senderman.telecrafter.telegram.TelegramProvider;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class EventListener implements Listener {

    private final TelegramProvider telegram;

    @Inject
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
    void onDeath(PlayerDeathEvent event) {
        String message = (String) ObjectUtils.defaultIfNull(
                event.getDeathMessage(),
                event.getEntity().getName() + "умер"
        );
        telegram.sendMessageToMainChat("☠️ " + message);
    }

    @EventHandler
    void onPlayerMessage(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String name = event.getPlayer().getName();
        telegram.sendMessageToMainChat(String.format("\uD83D\uDCAC <b>[%s]</b>: %s", name, message));
    }

    @EventHandler
    void onMobDeath(EntityDeathEvent event) {
        switch (event.getEntityType()) {
            case ENDER_DRAGON:
                telegram.sendMessageToMainChat("\uD83C\uDFC6 Дракон Края побежден!");
                break;
            case WITHER:
                telegram.sendMessageToMainChat("\uD83D\uDE31 Иссушитель побежден!");
                break;
            case ELDER_GUARDIAN:
                telegram.sendMessageToMainChat("\uD83D\uDC21 Древний страж побежден!");
                break;
        }
    }

    @EventHandler
    void onMobSpawn(CreatureSpawnEvent event) {
        switch (event.getEntity().getType()) {
            case ENDER_DRAGON:
                telegram.sendMessageToMainChat("\uD83D\uDE0E Сейчас будет сражение с Драконом Края!");
                break;
            case WITHER:
                telegram.sendMessageToMainChat("\uD83D\uDE31 Сейчас будет сражение с Иссушителем!");
                break;
        }
    }

    @EventHandler
    void onServerMessage(BroadcastMessageEvent event) {
        String message = event.getMessage();
        telegram.sendMessageToMainChat("\uD83D\uDCAC " + message);
    }

    @EventHandler
    void onServerLoad(ServerLoadEvent event) {
        telegram.sendMessageToMainChat("✅ Сервер запущен!");
    }

}
