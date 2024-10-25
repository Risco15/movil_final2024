package com.negocio.semana04comsumorest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionRest {
    public static final String URL = "http://192.168.18.3:8080/";
    public static Retrofit retrofit = null;

    public static Retrofit getConnecion() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()  // Permite que el JSON tenga un formato más flexible
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
