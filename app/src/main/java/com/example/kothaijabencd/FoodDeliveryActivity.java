package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.kothaijabencd.databinding.ActivityFoodDeliveryBinding;
import com.example.kothaijabencd.utils.FieldValidation;
import com.example.kothaijabencd.utils.UserFCMTokenRetriever;

public class FoodDeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActivityFoodDeliveryBinding binding;
    Context context = FoodDeliveryActivity.this;
    String title="Food Order", body="", uuid="", name = "";
    FieldValidation validation = new FieldValidation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDeliveryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        toolbar setup here
        toolbar = binding.customToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        shared data get here
        SharedPreferences sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        uuid = sharedPreferences.getString("id","");

        
        binding.placeOrder.setOnClickListener(v -> {
            saveFoodOrder();

            UserFCMTokenRetriever userFCMTokenRetriever = new UserFCMTokenRetriever(title, body, context);
            userFCMTokenRetriever.retrieveUserFCMTokens();
        });
    }

    private void saveFoodOrder() {
        String foodName = binding.foodName.getText().toString();
        String restaurantName = binding.restaurantName.getText().toString();
        String restaurantAddress = binding.restaurantAddress.getText().toString();
        String foodAmount = binding.amountFood.getText().toString();
        String foodDetails = binding.foodDetails.getText().toString();
        if (foodName.equals("")){
            validation.fieldFocus(binding.foodName,"give food name");
        } else if (restaurantName.equals("")) {
            validation.fieldFocus(binding.restaurantName,"give restaurant name");
        }
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