package com.example.final_android_project;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("time")
    public String datetime;

    @SerializedName("temperature")
    public double temperature;

    @SerializedName("weathercode")
    public int weathercode;

    @SerializedName("windspeed")
    public double windspeed;

    @SerializedName("winddirection")
    public double winddirection;

    // getters and setters
}
