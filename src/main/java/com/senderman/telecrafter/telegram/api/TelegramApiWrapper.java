package com.senderman.telecrafter.telegram.api;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import com.senderman.telecrafter.telegram.api.entity.File;
import com.senderman.telecrafter.telegram.api.entity.Message;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelegramApiWrapper {

    private final TelegramApi telegramApi;


    @Inject
    public TelegramApiWrapper(Config config) {
        this.telegramApi = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("https://api.telegram.org/bot" + config.getBotToken() + "/")
                .build()
                .create(TelegramApi.class);
    }


    public List<Update> getUpdates(Integer offset) {
        try {
            return Optional.ofNullable(telegramApi.getUpdates(offset).execute().body())
                    .map(Result::getResult)
                    .orElse(new ArrayList<>());
        } catch (IOException e) {
            return null;
        }
    }

    public File getFile(String fileId) {
        try {
            return telegramApi.getFile(fileId).execute().body().getResult();
        } catch (IOException e) {
            return null;
        }
    }

    public void sendMessageAsync(long chatId, String text) {
        telegramApi.sendMessage(chatId, text, null, "HTML").enqueue(new Callback<Result<Message>>() {
            @Override
            public void onResponse(Call<Result<Message>> call, Response<Result<Message>> response) {
            }

            @Override
            public void onFailure(Call<Result<Message>> call, Throwable t) {

            }
        });
    }

    public Message sendMessage(long chatId, String text) {
        return sendMessage(chatId, text, null);
    }

    public Message sendMessage(long chatId, String text, Integer replyToMessageId) {
        return sendMessage(chatId, text, replyToMessageId, "HTML");
    }


    public Message sendMessage(long chatId, String text, Integer replyToMessageId, String parseMode) {
        try {
            return telegramApi.sendMessage(chatId, text, replyToMessageId, parseMode).execute().body().getResult();
        } catch (IOException e) {
            return null;
        }
    }

    public Message sendDocument(long chatId, java.io.File file) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part document = MultipartBody.Part.createFormData("document", file.getName(), body);
        try {
            return telegramApi.sendDocument(chatId, document).execute().body().getResult();
        } catch (IOException e) {
            return null;
        }
    }
}
