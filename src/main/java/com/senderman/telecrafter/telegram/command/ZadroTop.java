package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.AbsRatingCommandExecutor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public class ZadroTop extends AbsRatingCommandExecutor {

    private final TelegramProvider telegram;

    public ZadroTop(TelegramProvider telegram, MinecraftProvider minecraft) {
        super(minecraft, Statistic.PLAY_ONE_MINUTE);
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
        String top = getStatisticsAsString(10);
        telegram.sendMessageToMainChat("<b>Топ задротов:</b>\n\n" + top);
    }

    private String formatTicks(int ticks) {
        int totalSecs = ticks / 20;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%s:%s:%s", hours, minutes, seconds);
    }

    @Override
    protected String format(OfflinePlayer player) {
        return "\uD83D\uDC64 " + player.getName() + " (" + formatTicks(player.getStatistic(statistic)) + ")";
    }
}
