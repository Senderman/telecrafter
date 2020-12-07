package com.senderman.telecrafter.minecraft;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayersInfo {

    private final Collection<? extends Player> onlinePlayers;
    private final OfflinePlayer[] offlinePlayers;

    public PlayersInfo(Collection<? extends Player> onlinePlayers, OfflinePlayer[] offlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        this.offlinePlayers = offlinePlayers;
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    public OfflinePlayer[] getOfflinePlayers() {
        return offlinePlayers;
    }

    public int getOnlinePlayersCount() {
        return onlinePlayers.size();
    }

    public int getOfflinePlayersCount() {
        return offlinePlayers.length;
    }
}
