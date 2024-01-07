package com.example.kothaijabencd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class DeliveryManReg extends AppCompatActivity {

    Button select_gender, select_transport;
    ArrayList<String> blood_group, religion, gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man_reg);
        select_gender = findViewById(R.id.select_gender);
        select_transport = findViewById(R.id.select_transport);
    }
}