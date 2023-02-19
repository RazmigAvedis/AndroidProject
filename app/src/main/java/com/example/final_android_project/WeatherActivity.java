package com.example.final_android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView timeText = findViewById(R.id.timeTextView);
    private TextView weatherCodeText = findViewById(R.id.weatherCodeTextView);
    private TextView windSpeedText = findViewById(R.id.windspeedTextView);
    private TextView windDirectionText = findViewById(R.id.winddirectionTextView);
    private TextView temperatureText = findViewById(R.id.temperatureTextView);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("WeatherActivity","In weather activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Button backButton = findViewById(R.id.floatingBackActionButton);
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
                true,// exclude,
                ""
        );

        Log.i("WeatherActivity", "weatherData.toString()");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    WeatherData weatherData = gson.fromJson(jsonObject, WeatherData.class);

                    timeText.setText(weatherData.datetime);
                    weatherCodeText.setText(weatherData.weathercode);
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