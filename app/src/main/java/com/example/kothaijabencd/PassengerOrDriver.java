package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PassengerOrDriver extends AppCompatActivity {
    Button userReg, driverReg;
    Toolbar custom_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_or_driver);

        //id selection
        userReg = findViewById(R.id.userReg);
        driverReg = findViewById(R.id.driverReg);
        custom_toolbar = findViewById(R.id.custom_toolbar);

        // toolbar action
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // user reg event
        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerOrDriver.this, UserReg.class);
                startActivity(intent);
            }
        });


        // driver reg event
        driverReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerOrDriver.this, RiderReg.class);
                startActivity(intent);
//                Toast.makeText(PassengerOrDriver.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }



//    back button toolbar
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