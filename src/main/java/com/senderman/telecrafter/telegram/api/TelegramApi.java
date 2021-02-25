package com.senderman.telecrafter.telegram.api;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.telegram.api.entity.Result;
import com.senderman.telecrafter.telegram.api.entity.Update;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class TelegramApi {

    private final TelegramService telegramService;


    @Inject
    public TelegramApi(Config config) {
        this.telegramService = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("https://api.telegram.org/bot" + config.getBotToken() + "/")
                .build()
                .create(TelegramService.class);
    }


    public void getUpdates(Integer offset, Callback<Result<List<Update>>> callback) {
        telegramService.getUpdates(offset).enqueue(callback);
    }

    public void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null, "HTML");
    }

    public void sendMessage(long chatId, String text, Integer replyToMessageId) {
        sendMessage(chatId, text, replyToMessageId, "HTML");
    }


    public void sendMessage(long chatId, String text, Integer replyToMessageId, String parseMode) {
        telegramService.sendMessage(chatId, text, replyToMessageId, parseMode).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void sendDocument(long chatId, java.io.File file) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part document = MultipartBody.Part.createFormData("document", file.getName(), body);
        telegramService.sendDocument(chatId, document).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
