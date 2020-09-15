package com.senderman.telecrafter.telegram;

import com.google.inject.Inject;
import com.senderman.telecrafter.telegram.api.TelegramApiWrapper;
import com.senderman.telecrafter.telegram.api.entity.Update;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TelegramPolling {

    private final AtomicBoolean doPolling;
    private final TelegramApiWrapper api;
    private final TelecrafterBot bot;

    @Inject
    public TelegramPolling(TelegramApiWrapper apiWrapper, TelecrafterBot bot) {
        doPolling = new AtomicBoolean(false);
        this.api = apiWrapper;
        this.bot = bot;
    }

    public void startPolling() {
        if (doPolling.get()) return;
        doPolling.set(true);
        new Thread(this::polling).start();

    }

    public void stopPolling() {
        doPolling.set(false);
    }

    private void polling() {
        int lastUpdateId = -1;

        while (doPolling.get()) {
            try {
                List<Update> updates = api.getUpdates(lastUpdateId == -1 ? null : lastUpdateId + 1);
                lastUpdateId = updates.isEmpty() ? lastUpdateId : updates.get(updates.size() - 1).getUpdateId();
                for (Update update : updates)
                    bot.onUpdateReceived(update);

                Thread.sleep(500);
            } catch (InterruptedException e) {
                doPolling.set(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
