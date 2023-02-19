package com.example.final_android_project;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenMeteoService {
    @GET("onecall")
    Call<JsonObject> getWeatherData(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("exclude") String exclude,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
