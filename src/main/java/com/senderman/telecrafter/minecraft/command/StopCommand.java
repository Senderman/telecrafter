package com.senderman.telecrafter.minecraft.command;

import com.google.inject.Inject;
import com.senderman.telecrafter.minecraft.ServerStopAction;
import com.senderman.telecrafter.minecraft.ServerStopDelayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopCommand implements CommandExecutor {

    private final ServerStopDelayer delayer;

    @Inject
    public StopCommand(ServerStopDelayer delayer) {
        this.delayer = delayer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        delayer.scheduleServerStop(ServerStopAction.STOP);
        return true;
    }
}
