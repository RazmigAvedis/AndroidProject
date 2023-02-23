package com.example.final_android_project.ui.Weather;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_android_project.LocationActivity;
import com.example.final_android_project.LocationModel;
import com.example.final_android_project.R;
import com.example.final_android_project.WeatherCodes;
import com.example.final_android_project.databinding.FragmentWeatherBinding;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WeatherFragment extends Fragment {


    private double longitude = 10;
    private double latitude = 10;
    private FragmentWeatherBinding binding;
    public WeatherViewModel weatherViewModel;
    private static final String TAG = "Weather Fragment";

    public Spinner spinner;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }

    public void updateLocationList(){
        weatherViewModel.loadLocationData(getContext());
    }

    public void updateLongLat(double longitude, double latitude) {

        this.latitude = latitude;
        this.longitude = longitude;

        weatherViewModel.initWeatherData(latitude, longitude);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        Log.i(TAG, "In weather activity");

        binding = FragmentWeatherBinding.inflate(inflater, container, false);

        spinner = binding.spinnerWeatherLocations;

        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(),
                weatherData -> {
                    Log.i(TAG, weatherData.datetime);
                    binding.timeTextView.setText("Last Update: " + weatherData.datetime);

                    String weatherDescription = "";
                    int imageResource = 0;
                    for (WeatherCodes.WeatherCode weatherCode : WeatherCodes.getWeatherCodes()) {
                        for (int i : weatherCode.codes) {
                            if (i == weatherData.weathercode) {
                                weatherDescription = weatherCode.description;
                                imageResource = weatherCode.imagesrc;
                                break;
                            }
                        }
                    }

                    binding.weatherImageView.setImageResource(imageResource);
                    binding.weatherCodeTextView.setText(weatherDescription);
                    binding.windspeedTextView.setText("Wind Speed: " + weatherData.windspeed + " km/h");
                    binding.winddirectionTextView.setText("Wind Direction: " + weatherData.winddirection+" \u00B0");
                    binding.temperatureTextView.setText(String.valueOf(weatherData.temperature) + "\u00B0C");

                    List<String> times = weatherData.getDailyData().times;
                    List<Double> maxTemperatures = weatherData.getDailyData().maxTemperatures;
                    List<Double> minTemperatures = weatherData.getDailyData().minTemperatures;

                    binding.day1Card.dateTextView.setText(String.valueOf(times.get(0)));
                    binding.day2Card.dateTextView.setText(String.valueOf(times.get(1)));
                    binding.day3Card.dateTextView.setText(String.valueOf(times.get(2)));
                    binding.day4Card.dateTextView.setText(String.valueOf(times.get(3)));
                    binding.day5Card.dateTextView.setText(String.valueOf(times.get(4)));
                    binding.day6Card.dateTextView.setText(String.valueOf(times.get(5)));
                    binding.day7Card.dateTextView.setText(String.valueOf(times.get(6)));

                    binding.day1Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(0)) + "\u00B0C");
                    binding.day2Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(1))+ "\u00B0C");
                    binding.day3Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(2))+ "\u00B0C");
                    binding.day4Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(3))+ "\u00B0C");
                    binding.day5Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(4))+ "\u00B0C");
                    binding.day6Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(5))+ "\u00B0C");
                    binding.day7Card.maxTempTextView.setText(String.valueOf(maxTemperatures.get(6))+ "\u00B0C");

                    binding.day1Card.minTempTextView.setText(String.valueOf(minTemperatures.get(0))+ "\u00B0C");
                    binding.day2Card.minTempTextView.setText(String.valueOf(minTemperatures.get(1))+ "\u00B0C");
                    binding.day3Card.minTempTextView.setText(String.valueOf(minTemperatures.get(2))+ "\u00B0C");
                    binding.day4Card.minTempTextView.setText(String.valueOf(minTemperatures.get(3))+ "\u00B0C");
                    binding.day5Card.minTempTextView.setText(String.valueOf(minTemperatures.get(4))+ "\u00B0C");
                    binding.day6Card.minTempTextView.setText(String.valueOf(minTemperatures.get(5))+ "\u00B0C");
                    binding.day7Card.minTempTextView.setText(String.valueOf(minTemperatures.get(6))+ "\u00B0C");
                });


        weatherViewModel.getListOfLocationMode().observe(getViewLifecycleOwner(), locationModels -> {
                    ArrayAdapter<LocationModel> adapter = new ArrayAdapter<LocationModel>(getContext(), R.layout.spinner_weather_location_item_layout, locationModels) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            if (convertView == null) {
                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                convertView = inflater.inflate(R.layout.spinner_weather_location_item_layout, parent, false);
                            }
                            // Set the description text
                            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.spinner_item_description);
                            descriptionTextView.setText(locationModels.get(position).description);

                            // Set the id text
                            TextView idTextView = (TextView) convertView.findViewById(R.id.spinner_item_id);
                            idTextView.setText(locationModels.get(position).id);

                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            return getView(position, convertView, parent);
                        }
                    };

                    int index = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).latitude == latitude && adapter.getItem(i).longitude == longitude) {
                            index = i;
                            break;
                        }
                    }
                    spinner.setAdapter(adapter);
                    spinner.setSelection(index);
                }
        );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the id from the selected item
                try {
                    TextView idTextView = (TextView) view.findViewById(R.id.spinner_item_id);

                    String selectedId = idTextView.getText().toString();
                    LocationModel selected_location = weatherViewModel.getLocationData(selectedId);
                    longitude = selected_location.longitude;
                    latitude = selected_location.latitude;
                    updateLongLat(longitude, latitude);
                    // Do something with the selected id
                    Log.d(TAG, "Selected item id: " + selectedId);

                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }

        });


        binding.GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.DeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected location ID
                TextView locationIDTextView = (TextView) binding.spinnerWeatherLocations.getSelectedView().findViewById(R.id.spinner_item_id);
                final String locationID = locationIDTextView.getText().toString();

                // Build a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this location?")
                        .setTitle("Confirmation");

                // Add the OK button action
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button, delete the location
                        weatherViewModel.deleteLocation(locationID);
                        updateLocationList();
                        spinner.setSelection(0);
                    }
                });

                // Add the Cancel button action
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Cancel button, do nothing
                    }
                });

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        updateLongLat(longitude, latitude);
        updateLocationList();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    // Receive the result data from the new Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Pass the result data to the desired fragment using the fragment manager
            double resultDataLong = data.getExtras().getDouble("long");
            double resultDataLat = data.getExtras().getDouble("lat");

            // Update the WeatherFragment with the new location data
            this.updateLongLat(resultDataLong, resultDataLat);
            updateLocationList();
        }
    }

    //
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

