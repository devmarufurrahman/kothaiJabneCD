package com.example.kothaijabencd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    String login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // get shared preference value
        SharedPreferences preferences = getSharedPreferences("SharePreference", MODE_PRIVATE);
        login = preferences.getString("login","");

        // splash screen delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (login.equals("login")){
                    Intent iHome = new Intent(SplashScreen.this,HomeActivity.class);
                    startActivity(iHome);
                    finish();
                } else {
                    Intent loginIntent = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        },2000);

    }

}