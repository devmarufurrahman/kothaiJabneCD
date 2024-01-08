package com.example.kothaijabencd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PassengerOrDriver extends AppCompatActivity {
    Button userReg, driverReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_or_driver);

        //id selection
        userReg = findViewById(R.id.userReg);
        driverReg = findViewById(R.id.driverReg);


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
//                Intent intent = new Intent(PassengerOrDriver.this, RiderReg.class);
//                startActivity(intent);
                Toast.makeText(PassengerOrDriver.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}