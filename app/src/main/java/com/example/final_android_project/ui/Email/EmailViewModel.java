package com.example.final_android_project.ui.Email;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_android_project.AppConstants;
import com.example.final_android_project.DatabaseHelper;
import com.example.final_android_project.JavaMailInboxAPI;
import com.example.final_android_project.MyMessage;

import java.util.List;

public class EmailViewModel extends ViewModel {

    DatabaseHelper mDatabaseHelper;
    private final MutableLiveData<String> userEmail;

    private final MutableLiveData<List<MyMessage>> Inbox;

    private final MutableLiveData<Boolean> busy;

    public EmailViewModel() {
        busy = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        Inbox = new MutableLiveData<>();
    }


    public void getInbox(Context context, String mEmailFrom) {
        busy.setValue(true);
        JavaMailInboxAPI javaMailInboxAPI = new JavaMailInboxAPI(context, mEmailFrom, new JavaMailInboxAPI.OnEmailResultListener() {
            @Override
            public void onSuccess(List<MyMessage> messages) {
                Inbox.postValue(messages);
                busy.postValue(false);
            }
            @Override
            public void onError(String errorMessage) {
            }
        });

        javaMailInboxAPI.execute();
    }

    public void setUserEmail(Context context) {

        String fileName = context.getPackageName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString(AppConstants.USER_ID_KEY, null);
        mDatabaseHelper = new DatabaseHelper(context);
        Log.i("ProfileActivity", userID);

        Cursor user = mDatabaseHelper.getItem(userID);

        Log.i("ProfileActivity", "user" + user);
        if (user != null && user.moveToFirst()) {
            int emailIndex = user.getColumnIndex("email");

            if (emailIndex >= 0) {
                userEmail.setValue(user.getString(emailIndex));
                Log.i("ProfileActivity", "email" + userEmail);
            }

            user.close();
        }
    }

    public LiveData<String> getuserEmail() {
        return userEmail;
    }
    public LiveData<List<MyMessage>> getuserInbox() {
        return Inbox;
    }

    public LiveData<Boolean> getBusy(){return  busy;}
}