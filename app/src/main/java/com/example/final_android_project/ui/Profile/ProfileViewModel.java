package com.example.final_android_project.ui.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_android_project.AppConstants;
import com.example.final_android_project.DatabaseHelper;

public class ProfileViewModel extends ViewModel {
    // Declare MutableLiveData fields

    DatabaseHelper mDatabaseHelper;

    private MutableLiveData<String> username;
    private MutableLiveData<String> email;
    private MutableLiveData<String> phone;

    public ProfileViewModel() {
        // Initialize MutableLiveData fields
        email = new MutableLiveData<>();
        username = new MutableLiveData<>();
        phone = new MutableLiveData<>();
    }

    public void loadDataForUser(Context context) {
        // Load data from SharedPreferences1

        String fileName = context.getPackageName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString(AppConstants.USER_ID_KEY, null);
        mDatabaseHelper = new DatabaseHelper(context);
        Log.i("ProfileActivity", userID);

        Cursor user = mDatabaseHelper.getItem(userID);

        Log.i("ProfileActivity", "user"+ user);
        if (user != null && user.moveToFirst()) {
            int usernameIndex = user.getColumnIndex("username");
            int emailIndex = user.getColumnIndex("email");
            int phoneIndex = user.getColumnIndex("phone");

            if (usernameIndex >= 0 && emailIndex >= 0 && phoneIndex >= 0) {
                username.setValue(user.getString(usernameIndex));
                Log.i("ProfileActivity", "username"+ username);
                email.setValue(user.getString(emailIndex));
                Log.i("ProfileActivity", "email" +email);
                phone.setValue(user.getString(phoneIndex));
                Log.i("ProfileActivity", "phone" +phone);
            }

            user.close();
        }

        // You can also load and set the values of email and password in a similar way
    }

    // Declare getter methods for the LiveData fields
    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPhone() {
        return phone;
    }
}
