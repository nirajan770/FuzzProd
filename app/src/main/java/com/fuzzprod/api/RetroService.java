package com.fuzzprod.api;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzzprod.util.Log;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by Nirajan on 10/20/2015.
 */
public class RetroService {
    private static final String TAG = "RetrofitServiceFactory";

    private static final String BASE_URL = "http://quizzes.fuzzstaging.com/";

    public static FuzzAPI createRetrofitService(Context context) {

        File cacheDir = new File(context.getCacheDir().getAbsolutePath(), "jsonCache");
        Log.i(TAG, "Cache Directory: " + context.getCacheDir().getAbsolutePath().toString());
        Cache cache = null;
        try {
            cache = new Cache(cacheDir, 10*1024*1024); // 10 MiB

        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        if (cache != null) {
            okHttpClient.setCache(cache);
        }

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new JacksonConverter(new ObjectMapper()))
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        final FuzzAPI fuzzAPI = restAdapter.create(FuzzAPI.class);
        return fuzzAPI;
    }
}
