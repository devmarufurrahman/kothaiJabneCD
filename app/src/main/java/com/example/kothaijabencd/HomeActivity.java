package com.example.kothaijabencd;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity {
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    CircleImageView user_photo;
    ImageSlider imageSlider;
    ArrayList<SlideModel> imageList = new ArrayList<>();
    LinearLayout ride_share_btn, parcel_delivery_btn, food_btn, medicine_btn;
    ImageView notificationImg;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    TextView profile_name, profile_level, profile_id, user_address, member_start_date;
    String name, id, address, startDate;
    int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // id define
        imageSlider = findViewById(R.id.imgSlider);
        ride_share_btn = findViewById(R.id.ride_share_btn);
        parcel_delivery_btn = findViewById(R.id.parcel_delivery_btn);
        food_btn = findViewById(R.id.food_btn);
        medicine_btn =findViewById(R.id.medicine_btn);
        notificationImg = findViewById(R.id.notificationImg);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        profile_name = findViewById(R.id.profile_name);
        profile_level = findViewById(R.id.profile_level);
        profile_id = findViewById(R.id.profile_id);
        user_address = findViewById(R.id.user_address);
        member_start_date = findViewById(R.id.member_start_date);
        user_photo = findViewById(R.id.user_photo);


        // image slider here
        imageList.add(new SlideModel(R.drawable.img_slide3, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.img_slide2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.slide_img1, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(imageList);

        // toolbar
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        navigationView= findViewById(R.id.nav_menu);
        drawerLayout= findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        bottomNavigationView = findViewById(R.id.bottomNavView);
//        bottomNavigationView.setSelectedItemId(R.id.location);
//        bottomNavigationView.setItemIconTintList(null);

        getUserData(firebaseUser);


//        profile image get
        String uid = firebaseUser.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_user/"+uid+"profile.jpg");

        if (firebaseUser != null){
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).placeholder(R.drawable.progress_animation).error(R.drawable.load_error).into(user_photo);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.this, "Image get fail", Toast.LENGTH_SHORT).show();
                    Log.e("img_error", "onFailure: ", e);
                }
            });

        }

//      navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.main_menu_logout) {
                    Intent intent=new Intent(HomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                    firebaseAuth.signOut();
                }
                return true;
            }
        });


        // notification view
        notificationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationView.class);
                startActivity(intent);
            }
        });



        // ride share btn click
        ride_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RideShare.class);
                startActivity(intent);
            }
        });


        // food delivery activity
        food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FoodDeliveryActivity.class);
                startActivity(intent);
            }
        });


        // medicine Delivery activity
        medicine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MedicineDeliveryActivity.class);
                startActivity(intent);
            }
        });


        // percel delivery activity
        parcel_delivery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PercelDeliveryActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setUserData(String name, int level, String id, String address, String startDate) {
        profile_name.setText(name);
        profile_level.setText(String.valueOf(level));
        profile_id.setText(id);
        user_address.setText(address);
        member_start_date.setText("User Since: "+ startDate);


        Log.d("get value", name + level + address);
    }

    private void getUserData(FirebaseUser user) {
        if (user == null){
            Toast.makeText(this, "Something went wrong! User not available.", Toast.LENGTH_SHORT).show();
        } else {
            String userId = user.getUid();
            DocumentReference documentReference = firestore.collection("user_profile").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    name = value.getString("name");
                    level = Math.toIntExact(value.getLong("user_role"));
                    id = userId;
                    address = value.getString("address");
                    startDate = value.getString("create_date");
                    setUserData(name, level, id, address, startDate);
                }
            });

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING")
                .setMessage("Are you sure you want to close the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(101);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}