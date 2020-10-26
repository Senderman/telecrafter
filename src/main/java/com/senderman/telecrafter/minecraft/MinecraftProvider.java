package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MinecraftProvider {

    private final Plugin plugin;


    @Inject
    public MinecraftProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String message) {
        plugin.getServer().broadcastMessage(message);
    }

    public String getOnlinePlayersNames() {
        StringBuilder result = new StringBuilder();
        Map<World, ? extends List<? extends Player>> envPlayersMap = plugin
                .getServer()
                .getOnlinePlayers()
                .stream()
                .collect(Collectors.groupingBy(Entity::getWorld));
        for (World world : envPlayersMap.keySet()) {
            String formattedWorld = getEnvironmentEmoji(world.getEnvironment())
                    + " <b>" + world.getName() + ":</b>\n";
            result.append(formattedWorld);

            for (Player player : envPlayersMap.get(world)) {
                String formattedPlayer = String.format(
                        "\uD83D\uDC64 %s (%d/%d ❤)\n",
                        player.getName(),
                        (int) player.getHealth(),
                        (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()
                );
                result.append(formattedPlayer);
            }
            result.append("\n");
        }
        return result.toString();
    }

    public void runCommand(String command, @Nullable Consumer<Boolean> callback) {
        Server server = plugin.getServer();
        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            boolean result = server.dispatchCommand(server.getConsoleSender(), command);
            if (callback != null) {
                callback.accept(result);
            }
        });
    }

    /**
     * Очистить все миры от дропа
     * @param callback Consumer принимающий long - кол-во удаленных предметов
     */
    public void removeAndCountDrop(@Nullable Consumer<Long> callback) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            long result = plugin.getServer().getWorlds().stream()
                    .flatMap(w -> w.getEntitiesByClass(Item.class).stream())
                    .peek(Entity::remove)
                    .count();
            if (callback != null)
                callback.accept(result);
        });
    }

    public File getLogsDirectory() {
        File logsDir = new File(plugin.getDataFolder().getParentFile().getParentFile(), "logs");
        if (!logsDir.exists())
            logsDir.mkdirs();
        return logsDir;
    }

    private String getEnvironmentEmoji(Environment environment) {
        String result = "Unknown";
        switch (environment) {
            case NORMAL:
                result = "\uD83C\uDF33";
                break;
            case NETHER:
                result = "\uD83D\uDD25";
                break;
            case THE_END:
                result = "\uD83D\uDD32";
                break;
        }
        return result;
    }
}
