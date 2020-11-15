package com.michael.app.web;

import android.util.Log;

import com.michael.app.MapsActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Класс для взаимодействия с сервером. */
public class WebClient {
    // https://it-hub-michael.herokuapp.com/api/get/?datetime_lte=10.01.2019%2000:00&datetime_gte=09.01.2019%2022:00
    /** Ссылка на сервер. **/
    private static final String URL = "https://it-hub-michael.herokuapp.com/api/";
    /** Интерфейс API сервера. */
    private ApiService apiService;
    /** Объект для запросов к серверу. */
    private Call<ResponseBody> serviceCall;

    public WebClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    /**
     * Метод добавления "Item".
     * @param webCallback коллбэк для возвращения данных
     * @param title наименование Item
     */
    public void addItem(WebCallback webCallback, String title) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        //noinspection rawtypes
        serviceCall = apiService.addItem((HashMap) params);
        sentData(webCallback);
    }

    /**
     * Метод получения "Profile".
     * @param webCallback коллбэк для возвращения данных
     */
    public void getEvents(WebCallback webCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("limit", "1000");
        params.put("datetime_lte", "10.08.2020 18:00");
        params.put("datetime_gte", "09.08.2020 00:00");
        //noinspection rawtypes
        serviceCall = apiService.getEvents((HashMap<String, String>) params);
        sentData(webCallback);
    }

    /**
     * Метод отправки запроса на сервер.
     * @param webCallback коллбэк для возвращения данных
     */
    private void sentData(final WebCallback webCallback) {
        serviceCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,
                                   @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            webCallback.onSuccess(response.body().string());
                        } else {
                            webCallback.onFailing("Web error: response is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        webCallback.onFailing(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                t.printStackTrace();
                webCallback.onFailing(t.getMessage());
            }
        });
    }
}
