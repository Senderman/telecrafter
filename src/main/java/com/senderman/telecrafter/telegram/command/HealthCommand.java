package com.senderman.telecrafter.telegram.command;

import com.senderman.telecrafter.telegram.TelegramProvider;
import com.senderman.telecrafter.telegram.api.entity.Message;
import com.senderman.telecrafter.telegram.command.abs.CommandExecutor;

import java.lang.management.ManagementFactory;

public class HealthCommand implements CommandExecutor {

    private final TelegramProvider telegram;

    public HealthCommand(TelegramProvider telegram) {
        this.telegram = telegram;
    }

    @Override
    public String getCommand() {
        return "/health";
    }

    @Override
    public String getDescription() {
        return "посмотреть нагрузку на сервер";
    }

    @Override
    public void execute(Message message) {
        String response = formatHealth();
        telegram.sendMessage(message.getChatId(), response);
    }

    private String formatHealth() {
        var r = Runtime.getRuntime();
        double delimiter = 1048576f;
        return String.format(
                "🖥 <b>Нагрузка:</b>\n\n" +

                        "Занято: <code>%.2f MiB</code>\n" +
                        "Свободно: <code>%.2f MiB</code>\n" +
                        "Выделено JVM: <code>%.2f MiB</code>\n" +
                        "Доступно JVM: <code>%.2f MiB</code>\n" +
                        "Аптайм: <code>%d min</code>\n" +
                        "Потоки: <code>%d</code>\n" +
                        "CPUs: <code>%d</code>\n",
                (r.totalMemory() - r.freeMemory()) / delimiter,
                r.freeMemory() / delimiter,
                r.totalMemory() / delimiter,
                r.maxMemory() / delimiter,
                ManagementFactory.getRuntimeMXBean().getUptime() / 60000,
                ManagementFactory.getThreadMXBean().getThreadCount(),
                ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()
        );
    }

}
