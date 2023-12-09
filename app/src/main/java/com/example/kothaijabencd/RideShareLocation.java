package com.example.kothaijabencd;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.kothaijabencd.databinding.ActivityRideShareLocationBinding;

import java.io.IOException;
import java.util.List;

public class RideShareLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRideShareLocationBinding binding;
    String event_longitude, event_latitude;
    double user_latitude, user_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRideShareLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(23.753233512455356, 90.39088015587427);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Customize the map settings
        // You can add markers, set the camera position, enable/disable gestures, etc.
        LatLng latLng = new LatLng(user_latitude, user_longitude);
        //LatLng latLng = new LatLng(23.64184558104228, 88.63826319419519);
        MarkerOptions markerOptions = new MarkerOptions();
        // Set position of marker
        markerOptions.position(latLng);
        googleMap.isBuildingsEnabled();
        // Set title of marker
        markerOptions.title("Current Location");

        final Marker[] previousMarker = {null};

//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String placeName = map_search.getText().toString().trim();
//
//                if (!placeName.isEmpty()) {
//                    Geocoder geocoder = new Geocoder(RideShare.this);
//                    try {
//                        List<Address> addressList = geocoder.getFromLocationName(placeName, 1);
//
//                        if (!addressList.isEmpty()) {
//                            Address address = addressList.get(0);
//                            double place_latitude = address.getLatitude();
//                            double place_longitude = address.getLongitude();
//
//                            LatLng latLng = new LatLng(place_latitude, place_longitude);
//
//                            googleMap.clear(); // Clear previous markers
//                            googleMap.addMarker(new MarkerOptions().position(latLng).title(placeName));
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
//
//
//                            event_latitude = String.valueOf(place_latitude);
//                            event_longitude = String.valueOf(place_longitude);
//
//                            latitude.setText("latitude = " + event_latitude);
//                            longitude.setText("Longitude = " + event_longitude);
//
//                            Toast.makeText(RideShareLocation.this, "EVENT LATITUDE = " + event_latitude + "EVENT LONGITUDE = " + event_longitude, Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(RideShareLocation.this, "No results found for the specified place", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Toast.makeText(RideShareLocation.this, "Error occurred while searching for the place", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(RideShareLocation.this, "Please enter a place name", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Remove the previous marker if it exists
                if (previousMarker[0] != null) {
                    previousMarker[0].remove();
                }

                event_latitude = String.valueOf(latLng.latitude);
                event_longitude = String.valueOf(latLng.longitude);


//                latitude.setText("latitude = " + event_latitude);
//                longitude.setText("Longitude = " + event_longitude);


                MarkerOptions clickedMarkerOptions = new MarkerOptions();
                clickedMarkerOptions.position(latLng);
                clickedMarkerOptions.title("Destination Location");
                previousMarker[0] = googleMap.addMarker(clickedMarkerOptions);
            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RideShareLocation.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
        googleMap.addMarker(markerOptions);

    }

}