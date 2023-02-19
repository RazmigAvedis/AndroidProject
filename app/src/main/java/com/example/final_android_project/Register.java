package com.example.final_android_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;


    public String Ecrypt_Pass(String pass_text) {
        //getting pass and encrypting it;
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
        setContentView(R.layout.activity_register);
        Button registerBtn = (Button) findViewById(R.id.registerButton);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.usernameEditText);
                EditText password = (EditText) findViewById(R.id.passwordEditText);
                EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);


                String encryptedPass = Ecrypt_Pass(password.getText().toString());
                Log.i("Register", encryptedPass);

                if (encryptedPass == "no such algorithm") {
                    Toast.makeText(Register.this, "Encryption Error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("Password", password.getText().toString());
                Log.i("Confirm Password", confirmPassword.getText().toString());
                boolean x = (password.getText().toString() == confirmPassword.getText().toString());
                if (x) Log.i("Result", "true");
                else Log.i("Result", "false");


                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    mDatabaseHelper = new DatabaseHelper(Register.this);
                    String[] values = {username.getText().toString(), encryptedPass};
                    boolean checkuser = mDatabaseHelper.checkProfile(username.getText().toString(), encryptedPass);
                    if (!checkuser) {

                        boolean success = mDatabaseHelper.addData(values);

                        if (success) {

                            String fileName = getPackageName();
                            SharedPreferences sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentUsername",username.getText().toString());
                            editor.apply();

                            RegisterToDashboardOKMessageBox("Success", "User Registered", (dialog, id) -> {
                                // User clicked OK button

                                Intent intent1 = new Intent(Register.this, DashboardActivity.class);
                                startActivity(intent1);
                            });
                        } else {
                            // Credentials are incorrect
                            Toast.makeText(Register.this, "User Not Registered, check your credentials!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        RegisterToDashboardOKMessageBox("Info", "User Is Already Registered", (dialog, id) -> {
                            // User clicked OK button
                        });
                    }
                } else {
                    Toast.makeText(Register.this, "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void RegisterToDashboardOKMessageBox(String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}