package com.example.final_android_project.ui.Weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_android_project.AppConstants;
import com.example.final_android_project.LocationModel;
import com.example.final_android_project.OpenMeteoService;
import com.example.final_android_project.WeatherData;
import com.example.final_android_project.WeatherDatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<WeatherData> weatherData;
    private WeatherDatabaseHelper mWeatherDBHelper;

    private MutableLiveData<List<LocationModel>> locationData;
    public WeatherViewModel() {
        weatherData = new MutableLiveData<>();
        locationData= new MutableLiveData<>();
        locationData.setValue(new ArrayList<>());
//       loadLocationData(context);
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }
    public LiveData<List<LocationModel>> getListOfLocationMode(){return  locationData;}

    public void loadLocationData(Context context){
        List<LocationModel> locationModelList = new ArrayList<>();
        String fileName = context.getPackageName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString(AppConstants.USER_ID_KEY, null);
        mWeatherDBHelper = new WeatherDatabaseHelper(context,userID);
        Cursor cursor = mWeatherDBHelper.getDataOfUser();

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("ID");
            int longitudeIndex = cursor.getColumnIndex("longitude");
            int latitudeIndex = cursor.getColumnIndex("latitude");
            int useridIndex = cursor.getColumnIndex("userid");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (idIndex >= 0 && longitudeIndex >= 0 && latitudeIndex >= 0 && useridIndex >= 0 && descriptionIndex >= 0) {
                LocationModel location = new LocationModel();
                location.id = cursor.getString(idIndex);
                location.longitude = cursor.getDouble(longitudeIndex);
                location.latitude = cursor.getDouble(latitudeIndex);
                location.userid = cursor.getString(useridIndex);
                location.description = cursor.getString(descriptionIndex);
                locationModelList.add(location);
                locationData.setValue(locationModelList);
            }
        }
        cursor.close();
    }


    public LocationModel getLocationData(String id){
        Cursor cursor = mWeatherDBHelper.getItem(id);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("ID");
            int longitudeIndex = cursor.getColumnIndex("longitude");
            int latitudeIndex = cursor.getColumnIndex("latitude");
            int useridIndex = cursor.getColumnIndex("userid");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (idIndex >= 0 && longitudeIndex >= 0 && latitudeIndex >= 0 && useridIndex >= 0 && descriptionIndex >= 0) {
                LocationModel location = new LocationModel();
                location.id = cursor.getString(idIndex);
                location.longitude = cursor.getDouble(longitudeIndex);
                location.latitude = cursor.getDouble(latitudeIndex);
                location.userid = cursor.getString(useridIndex);
                location.description = cursor.getString(descriptionIndex);
                return location;
            }
        }
        cursor.close();
        return null;
    }

    public void deleteLocation(String id){
        mWeatherDBHelper.deleteWeather(id);
    }

    public void initWeatherData(double latitude, double longitude) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // Set connection timeout to 10 seconds
                .readTimeout(1, TimeUnit.SECONDS) // Set read timeout to 1 second
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        OpenMeteoService service = retrofit.create(OpenMeteoService.class);

        TimeZone timeZone = TimeZone.getDefault();
        String timeZoneName = timeZone.getID();
        Log.d("Time Zone", timeZoneName);

        Call<JsonObject> call = service.getWeatherData(
                latitude, // latitude
                longitude, // longitude
                true,// exclude,
                timeZoneName,
                "temperature_2m_max",
                "temperature_2m_min"
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.i("hello", response.body().toString());
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    JsonObject currentWeatherObject = jsonObject.getAsJsonObject("current_weather");
                    JsonObject dailyObject = jsonObject.getAsJsonObject("daily");
                    WeatherData weather_data = gson.fromJson(currentWeatherObject, WeatherData.class);


                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        Date date = inputFormat.parse(weather_data.datetime);
                        String formattedDate = outputFormat.format(date);
                        weather_data.datetime= formattedDate;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    WeatherData.DailyData dailyData = gson.fromJson(dailyObject, WeatherData.DailyData.class);
                    weather_data.setDailyData(dailyData);
                    weatherData.setValue(weather_data);

                } else {
                    // handle error

                    Log.i("WeatherViewMode", "request Failed: "+ response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // handle error
                Log.e("WeatherViewModel","Error Has Occured" + t.toString()+ " Retrying ... ");    // Retry the request after a delay
                Callback<JsonObject> callback = this;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call.clone().enqueue(callback);
                    }
                }, 1000); // Retry after 5 seconds
            }
        });
    }

}