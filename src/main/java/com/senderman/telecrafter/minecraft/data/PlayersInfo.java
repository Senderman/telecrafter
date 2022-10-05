package com.senderman.telecrafter.minecraft.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public record PlayersInfo(Collection<? extends Player> onlinePlayers, OfflinePlayer[] offlinePlayers) {

    public int getOnlinePlayersCount() {
        return onlinePlayers.size();
    }

    public int getOfflinePlayersCount() {
        return offlinePlayers.length;
    }
}
