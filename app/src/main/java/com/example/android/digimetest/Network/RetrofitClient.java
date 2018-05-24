package com.example.android.digimetest.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by melisa-pc on 23.05.2018.
 */

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

