package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kothaijabencd.databinding.ActivityFoodDeliveryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodDeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActivityFoodDeliveryBinding binding;
    Context context = FoodDeliveryActivity.this;
    // Firebase Cloud Messaging server key
    private static final String SERVER_KEY = "AAAAH-KB6HI:APA91bG6A1DPnbn0WKea66eL7WBt4O_Pm_KEES_ixwR6xPWchMLmk74F_7QKiXfGSA4THgx_VI3sW1okGssM2LQpOoEm0MIsL005b8vCgUnyuLzQGswHclXVh7kIq7F3l-8YOUAHU9Jc";

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
            sendNotificationToRider("fyiqt4uSS8-nRgxlRHHY-N:APA91bFhpgiPiMoIsW7jgkn4aZ2cykwFx1Y3kGDcKfDtpGN14DuFjHlPXT2tr0OxWao38H9HkaAY4uWF-eAhdyOhkphIPpWgAMRLurP4Gqr0fYECzuScYk_rXVwwvPU61culXRnwfmN4", "Hi", "Hello test", context);
        });
    }


        // Method to send notification to a single rider device
        public static void sendNotificationToRider(String riderToken, String title, String message, Context context) {
            try {
                // Create JSON payload for notification
                JSONObject notificationPayload = new JSONObject();
                notificationPayload.put("title", title);
                notificationPayload.put("body", message);

                // Create JSON payload for FCM message
                JSONObject fcmPayload = new JSONObject();
                fcmPayload.put("to", riderToken);
                fcmPayload.put("data", notificationPayload);

                // Create JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", fcmPayload,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("Notification sent successfully to rider: " );
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.err.println("Failed to send notification to rider: " + riderToken);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "key=" + SERVER_KEY);
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                // Add request to Volley request queue
                Volley.newRequestQueue(context).add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
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