package com.example.final_android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    private MapView map;
    private IMapController mapController;

    private static final String TAG = "OsmActivity";

WeatherDatabaseHelper mDatabaseHelper;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        //handle permissions first, before map is created. not depicted here
        Button getLocationButton = (Button) findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            IGeoPoint iGeoPoint=map.getMapCenter();
                Log.i("Weather",String.valueOf(iGeoPoint.getLatitude()));

                Bundle bundle = new Bundle();
                bundle.putDouble("long",iGeoPoint.getLongitude());
                bundle.putDouble("lat",iGeoPoint.getLatitude());

                Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(iGeoPoint.getLatitude(),iGeoPoint.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String locationName = "";
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    locationName = address.getAddressLine(0);
                }


                String fileName = getPackageName();
                SharedPreferences sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                String userID = sharedPreferences.getString(AppConstants.USER_ID_KEY, null);
                mDatabaseHelper = new WeatherDatabaseHelper(LocationActivity.this,userID);

                String[] values ={String.valueOf(iGeoPoint.getLongitude()),String.valueOf(iGeoPoint.getLatitude()),locationName};
                mDatabaseHelper.addData(values);

                Intent intent = new Intent(LocationActivity.this, MainDashboardActivity.class);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);

// Finish the new Activity
                finish();
            }
        });

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map


        if (Build.VERSION.SDK_INT >= 23) {

        }


        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(51496994, -134733);
        mapController.setCenter(startPoint);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (map != null)
            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        if (map != null)
            map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

}