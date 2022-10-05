package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.minecraft.data.PlayersInfo;
import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MineNow implements CommandExecutor {

    private final TelegramProvider telegram;
    private final MinecraftProvider minecraft;

    public MineNow(TelegramProvider telegram, MinecraftProvider minecraft) {
        this.telegram = telegram;
        this.minecraft = minecraft;
    }

    @Override
    public String getCommand() {
        return "/mnow";
    }

    @Override
    public String getDescription() {
        return "посмотреть текущий онлайн";
    }

    @Override
    public void execute(Message message) {
        StringBuilder text = new StringBuilder("<b>Игроки на сервере:</b> (");
        PlayersInfo info = minecraft.getOnlineInfo();
        text.append(info.getOnlinePlayersCount()).append("/").append(info.getOfflinePlayersCount()).append(")\n\n");

        List<World> worlds = info.onlinePlayers()
                .stream()
                .map(Entity::getWorld)
                .distinct()
                .toList();

        for (World world : worlds) {
            text.append(formatWorld(world)).append("\n");

            String playersInWorld = world.getPlayers().stream()
                    .map(this::formatPlayer)
                    .collect(Collectors.joining("\n"));

            text.append(playersInWorld).append("\n\n");
        }

        telegram.sendMessage(message.getChatId(), text.toString());
    }

    private String formatWorld(World world) {
        return getEnvironmentEmoji(world.getEnvironment())
                + " <b>" + world.getName() + ":</b>";
    }

    private String formatPlayer(Player player) {
        return String.format(
                "\uD83D\uDC64 %s (%d/%d ❤)",
                player.getName(),
                (int) player.getHealth(),
                (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()
        );
    }

    private String getEnvironmentEmoji(World.Environment environment) {
        return switch (environment) {
            case NORMAL -> "\uD83C\uDF33";
            case NETHER -> "\uD83D\uDD25";
            case THE_END -> "\uD83D\uDD32";
            case CUSTOM -> "\uD83D\uDCE6";
        };
    }
}
