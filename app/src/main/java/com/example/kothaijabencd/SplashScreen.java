package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.kothaijabencd.utils.CurrentLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    String login="", address= "";
    CurrentLocation currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // get shared preference value
        SharedPreferences preferences = getSharedPreferences("SharePreference", MODE_PRIVATE);
//        login = preferences.getString("login","");



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            currentLocation = new CurrentLocation(SplashScreen.this);
            String address = currentLocation.getAddress();
//            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Log.d("address2", "address2"+address);

        }

        // splash screen delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent loginIntent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        },2000);

    }

}