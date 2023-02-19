package com.example.final_android_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profile_activity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String fileName = getPackageName();
        SharedPreferences sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("currentUsername", null);
        Log.i("ProfileActivity", storedUsername);
        TextView myTextView = (TextView) findViewById(R.id.tv_name);
        myTextView.setText(storedUsername);
    }
}