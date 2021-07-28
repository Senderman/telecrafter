package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.MinecraftProvider;
import com.senderman.telecrafter.minecraft.PlayersInfo;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.stream.Collectors;

@Singleton
public class ZadroTop implements CommandExecutor {

    private final MinecraftProvider minecraft;
    private final TelegramProvider telegram;

    @Inject
    public ZadroTop(MinecraftProvider minecraft, TelegramProvider telegram) {
        this.minecraft = minecraft;
        this.telegram = telegram;
    }

    @Override
    public String getCommand() {
        return "/zadrotop";
    }

    @Override
    public String getDescription() {
        return "топ задротов сервера";
    }

    @Override
    public void execute(Message message) {

        PlayersInfo info = minecraft.getOnlineInfo();
        String top = Arrays.stream(info.getOfflinePlayers())
                .sorted((p1, p2) -> Integer.compare(getTickPlayer(p2), getTickPlayer(p1)))
                .limit(10)
                .map(p -> "\uD83D\uDC64 " + p.getName() + " (" + formatTicks(p.getStatistic(Statistic.PLAY_ONE_MINUTE)) + ")")
                .collect(Collectors.joining("\n"));

        telegram.sendMessageToMainChat("<b>Топ задротов:</b>\n\n" + top);
    }

    private int getTickPlayer(OfflinePlayer player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    private String formatTicks(int ticks) {
        int totalSecs = ticks / 20;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%s:%s:%s", hours, minutes, seconds);
    }
}
