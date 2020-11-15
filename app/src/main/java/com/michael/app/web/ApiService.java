package com.michael.app.web;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/** Интерфейс для описания API серверной части. */
public interface ApiService {
    @SuppressWarnings("rawtypes")
    @POST("items")
    Call<ResponseBody> addItem(@Body HashMap hashMap);

    @GET("get")
    Call<ResponseBody> getEvents(@QueryMap Map<String, String> options);
}
