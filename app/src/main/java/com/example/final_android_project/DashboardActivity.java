package com.example.final_android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        RelativeLayout profileBtn = findViewById(R.id.DashboardProfileBtn);
        RelativeLayout weatherBtn = findViewById(R.id.DashboardWeatherBtn);
        profileBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent1 = new Intent(DashboardActivity.this, profile_activity.class);

                        startActivity(intent1);
                    }
                }
        );


        weatherBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(DashboardActivity.this, LocationActivity.class);

                        startActivity(intent);
                    }
                }
        );
    }
}