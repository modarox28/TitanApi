package com.fritosverda.titanapi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.attackontitanapi.com/";

    private static TitanApiService servicio;

    public static TitanApiService getServicio() {
        if (servicio == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicio = retrofit.create(TitanApiService.class);
        }
        return servicio;
    }
}
