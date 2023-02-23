package com.example.final_android_project;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    @SerializedName("daily")
    public DailyData dailyData;

    public class DailyData {
        @SerializedName("time")
        public List<String> times;

        @SerializedName("temperature_2m_max")
        public List<Double> maxTemperatures;

        @SerializedName("temperature_2m_min")
        public List<Double> minTemperatures;
    }

    // getters and setters
    public DailyData getDailyData() {
        return dailyData;
    }

    public void setDailyData(DailyData dailyData) {
        this.dailyData = dailyData;
    }

    public List<String> getDailyTimes() {
        return dailyData.times;
    }

    public List<Double> getDailyMaxTemperatures() {
        return dailyData.maxTemperatures;
    }

    public List<Double> getDailyMinTemperatures() {
        return dailyData.minTemperatures;
    }
}
