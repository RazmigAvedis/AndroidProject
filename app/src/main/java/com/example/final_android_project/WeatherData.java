package com.example.final_android_project;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("datetime")
    private String datetime;

    @SerializedName("temp")
    private double temperature;

    @SerializedName("rh")
    private int humidity;

    // getters and setters
}
