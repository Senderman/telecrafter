package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinecraftProvider {

    private final Plugin plugin;

    @Inject
    public MinecraftProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String message) {
        plugin.getServer().broadcastMessage(message);
    }

    public PlayersInfo getOnlineInfo() {
        Server server = plugin.getServer();
        return new PlayersInfo(server.getOnlinePlayers(), server.getOfflinePlayers());
    }

    public String getPlugins() {
        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
        return Stream.of(plugins)
                .map(p -> getPluginStatus(p) + p.getName() + " " + p.getDescription().getVersion())
                .collect(Collectors.joining("\n"));
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
     *
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

    private String getPluginStatus(Plugin plugin) {
        return plugin.isEnabled() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ";
    }
}
