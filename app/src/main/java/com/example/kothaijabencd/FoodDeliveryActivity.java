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
import com.example.kothaijabencd.utils.ActivityLogReadWrite;
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
    String title="Food Order", body="", uuid="", name = "", today = "";
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
            // Print the current date and time in the default format (yyyy-MM-dd HH:mm:ss)
            DateTimeFormatter defaultFormat =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            today = dateTime.format(defaultFormat);
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
            binding.progressbar.setVisibility(View.VISIBLE);
            if (uuid != null || !uuid.isEmpty()){
                body = name + " order "+ foodName;
//                send notification
                UserFCMTokenRetriever userFCMTokenRetriever = new UserFCMTokenRetriever(title, body, context);
                userFCMTokenRetriever.retrieveUserFCMTokens();

                // Generate a unique key for the food order
                String orderKey = db.child("ProductOrders").child(uuid).child("food").push().getKey();

                // Create a reference to the location where the data will be saved
                assert orderKey != null;
                DatabaseReference productOrderRef = db.child("ProductOrders").child(uuid).child("food").child(orderKey);
                // Create a ProductOrder object with the provided data
                FoodOrder productOrder = new FoodOrder(foodName, restaurantName, restaurantAddress, foodAmount, foodDetails, destination, today, "", "", name, "N/A", uuid, 1, 2);

                // Save the ProductOrder object to the database
                productOrderRef.setValue(productOrder);
                Toast.makeText(context, "Order Successfully", Toast.LENGTH_SHORT).show();

                // Activity Added
                // Generate activity key
                String activityKey = db.child("Activity").child(uuid).push().getKey();

//                create reference to the location where activity will save
                assert activityKey != null;
                DatabaseReference activityRef = db.child("Activity").child(uuid).child(activityKey);

                // Object provide the data
                ActivityLogReadWrite logWrite = new ActivityLogReadWrite("Food Order", orderKey, uuid, name, today, "", 1);
                activityRef.setValue(logWrite);

                binding.progressbar.setVisibility(View.GONE);
                finish();
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