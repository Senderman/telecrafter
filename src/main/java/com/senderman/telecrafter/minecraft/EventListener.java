package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import com.senderman.telecrafter.telegram.TelegramChat;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class EventListener implements Listener {

    private final TelegramChat telegram;

    @Inject
    public EventListener(TelegramChat telegram) {
        this.telegram = telegram;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        telegram.sendMessage("⛏ <b>" + playerName + " зашел на сервер!</b>");
    }

    @EventHandler
    void onLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        telegram.sendMessage("⛏ <b>" + playerName + " ушел с сервера!</b>");
    }

    @EventHandler
    void onDeath(PlayerDeathEvent event) {
        String message = (String) ObjectUtils.defaultIfNull(
                event.getDeathMessage(),
                event.getEntity().getName() + "умер"
        );
        telegram.sendMessage("⛏ ☠️ " + message);
    }

    @EventHandler
    void onPlayerMessage(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String name = event.getPlayer().getName();
        telegram.sendMessage(String.format("⛏ \uD83D\uDCAC <b>[%s]</b>: %s", name, message));
    }

    @EventHandler
    void onMobDeath(EntityDeathEvent event) {
        switch (event.getEntityType()) {
            case ENDER_DRAGON:
                telegram.sendMessage("⛏ \uD83C\uDFC6 Дракон Края побежден!");
                break;
            case WITHER:
                telegram.sendMessage("⛏ \uD83D\uDE31 Иссушитель побежден!");
                break;
            case ELDER_GUARDIAN:
                telegram.sendMessage("⛏ \uD83D\uDC21 Древний страж побежден!");
                break;
        }
    }

    @EventHandler
    void onServerMessage(BroadcastMessageEvent event) {
        String message = event.getMessage();
        telegram.sendMessage("⛏ \uD83D\uDCAC [Сервер]: " + message);
    }

    @EventHandler
    void onServerLoad(ServerLoadEvent event) {
        telegram.sendMessage("⛏ ✅ Сервер запущен!");
    }

    @EventHandler
    void onChristmasBlocks(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            Player player = event.getPlayer();
            player.playSound(player.getLocation(), "jbells", 0.5f, 1f);
        }
    }
}
