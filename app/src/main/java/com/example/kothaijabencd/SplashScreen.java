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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    String login="", address= "";
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // get shared preference value
        SharedPreferences preferences = getSharedPreferences("SharePreference", MODE_PRIVATE);
//        login = preferences.getString("login","");



        // location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

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


    // get current location
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(SplashScreen.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        //List<Address> addressList = geocoder.getFromLocation(23.755247, 90.393884, 1);

//                        user_latitude = addressList.get(0).getLatitude();
//                        user_longitude = addressList.get(0).getLongitude();
                         address = addressList.get(0).getAddressLine(0);

                        Log.d("address", "adress"+ address);
                        Toast.makeText(SplashScreen.this, address, Toast.LENGTH_SHORT).show();

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}