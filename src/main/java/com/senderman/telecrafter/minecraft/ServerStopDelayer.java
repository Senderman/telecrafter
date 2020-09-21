package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.crafty.CraftyWrapper;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public class ServerStopDelayer {

    private final Plugin plugin;
    private final CraftyWrapper craftyWrapper;

    @Inject
    public ServerStopDelayer(Plugin plugin, CraftyWrapper craftyWrapper) {
        this.plugin = plugin;
        this.craftyWrapper = craftyWrapper;
    }

    public void scheduleServerStop(ServerStopAction action) {
        String willBe;
        Runnable whatToDo;
        switch (action) {
            case STOP:
                willBe = "остановлен";
                whatToDo = craftyWrapper::stopServer;
                break;
            case RELOAD:
                willBe = "перезагружен";
                whatToDo = craftyWrapper::restartServer;
                break;
            default:
                return;
        }
        Server server = plugin.getServer();
        server.broadcastMessage("§4 ВНИМАНИЕ! Сервер будет " + willBe + " через 30 секунд!");
        server.getScheduler().scheduleSyncDelayedTask(plugin, whatToDo, 600);
    }

}
