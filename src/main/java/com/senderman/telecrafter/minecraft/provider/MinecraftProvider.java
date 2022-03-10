package com.senderman.telecrafter.minecraft.provider;

import com.senderman.telecrafter.minecraft.data.PlayersInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinecraftProvider {

    private final Plugin plugin;

    public MinecraftProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(String message) {
        plugin.getServer().broadcast(Component.text(message), "telecrafter.receive");
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

    public List<OfflinePlayer> getTopNPlayersByStatistic(int n, Statistic statistic) {
        return Arrays.stream(plugin.getServer().getOfflinePlayers())
                .sorted((p1, p2) -> Integer.compare(p2.getStatistic(statistic), p1.getStatistic(statistic)))
                .limit(n)
                .collect(Collectors.toList());
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
