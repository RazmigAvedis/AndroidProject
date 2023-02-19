package com.example.final_android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private double longitude;
    private double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("WeatherActivity","In weather activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        FloatingActionButton backButton = findViewById(R.id.floatingBackActionButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();

        longitude = bundle.getDouble("long");
        latitude= bundle.getDouble("lat");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenMeteoService service = retrofit.create(OpenMeteoService.class);

        Call<JsonObject> call = service.getWeatherData(
                latitude, // latitude
                longitude, // longitude
                true// exclude,
        );

        Log.i("WeatherActivity", "weatherData.toString()");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();// First, create a Gson object
                    Gson gson = new Gson();
// Next, extract the "current_weather" object from the JsonObject
                    JsonObject currentWeatherObject = jsonObject.getAsJsonObject("current_weather");

// Finally, cast the current_weather object into your class
                    WeatherData weatherData = gson.fromJson(currentWeatherObject, WeatherData.class);

                    Log.i("WeatherActivity",currentWeatherObject.toString());
                    TextView timeText = findViewById(R.id.timeTextView);
                    TextView weatherCodeText = findViewById(R.id.weatherCodeTextView);
                    TextView windSpeedText = findViewById(R.id.windspeedTextView);
                    TextView windDirectionText = findViewById(R.id.winddirectionTextView);
                    TextView temperatureText = findViewById(R.id.temperatureTextView);

                    timeText.setText(String.valueOf(weatherData.datetime));
                    weatherCodeText.setText(String.valueOf(weatherData.weathercode));
                    windSpeedText.setText(String.valueOf(weatherData.windspeed));
                    temperatureText.setText(String.valueOf(weatherData.temperature));
                    windDirectionText.setText(String.valueOf(weatherData.winddirection));

                    Log.i("WeatherActivity", weatherData.toString());
                    // parse the JSON response and update your UI
                } else {
                    Log.i("WeatherActivity","Fail!");
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("WeatherActivity","Faill");
                // handle error
            }
        });
    }
}