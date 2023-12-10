package com.example.kothaijabencd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kothaijabencd.R;
import com.example.kothaijabencd.utils.NotificationListData;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<NotificationListData> {
    public NotificationListAdapter(@NonNull Context context, ArrayList<NotificationListData> dataArrayList) {
        super(context, R.layout.notifiction_design_layout,dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        NotificationListData listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.notifiction_design_layout,parent,false);

        }

        ImageView notificationImg = view.findViewById(R.id.notificationImg);
        TextView notificationTitle = view.findViewById(R.id.notificationTitle);
        TextView notificationDesc = view.findViewById(R.id.notificationDesc);
        TextView notificationTime = view.findViewById(R.id.notificationTime);


        assert listData != null;
        notificationImg.setImageResource(listData.getImg());
        notificationTime.setText(listData.getTime());
        notificationTitle.setText(listData.getTitle());
        notificationDesc.setText(listData.getDesc());

        return view;
    }

}
