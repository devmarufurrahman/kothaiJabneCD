package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kothaijabencd.databinding.ActivityRiderRegBinding;
import com.example.kothaijabencd.utils.ReadWriteUserDetails;
import com.example.kothaijabencd.utils.ReadWriterRiderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RiderReg extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    ActivityRiderRegBinding binding;
    ArrayList<String> religion,  gender, transport;
    int day, month, year, myday, myMonth, myYear, user_role = 0;
    String religion_ref= "", gender_ref= "", transport_ref= "", birth_date = "", encodedImage = "", today, uuid;
    Bitmap bitmap;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    ArrayList<Pair<Uri, String>> imageUris = new ArrayList<>();
    Dialog dialog;
    Uri drivingImg, slipImg, nidImg, profileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRiderRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // id selector
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_rider");
//        riderName = findViewById(R.id.riderName);
//        riderAddress = findViewById(R.id.riderAddress);
//        riderOccupation = findViewById(R.id.riderOccupation);
//        riderContact = findViewById(R.id.riderContact);
//        riderBikeInfo = findViewById(R.id.riderBikeInfo);
//        date_birthRider = findViewById(R.id.date_birthRider);
//        binding.selectGenderRider = findViewById(R.id.binding.selectGenderRider_rider);
//        binding.selectReligionRider = findViewById(R.id.binding.selectReligionRider_rider);
//        SignUpBtn = findViewById(R.id.SignUpBtn);
//        binding.selectTransport = findViewById(R.id.binding.selectTransport);
//        riderDl = findViewById(R.id.riderDl);
//        riderBikeSlip = findViewById(R.id.riderBikeSlip);
//        riderNid = findViewById(R.id.riderNid);
//        riderProfilePhoto = findViewById(R.id.riderProfilePhoto);
//        binding.bikeLayout = findViewById(R.id.binding.bikeLayout);


        // birth day selection
        binding.dateBirthRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dateBirthRider.setError(null);
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RiderReg.this, RiderReg.this,year, month,day);
                datePickerDialog.show();
            }
        });

        //gender selection
        gender=new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");

        binding.selectGenderRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(RiderReg.this);
                dialog.setContentView(R.layout.custom_spinner_layout_2);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(RiderReg.this, android.R.layout.simple_list_item_1,gender);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        String item  = adapter.getItem(position);
                        switch (Objects.requireNonNull(item)){
                            case "Male" :
                                gender_ref = "Male";
                                binding.selectGenderRider.setText(item);
                                binding.selectGenderRider.setError(null);
                                dialog.dismiss();
                                break;
                            case "Female" :
                                gender_ref = "Female";
                                binding.selectGenderRider.setText(item);
                                binding.selectGenderRider.setError(null);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                gender_ref = "Other";
                                binding.selectGenderRider.setText(item);
                                binding.selectGenderRider.setError(null);
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

        binding.selectReligionRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.selectReligionRider.setError(null);
                dialog = new Dialog(RiderReg.this);
                dialog.setContentView(R.layout.custom_spinner_layout_2);
                Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(RiderReg.this, android.R.layout.simple_list_item_1,religion);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        String item  = adapter.getItem(position);
                        switch (Objects.requireNonNull(item)){
                            case "Islam" :
                                religion_ref = "Islam";
                                binding.selectReligionRider.setText(item);
                                dialog.dismiss();
                                break;
                            case "Hinduism" :
                                religion_ref = "Hinduism";
                                binding.selectReligionRider.setText(item);
                                dialog.dismiss();
                                break;
                            case "Christianity" :
                                religion_ref = "Christianity";
                                binding.selectReligionRider.setText(item);
                                dialog.dismiss();
                                break;
                            case "Buddhism" :
                                religion_ref = "Buddhism";
                                binding.selectReligionRider.setText(item);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                religion_ref = "Other";
                                binding.selectReligionRider.setText(item);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });


        //transport selection
        transport=new ArrayList<>();
        transport.add("Bike");
        transport.add("Bicycle");
        transport.add("Others");

        binding.selectTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(RiderReg.this);
                dialog.setContentView(R.layout.custom_spinner_layout_2);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(RiderReg.this, android.R.layout.simple_list_item_1,transport);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        String item  = adapter.getItem(position);
                        switch (Objects.requireNonNull(item)){
                            case "Bike" :
                                transport_ref = "Bike";
                                binding.selectTransport.setText(item);
                                binding.selectTransport.setError(null);
                                binding.bikeLayout.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                                break;
                            case "Bicycle" :
                                transport_ref = "Bicycle";
                                binding.selectTransport.setText(item);
                                binding.selectTransport.setError(null);
                                binding.bikeLayout.setVisibility(View.GONE);
                                dialog.dismiss();
                                break;
                            case "Others" :
                                transport_ref = "Others";
                                binding.selectTransport.setText(item);
                                binding.selectTransport.setError(null);
                                binding.bikeLayout.setVisibility(View.GONE);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });


        // select driving licence
        binding.riderDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageSelection(1);

            }
        });

        // select Slip
        binding.riderBikeSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageSelection(2);

            }
        });

        // select nid
        binding.riderNid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageSelection(3);

            }
        });

        // select profile photo
        binding.riderProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageSelection(4);

            }
        });


        // sign up rider
        binding.SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRiderAccount();
            }
        });


    }

    private void createRiderAccount() {
        String name = binding.riderName.getText().toString();
        String contact  = binding.riderContact.getText().toString().trim();
        String address  = binding.riderAddress.getText().toString();
        String occupation  = binding.riderOccupation.getText().toString();
        String email  = binding.riderEmail.getText().toString().trim();
        String pass  = binding.riderPass.getText().toString().trim();
        String passC  = binding.riderPassC.getText().toString();
        String bikeInfo = binding.riderBikeInfo.getText().toString();


        if (name.equals("")){
            binding.riderName.setError("Name can't be blank");
            binding.riderName.requestFocus();
        } else if (birth_date.equals("")) {
            Toast.makeText(this, "Birthday can't be blank", Toast.LENGTH_SHORT).show();
            binding.dateBirthRider.setError("Birthday can't be blank");
        } else if (TextUtils.isEmpty(contact)) {
            binding.riderContact.setError("Contact can't be blank");
            binding.riderContact.requestFocus();
        } else if (contact.length() != 11) {
            binding.riderContact.setError("Contact is not valid");
            binding.riderContact.requestFocus();
        } else if (email.equals("")) {
            binding.riderEmail.setError("Email can't be blank");
            binding.riderEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.riderEmail.setError("Email can't valid");
            binding.riderEmail.requestFocus();
        } else if (pass.equals("") && pass.length() >= 6) {
            binding.riderPass.setError("Password min char 6");
            binding.riderPass.requestFocus();
        } else if (passC == pass) {
            binding.riderPassC.setError("Password not matching");
            binding.riderPassC.requestFocus();
        }  else if (address.equals("")) {
            binding.riderAddress.setError("Address can't be blank");
            binding.riderAddress.requestFocus();
        } else if (occupation.equals("")) {
            binding.riderOccupation.setError("Occupation can't be blank");
            binding.riderOccupation.requestFocus();
        } else if (gender_ref.equals("")) {
            Toast.makeText(this, "Gender can't be blank", Toast.LENGTH_SHORT).show();
            binding.selectGenderRider.setError("Gender can't be blank");
        } else if (religion_ref.equals("")) {
            Toast.makeText(this, "Religion can't be blank", Toast.LENGTH_SHORT).show();
            binding.selectReligionRider.setError("Religion can't be blank");
        } else if (transport_ref.equals("")) {
            Toast.makeText(this, "Transport can't be blank", Toast.LENGTH_SHORT).show();
            binding.selectTransport.setError("Transport can't be blank");
        } else if (nidImg.equals("")) {
            Toast.makeText(this, "Nid can't be blank", Toast.LENGTH_SHORT).show();
        } else if (profileImg.equals("")) {
            Toast.makeText(this, "Image can't be blank", Toast.LENGTH_SHORT).show();
        } else {
//            progressbar.setVisibility(View.VISIBLE);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate date = LocalDate.now();
                today = date.toString();
                Log.d("Current Date (update)", today);
            } else {
                Date currentDate = new Date();

                // Print the current date in the default format (yyyy-MM-dd)
                SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                today = defaultFormat.format(currentDate);
                Log.d("Current Date (Default)", today);
            }

            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RiderReg.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        uuid = firebaseUser.getUid();

                        if (transport_ref.equals("Bike")){
                            user_role = 2;
                        } else {
                            user_role = 3;
                        }
//                       user data save in firestore
                        ReadWriterRiderDetails writerRiderDetails = new ReadWriterRiderDetails(name,birth_date,contact,address,occupation,gender_ref,religion_ref,today,transport_ref,bikeInfo,user_role,2);
                        DocumentReference documentReference = firestore.collection("user_rider").document(uuid);

                        documentReference.set(writerRiderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RiderReg.this, "Registration Successful Submitted", Toast.LENGTH_SHORT).show();
                                Log.d("create uuid", "onSuccess: user is "+ uuid);

//                               save images
                                if (!nidImg.equals("") && !profileImg.equals("")){

                                    for (Pair<Uri, String> pair : imageUris){
                                        Uri imgUri = pair.first;
                                        String name = pair.second;
                                        StorageReference fileReference  = storageReference.child(uuid + name +"." +
                                                getFileExtensions(imgUri));
                                        fileReference.putFile(imgUri);

//                                     open login system
                                        Intent loginIntent = new Intent(RiderReg.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                }


//                        send verification email
                                firebaseUser.sendEmailVerification();
//                                progressbar.setVisibility(View.GONE);


                            }

                        });


                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            binding.riderEmail.setError("Your email is invalid or already in use. Kindly re-enter");
                            binding.riderEmail.requestFocus();
