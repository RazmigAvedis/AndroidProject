package com.example.final_android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    public String getUsername() {
        TextView usernametxtview = findViewById(R.id.loginUsernameEditText);
        return usernametxtview.getText().toString();
    }

    public String Ecrypt_Pass() {
        //getting pass and encrypting it
        TextView passtxtview = findViewById(R.id.loginPasswordEditText);
        String pass_text = passtxtview.getText().toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pass_text.getBytes());
            byte[] hash = messageDigest.digest();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            return "no such algorithm";
        }
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn = findViewById(R.id.loginButton);
        Button Registerbtn = findViewById(R.id.loginRegisterButton);
        Registerbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(MainActivity.this, Register.class);
                        startActivity(intent1);
                    }
                }
        );

        // check credentials
        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = getUsername();
                        String encryptedpass = Ecrypt_Pass();
                        mDatabaseHelper = new DatabaseHelper(MainActivity.this);

                        boolean success = mDatabaseHelper.checkProfile(username, encryptedpass);

                        if (success) {

                            String fileName = getPackageName();
                            SharedPreferences sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentUsername",username);
                            editor.apply();

                            Toast.makeText(MainActivity.this, "correct", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(intent1);
                        } else {
                            // Credentials are incorrect
                            Toast.makeText(MainActivity.this, "incorrect", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}