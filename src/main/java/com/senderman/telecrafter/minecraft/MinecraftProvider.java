package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.World.Environment;
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
                        "\uD83D\uDC64 %s (%d/%d ❤) (Loc: %s)",
                        player.getName(),
                        (int) player.getHealth(),
                        (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(),
                        getEnvironmentName(player.getWorld().getEnvironment())
                )).collect(Collectors.joining("\n"));
    }

    public boolean runCommand(String command) {
        return plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
    }

    private String getEnvironmentName(Environment environment) {
        String result = "Unknown";
        switch (environment) {
            case NORMAL:
                result = "\uD83C\uDF33 Overworld";
                break;
            case NETHER:
                result = "\uD83D\uDD25 Nether";
                break;
            case THE_END:
                result = "\uD83D\uDC7D Ender world";
                break;
        }
        return result;
    }
}
