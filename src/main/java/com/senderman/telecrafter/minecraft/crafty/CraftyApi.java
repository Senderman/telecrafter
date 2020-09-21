package com.senderman.telecrafter.minecraft.crafty;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CraftyApi {

    @POST("server/restart")
    Call<Void> restart(@Query("token") String token, @Query("id") int serverId);

    @POST("server/stop")
    Call<Void> stop(@Query("token") String token, @Query("id") int serverId);

}
