package com.senderman.telecrafter.minecraft.crafty;

import com.google.inject.Inject;
import com.senderman.telecrafter.Config;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class CraftyWrapper {

    private final CraftyApi craftyApi;
    private final String craftyToken;
    private final int craftyServerId;

    @Inject
    public CraftyWrapper(Config config) {
        this.craftyToken = config.getCraftyToken();
        this.craftyServerId = config.getCraftyServerId();
        Retrofit retrofit = new Retrofit.Builder()
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .baseUrl("https://127.0.0.1:" + config.getCraftyPort() + "/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.craftyApi = retrofit.create(CraftyApi.class);
    }

    public void restartServer() {
        try {
            Response<Void> r = craftyApi.restart(craftyToken, craftyServerId).execute();
            System.out.println(r.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            craftyApi.stop(craftyToken, craftyServerId).execute();
        } catch (IOException ignored) {
        }
    }

}
