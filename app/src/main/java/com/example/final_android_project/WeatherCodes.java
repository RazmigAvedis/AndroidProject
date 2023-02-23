package com.example.final_android_project;

public class WeatherCodes {

    public static class WeatherCode {
        public int[] codes;
        public String description;
        public int imagesrc;

        public WeatherCode(int[] codes, String description, int imagesrc) {
            this.codes = codes;
            this.description = description;
            this.imagesrc = imagesrc;
        }
    }

    private static final WeatherCode[] WEATHER_CODES = {
            new WeatherCode(new int[]{0}, "Clear sky", R.drawable.clear_sky),
            new WeatherCode(new int[]{1, 2, 3}, "Mainly clear, partly cloudy and overcast", R.drawable.mainly_clear),
            new WeatherCode(new int[]{45, 48}, "Fog and depositing rime fog", R.drawable.fog),
            new WeatherCode(new int[]{51, 53, 55}, "Drizzle: Light, moderate, and dense intensity", R.drawable.drizzle),
            new WeatherCode(new int[]{56, 57}, "Freezing Drizzle: Light and dense intensity", R.drawable.drizzle),
            new WeatherCode(new int[]{61, 63, 65}, "Rain: Slight, moderate and heavy intensity", R.drawable.rain),
            new WeatherCode(new int[]{66, 67}, "Freezing Rain: Light and heavy intensity", R.drawable.freezing_rain),
            new WeatherCode(new int[]{71, 73, 75}, "Snow fall: Slight, moderate, and heavy intensity", R.drawable.snow_fall),
            new WeatherCode(new int[]{77}, "Snow grains", R.drawable.snow_fall),
            new WeatherCode(new int[]{80, 81, 82}, "Rain showers: Slight, moderate, and violent", R.drawable.rain_showers),
            new WeatherCode(new int[]{85, 86}, "Snow showers slight and heavy", R.drawable.snow_fall),
            new WeatherCode(new int[]{95}, "Thunderstorm: Slight or moderate", R.drawable.thunderstorm),
            new WeatherCode(new int[]{96, 99}, "Thunderstorm with slight and heavy hail", R.drawable.thunderstorm)
    };

    public static WeatherCode[] getWeatherCodes() {
        return WEATHER_CODES;
    }
}
