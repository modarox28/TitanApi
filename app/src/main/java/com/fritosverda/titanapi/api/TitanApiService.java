package com.fritosverda.titanapi.api;

import com.fritosverda.titanapi.model.RespuestaPersonajes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TitanApiService {

    @GET("characters")
    Call<RespuestaPersonajes> obtenerPersonajes(@Query("page") int pagina);
}
