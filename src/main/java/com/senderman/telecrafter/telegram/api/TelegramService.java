package com.senderman.telecrafter.telegram.api;

import com.senderman.telecrafter.telegram.api.entity.Result;
import com.senderman.telecrafter.telegram.api.entity.Update;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TelegramService {

    @GET("getUpdates")
    Call<Result<List<Update>>> getUpdates(@Query("offset") Integer offset);

    @GET("sendMessage")
    Call<Void> sendMessage(
            @Query("chat_id") long chatId,
            @Query("text") String text,
            @Query("reply_to_message_id") Integer replyToMessageId,
            @Query("parse_mode") String parseMode
    );

    @Multipart
    @POST("sendDocument")
    Call<Void> sendDocument(
            @Query("chat_id") long chatId,
            @Part MultipartBody.Part document
    );

}
