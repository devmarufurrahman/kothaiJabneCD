package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.kothaijabencd.databinding.ActivityRideShareLocationBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RideShareLocation extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityRideShareLocationBinding binding;
    String event_longitude, event_latitude, address;
    double lastLat, lastLong, user_latitude, user_longitude;
    private  LatLng destinationLocation, userLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView destination_area, selectedOrNot;
    Button backRideShare;
    float km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRideShareLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // id selection
        destination_area = findViewById(R.id.destination_area);
        selectedOrNot = findViewById(R.id.selectedOrNot);
        backRideShare = findViewById(R.id.backRideShare);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        backRideShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideShareLocation.this, RideShare.class);
                intent.putExtra("distance",km);
                intent.putExtra("destination",address);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RideShareLocation.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        googleMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                destinationLocation = latLng;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(destinationLocation);
                markerOptions.icon(setIcon(RideShareLocation.this,R.drawable.user_location));
                markerOptions.title("Destination Location");
                mMap.addMarker(markerOptions);
                
                
                // route method
                getCalculateRoute();
                
            }
        });

        fetchMyLocation();
    }



    private void fetchMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lastLat = location.getLatitude();
                lastLong = location.getLongitude();

                userLocation = new LatLng(lastLat,lastLong);
                LatLng latLng = new LatLng(lastLat,lastLong);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(16.0f)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location")
                        .icon(setIcon(RideShareLocation.this,R.drawable.user_location)));


            }
        });
    }
    public BitmapDescriptor setIcon(Activity context, int drawableID){
        Drawable drawable = ActivityCompat.getDrawable(context,drawableID);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap =Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void getCalculateRoute() {

        Location startPoint=new Location("Start Point");
        startPoint.setLatitude(lastLat);
        startPoint.setLongitude(lastLong);

        Location endPoint=new Location("End Point");
        endPoint.setLatitude(destinationLocation.latitude);
        endPoint.setLongitude(destinationLocation.longitude);



        double distance=startPoint.distanceTo(endPoint);
        km = (float) (distance / 1000);

        Log.d("distance", String.valueOf(km));
        Log.d("distanc", String.valueOf(distance));

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(destinationLocation.latitude,destinationLocation.longitude,1);

            user_latitude = addressList.get(0).getLatitude();
            user_longitude = addressList.get(0).getLongitude();
            address = addressList.get(0).getAddressLine(0);

            System.out.println(address + "name");
            destination_area.setText("Area: "+ address);
            selectedOrNot.setText("Your destination is selected");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;


    }

}