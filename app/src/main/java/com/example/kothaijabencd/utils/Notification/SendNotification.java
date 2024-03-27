package com.example.kothaijabencd.utils.Notification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {
    private String riderToken;
    private String title;
    private String message;
    private Context context;
    private static final String SERVER_KEY = "AAAAH-KB6HI:APA91bG6A1DPnbn0WKea66eL7WBt4O_Pm_KEES_ixwR6xPWchMLmk74F_7QKiXfGSA4THgx_VI3sW1okGssM2LQpOoEm0MIsL005b8vCgUnyuLzQGswHclXVh7kIq7F3l-8YOUAHU9Jc";
    private static final String url = "https://fcm.googleapis.com/fcm/send";


    public SendNotification() {
    }

    public SendNotification(String riderToken, String title, String message, Context context) {
        this.riderToken = riderToken;
        this.title = title;
        this.message = message;
        this.context = context;
    }


    // Method to send notification to a single rider device
    public void sendNotificationToRider() {
        try {
            // Create JSON payload for notification and  Create JSON payload for FCM message
            JSONObject notificationPayload = new JSONObject();
            JSONObject messagePayload = new JSONObject();
            try{

                notificationPayload.put("title", title);
                notificationPayload.put("body", message);
                messagePayload.put("to", riderToken);
                messagePayload.put("notification", notificationPayload);

            } catch (JSONException e){
                e.printStackTrace();
            }


            // Create JsonObjectRequest
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url, messagePayload,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Notification sent successfully to rider: " );
                            System.out.println(messagePayload );
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
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRiderToken() {
        return riderToken;
    }

    public void setRiderToken(String riderToken) {
        this.riderToken = riderToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
