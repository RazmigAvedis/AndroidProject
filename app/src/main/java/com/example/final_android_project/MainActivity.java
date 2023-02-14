package com.example.final_android_project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    public String getUsername(){
        TextView usernametxtview = findViewById(R.id.editusername);
        return usernametxtview.getText().toString();
    }

    public String Ecrypt_Pass(){
        //getting pass and encrypting it
        TextView passtxtview = findViewById(R.id.editpass);
        String pass_text = passtxtview.getText().toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pass_text.getBytes());
            byte[] hash = messageDigest.digest();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        }
        catch (NoSuchAlgorithmException e) {
           return "no such algorithm";
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn=findViewById(R.id.loginBtn);
        Button Registerbtn=findViewById(R.id.registerbtn);
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=getUsername();
                String encryptedpass=Ecrypt_Pass();
                String fileName = getPackageName();
                SharedPreferences sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",username);
                editor.putString("password", encryptedpass);
                editor.apply();
                Toast.makeText(MainActivity.this, "registration completed!", Toast.LENGTH_SHORT).show();
            }
        }
        );

        // check credentials
        loginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                String username=getUsername();
                String encryptedpass=Ecrypt_Pass();
                  String fileName = getPackageName();
                   SharedPreferences sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
             String storedUsername = sharedPreferences.getString("username", null);
             String storedPassword = sharedPreferences.getString("password", null);

             if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(encryptedpass)) {

                 Toast.makeText(MainActivity.this, "correct", Toast.LENGTH_SHORT).show();
                 Intent intent1=new Intent(MainActivity.this,DashboardActivity.class);
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