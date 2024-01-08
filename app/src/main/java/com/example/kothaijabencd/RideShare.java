package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class RideShare extends AppCompatActivity {

    Button findRider, selectDestination;
    Toolbar toolbar;
    TextView  currentLocation, destinationLocation, distanceValue, distanceCost;

    String destination, event_latitude;
    float distance;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_share);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //id define
        findRider = findViewById(R.id.findRiderBtn);
        selectDestination = findViewById(R.id.selectDestination);
        currentLocation = findViewById(R.id.currentLocation);
        destinationLocation = findViewById(R.id.destinationLocation);
        distanceValue = findViewById(R.id.distance);
        distanceCost = findViewById(R.id.distanceCost);

        // get intent value
        Intent getIntent = getIntent();
        distance = getIntent.getFloatExtra("distance",0);
        destination = getIntent.getStringExtra("destination");
//        getIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        // set intent value
        if (distance != 0 && !destination.equals("")){
            distanceValue.setText("Distance: "+ decimalFormat.format(distance) + " km");
            destinationLocation.setText("Destination Location: "+ destination);
            getCostRide(distance);
        }



        // location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


        // rider find
        findRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // location select
        selectDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideShare.this, RideShareLocation.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });

    }

    private void getCostRide(float dis) {
        float disCost = (int) (dis * 9);
        distanceCost.setText("Payable: " + disCost +" Tk");
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
                    Geocoder geocoder = new Geocoder(RideShare.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        //List<Address> addressList = geocoder.getFromLocation(23.755247, 90.393884, 1);

//                        user_latitude = addressList.get(0).getLatitude();
//                        user_longitude = addressList.get(0).getLongitude();
//                         address = addressList.get(0).getAddressLine(0);


                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

//
//
//        search_btn.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        String placeName = map_search.getText().toString().trim();
//
//        if (!placeName.isEmpty()) {
//        Geocoder geocoder = new Geocoder(RideShare.this);
//        try {
//        List<Address> addressList = geocoder.getFromLocationName(placeName, 1);
//
//        if (!addressList.isEmpty()) {
//        Address addresss = addressList.get(0);
//        double place_latitude = addresss.getLatitude();
//        double place_longitude = addresss.getLongitude();
//
//        address = addressList.get(0).getAddressLine(0);
//
//        LatLng latLng = new LatLng(place_latitude, place_longitude);
//
//        googleMap.clear(); // Clear previous markers
//        googleMap.addMarker(new MarkerOptions().position(latLng).title(placeName));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
//
//
//        event_latitude = String.valueOf(place_latitude);
//        event_longitude = String.valueOf(place_longitude);
//
//        latitude.setText("latitude = " + event_latitude);
//        longitude.setText("Longitude = " + event_longitude);
//
//        Toast.makeText(RideShare.this, "EVENT LATITUDE = " + event_latitude + "EVENT LONGITUDE = " + event_longitude, Toast.LENGTH_SHORT).show();
//
//        } else {
//        Toast.makeText(RideShare.this, "No results found for the specified place", Toast.LENGTH_SHORT).show();
//        }
//        } catch (IOException e) {
//        e.printStackTrace();
//        Toast.makeText(RideShare.this, "Error occurred while searching for the place", Toast.LENGTH_SHORT).show();
//        }
//        } else {
//        Toast.makeText(RideShare.this, "Please enter a place name", Toast.LENGTH_SHORT).show();
//        }
//        }
//        });
