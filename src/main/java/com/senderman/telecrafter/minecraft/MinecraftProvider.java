package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.stream.Collectors;

public class MinecraftProvider {

    private final JavaPlugin plugin;

    @Inject
    public MinecraftProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String message) {
        plugin.getServer().broadcastMessage(message);
    }

    public String getOnlinePlayersNames() {
        return plugin
                .getServer()
                .getOnlinePlayers()
                .stream()
                .map(player -> String.format(
                        "\uD83D\uDC64 %s (%d/%d ❤)",
                        player.getName(),
                        (int) player.getHealth(),
                        (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()
                )).collect(Collectors.joining("\n"));
    }
}