//                            progressbar.setVisibility(View.GONE);
                        } catch (FirebaseAuthUserCollisionException e){
                            binding.riderEmail.setError("Your email is already registered. Use another email");
                            binding.riderEmail.requestFocus();
//                            progressbar.setVisibility(View.GONE);
                        } catch (Exception e){
                            Log.e("User Auth", "Reg: " + e );
//                            progressbar.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }

    //    get image method
    private void imageSelection(int REQ_CODE) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(RiderReg.this)
                    .withPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);
                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                            Toast.makeText(RiderReg.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }


                    }).check();
        } else {

            Dexter.withActivity(RiderReg.this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);

                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                            Toast.makeText(RiderReg.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }
    

    // file extensions get method
    private String getFileExtensions(Uri filepath) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(filepath));
    };
    

    // set image method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK){

            assert data != null;
            drivingImg=data.getData();
            if ("image/jpeg".equals(getContentResolver().getType(drivingImg))) {
                imageUris.add(new Pair<>(drivingImg,"DL"));
                try {

                    InputStream inputStream = getContentResolver().openInputStream(drivingImg);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.riderDl.setImageBitmap(bitmap);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  else {
                Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode==2 && resultCode==RESULT_OK) {

            assert data != null;
            slipImg=data.getData();
            if ("image/jpeg".equals(getContentResolver().getType(slipImg))) {
                imageUris.add(new Pair<>(slipImg,"slip"));
                try {

                    InputStream inputStream = getContentResolver().openInputStream(slipImg);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.riderBikeSlip.setImageBitmap(bitmap);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  else {
                Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode==3 && resultCode==RESULT_OK) {

            assert data != null;
            nidImg=data.getData();
            if ("image/jpeg".equals(getContentResolver().getType(nidImg))) {
                imageUris.add(new Pair<>(nidImg,"nid"));
                try {

                    InputStream inputStream = getContentResolver().openInputStream(nidImg);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.riderNid.setImageBitmap(bitmap);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  else {
                Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode==4 && resultCode==RESULT_OK) {

            assert data != null;
            profileImg=data.getData();
            if ("image/jpeg".equals(getContentResolver().getType(profileImg))) {
                imageUris.add(new Pair<>(profileImg,"profile"));
                try {

                    InputStream inputStream = getContentResolver().openInputStream(profileImg);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.riderProfilePhoto.setImageBitmap(bitmap);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  else {
                Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }





    //    set date method
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        birth_date = (myday + "/" + (myMonth+1) + "/" + myYear);
        binding.dateBirthRider.setText(birth_date);
    }
}