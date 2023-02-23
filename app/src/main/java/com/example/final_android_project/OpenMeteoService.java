package com.example.final_android_project;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenMeteoService {
    @GET("forecast")
    Call<JsonObject> getWeatherData(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current_weather") boolean currentWeather,
            @Query("timezone") String timezone,
            @Query("daily") String dailytemp2mmax,
            @Query("daily") String dailytemp2mmin
    );
}
