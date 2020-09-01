package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

public class ServerStopDelayer {

    private final Plugin plugin;

    @Inject
    public ServerStopDelayer(Plugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleServerStop(String command) {
        ServerStopAction action;
        String willBe;
        switch (command.split("\\s+", 2)[0]) {
            case "stop":
                action = ServerStopAction.STOP;
                willBe = "остановлен";
                break;
            case "reload":
                action = ServerStopAction.RELOAD;
                willBe = "перезагружен";
                break;
            default:
                return;
        }
        Server server = plugin.getServer();
        server.broadcastMessage("§4 ВНИМАНИЕ! Сервер будет " + willBe + " через 30 секунд!");
        server.getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> server.dispatchCommand(server.getConsoleSender(), action.getCommand()),
                600);
    }

    private enum ServerStopAction {

        STOP("stop"), RELOAD("reload");

        private final String command;

        ServerStopAction(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

}
