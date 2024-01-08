package com.example.kothaijabencd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class UserReg extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    EditText userName, userAddress, userOccupation;
    Button date_birth, select_gender, select_religion;
    ShapeableImageView userNid, userProfilePhoto;
    ArrayList<String>  religion,  gender;
    int day, month, year, religion_ref= 0, gender_ref= 0, myday, myMonth, myYear;
    String birth_date = "", encodedImage = "";
    Bitmap bitmap;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        // id selection
        userName = findViewById(R.id.userName);
        userAddress = findViewById(R.id.userAddress);
        userOccupation = findViewById(R.id.userOccupation);
        date_birth = findViewById(R.id.date_birth);
        select_gender = findViewById(R.id.select_gender);
        select_religion = findViewById(R.id.select_religion);
        userNid = findViewById(R.id.userNid);
        userProfilePhoto = findViewById(R.id.userProfilePhoto);


        // birth day selection

        date_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_birth.setError(null);
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserReg.this, UserReg.this,year, month,day);
                datePickerDialog.show();
            }
        });


        //gender selection
        gender=new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");

        select_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(UserReg.this);
                dialog.setContentView(R.layout.custom_spinner_layout_2);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(UserReg.this, android.R.layout.simple_list_item_1,gender);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        String item  = adapter.getItem(position);
                        switch (Objects.requireNonNull(item)){
                            case "Male" :
                                gender_ref = 1;
                                select_gender.setText(item);
                                select_gender.setError(null);
                                dialog.dismiss();
                                break;
                            case "Female" :
                                gender_ref = 2;
                                select_gender.setText(item);
                                select_gender.setError(null);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                gender_ref = 3;
                                select_gender.setText(item);
                                select_gender.setError(null);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });



        // religion selection

        religion=new ArrayList<>();
        religion.add("Islam");
        religion.add("Hinduism");
        religion.add("Christianity");
        religion.add("Buddhism");
        religion.add("Other");

        select_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_religion.setError(null);
                dialog = new Dialog(UserReg.this);
                dialog.setContentView(R.layout.custom_spinner_layout_2);
                Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(UserReg.this, android.R.layout.simple_list_item_1,religion);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        String item  = adapter.getItem(position);
                        switch (Objects.requireNonNull(item)){
                            case "Islam" :
                                religion_ref = 1;
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Hinduism" :
                                religion_ref = 2;
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Christianity" :
                                religion_ref = 3;
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Buddhism" :
                                religion_ref = 4;
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                religion_ref = 5;
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });


        // select nid
        userNid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withActivity(UserReg.this)
                            .withPermission(Manifest.permission.READ_MEDIA_IMAGES)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
                                }
                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                    Toast.makeText(UserReg.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }


                            }).check();
                } else {

                    Dexter.withActivity(UserReg.this)
                            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

                                }
                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                    Toast.makeText(UserReg.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
            }
        });


    }


// select nid image method

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK){

            assert data != null;
            Uri filepath=data.getData();
            try {

                InputStream inputStream= getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                userNid.setImageBitmap(bitmap);
                encodeBitmapImage();

            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
        byte[] bytesOfImage=byteArrayOutputStream.toByteArray();
        int lengthbmp = bytesOfImage.length;
        lengthbmp=lengthbmp/1024;
        System.out.println("image length : " + lengthbmp);

        if (lengthbmp>2048){

            Toast.makeText(this, "Image Too Large...select a smaller one", Toast.LENGTH_SHORT).show();

        } else if (lengthbmp==0){

            encodedImage="";

        }else{

            encodedImage= Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
        }
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        birth_date = (myday + "/" + (myMonth+1) + "/" + myYear);
        date_birth.setText(birth_date);
    }
}