package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.kothaijabencd.databinding.ActivityFoodDeliveryBinding;
import com.example.kothaijabencd.utils.UserFCMTokenRetriever;

public class FoodDeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActivityFoodDeliveryBinding binding;
    Context context = FoodDeliveryActivity.this;
    String title="Hi", body="Hello test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDeliveryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        toolbar = binding.customToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        
        binding.placeOrder.setOnClickListener(v -> {
            UserFCMTokenRetriever userFCMTokenRetriever = new UserFCMTokenRetriever(title, body, context);
            userFCMTokenRetriever.retrieveUserFCMTokens();
        });
    }

    private void subscriberTo() {

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