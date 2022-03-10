package com.senderman.telecrafter.telegram.command.abs;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbsRatingCommandExecutor implements CommandExecutor {

    protected final MinecraftProvider minecraft;
    protected final Statistic statistic;

    public AbsRatingCommandExecutor(MinecraftProvider minecraft, Statistic statistic) {
        this.minecraft = minecraft;
        this.statistic = statistic;
    }

    protected String getStatisticsAsString(int n) {
        return getStatistics(n)
                .stream()
                .map(this::format)
                .collect(Collectors.joining("\n"));
    }

    protected List<OfflinePlayer> getStatistics(int n) {
        return minecraft.getTopNPlayersByStatistic(n, statistic);
    }

    // How to format every line in the result
    protected abstract String format(OfflinePlayer player);
}
