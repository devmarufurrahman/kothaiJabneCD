package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kothaijabencd.adapter.NotificationListAdapter;
import com.example.kothaijabencd.utils.NotificationListData;

import java.util.ArrayList;

public class NotificationView extends AppCompatActivity {

    NotificationListAdapter notificationListAdapter;
    ArrayList<NotificationListData> dataArrayList = new ArrayList<>();
    NotificationListData listData;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        listView = findViewById(R.id.listViewNotification);

        // toolbar
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // notification view

        int[] imgList = {R.drawable.avatar_image, R.drawable.avatar_image, R.drawable.avatar_image} ;
        String[] title = {"Bike offer","delivery offer","Medicine offer"} ;
        String[] desc = {"You can get a 50% discount on your first bike ride by using your friends invite code.","Order food from restaurants or groceries from top shops across Bangladesh ✓","MedEasy is a one-stop destination for all healthcare needs"} ;
        String[] time = {"2023-10-15 11:15, 2 days ago", "2023-10-15 11:15, 2 days ago", "2023-10-15 11:15, 2 days ago"} ;


        for(int i = 0; i<imgList.length; i++){
            listData = new NotificationListData(title[i], desc[i], time[i], imgList[i]);
            dataArrayList.add(listData);
        }

        notificationListAdapter = new NotificationListAdapter(NotificationView.this,dataArrayList);
        listView.setAdapter(notificationListAdapter);
        listView.setClickable(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(NotificationView.this, position, Toast.LENGTH_SHORT).show();

            }
        });
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