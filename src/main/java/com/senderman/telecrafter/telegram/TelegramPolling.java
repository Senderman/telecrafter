package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.telegram.api.TelegramApi;
import com.senderman.telecrafter.telegram.api.entity.Update;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TelegramPolling {

    private final TelegramApi api;
    private final TelecrafterBot bot;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> control;
    private int lastReceivedUpdateId;

    @Inject
    public TelegramPolling(TelegramApi apiWrapper, TelecrafterBot bot) {
        this.api = apiWrapper;
        this.bot = bot;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.lastReceivedUpdateId = 0;
    }

    public void startPolling() {
        if (control != null) return;
        control = executorService.scheduleAtFixedRate(this::polling, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void stopPolling() {
        control.cancel(true);
        control = null;
    }

    private void polling() {
        List<Update> updates = api.getUpdates(lastReceivedUpdateId + 1);
        if (updates.isEmpty()) return;

        lastReceivedUpdateId = updates
                .parallelStream()
                .map(Update::getUpdateId)
                .max(Integer::compareTo)
                .orElse(lastReceivedUpdateId);
        updates.forEach(bot::onUpdateReceived);
    }
}
