package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kothaijabencd.databinding.ActivityFoodDeliveryBinding;
import com.example.kothaijabencd.utils.FieldValidation;
import com.example.kothaijabencd.utils.Order.FoodOrder;
import com.example.kothaijabencd.utils.UserFCMTokenRetriever;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class FoodDeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActivityFoodDeliveryBinding binding;
    Context context = FoodDeliveryActivity.this;
    String title="Food Order", body="", uuid="", name = "", today = "", userToken = "";
    FieldValidation validation = new FieldValidation();
    DatabaseReference db ;

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
        userToken = sharedPreferences.getString("uerToken","");

//        firebase database setup
        // Get a reference to the Firebase database
        db = FirebaseDatabase.getInstance().getReference();

        binding.placeOrder.setOnClickListener(v -> {
            saveFoodOrder();
        });
    }

    private void saveFoodOrder() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.now();
            today = dateTime.toString();
            Log.d("Current Date and Time (update)", today);
        } else {
            Date currentDate = new Date();

            // Print the current date and time in the default format (yyyy-MM-dd HH:mm:ss)
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            today = defaultFormat.format(currentDate);
            Log.d("Current Date and Time (Default)", today);
        }


        String foodName = binding.foodName.getText().toString();
        String restaurantName = binding.restaurantName.getText().toString();
        String restaurantAddress = binding.restaurantAddress.getText().toString();
        String foodAmount = binding.amountFood.getText().toString();
        String foodDetails = binding.foodDetails.getText().toString();
        String destination = binding.destination.getText().toString();
        if (foodName.equals("")){
            validation.fieldFocus(binding.foodName,"give food name");
        } else if (restaurantName.equals("")) {
            validation.fieldFocus(binding.restaurantName,"give restaurant name");
        } else if (foodAmount.equals("")) {
            validation.fieldFocus(binding.amountFood,"give food amount");
        } else if (destination.equals("")) {
            validation.fieldFocus(binding.restaurantName,"give restaurant name");
        } else {
            if (uuid != null || !uuid.isEmpty()){
                binding.progressbar.setVisibility(View.VISIBLE);
                body = name + " order "+ foodName;
//                send notification
                UserFCMTokenRetriever userFCMTokenRetriever = new UserFCMTokenRetriever(title, body, context);
                userFCMTokenRetriever.retrieveUserFCMTokens();

                // Create a reference to the location where the data will be saved
                DatabaseReference productOrderRef = db.child("productOrders").child(uuid);


                // Create a ProductOrder object with the provided data
                FoodOrder productOrder = new FoodOrder(foodName, restaurantName, restaurantAddress, foodAmount, foodDetails, destination, today, "", "", "food", name, "", userToken);

                // Save the ProductOrder object to the database
                productOrderRef.setValue(productOrder);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(context, "Order Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Order Unsuccessfully", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
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