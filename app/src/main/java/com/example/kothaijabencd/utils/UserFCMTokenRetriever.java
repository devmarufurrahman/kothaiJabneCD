package com.example.kothaijabencd.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kothaijabencd.utils.Notification.SendNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class UserFCMTokenRetriever {

    private String title;
    private String body;
    private Context context;

    public UserFCMTokenRetriever(String title, String body, Context context) {
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public UserFCMTokenRetriever() {
    }

    public void retrieveUserFCMTokens() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query users collection to filter users with userRole equal to 3 or 2
        db.collection("user_profile")
                .whereIn("user_role", Arrays.asList(2, 3))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // For each user document with userRole equal to 3 or 2, retrieve the FCM tokens array
                                String fcmTokens = document.getString("userToken");
                                if (fcmTokens != null) {
                                    // Split the string into individual tokens
                                    String[] tokensArray = fcmTokens.split(",");
                                    // Loop through the FCM tokens array and handle each token
                                    for (String fcmToken : tokensArray) {
                                        // Use the FCM token for further processing (e.g., sending notifications)
                                        Log.d("FCM Token", fcmToken);
                                        // Call your method here to handle the FCM token
                                        // SendNotification(fcmToken, title, message, context);
                                        SendNotification sendNotification = new SendNotification(fcmToken.trim(),title,body,context);
                                        sendNotification.sendNotificationToRider();
                                    }
                                } else {
                                    // FCM tokens array is not found or null
                                    Log.e("FCM Tokens", "FCM tokens array not found for the user");
                                }
                            }
                        } else {
                            // Failed to query users collection
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
