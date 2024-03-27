package com.example.kothaijabencd.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.kothaijabencd.SplashScreen;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocation {

    FusedLocationProviderClient fusedLocationProviderClient;
    String address;



    public CurrentLocation(Context context) {

        // location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        getCurrentLocation(context);
    }

    // get current location
    public void getCurrentLocation(Context context) {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        //List<Address> addressList = geocoder.getFromLocation(23.755247, 90.393884, 1);

//                        user_latitude = addressList.get(0).getLatitude();
//                        user_longitude = addressList.get(0).getLongitude();
//                        address = addressList.get(0).getAddressLine(0);
                        setAddress(addressList.get(0).getAddressLine(0));

                        Log.d("address", "address"+ address);
                        Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
                        setAddress(address);

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        setAddress(String.valueOf(e));
                    }
                }
            }
        });
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

}
