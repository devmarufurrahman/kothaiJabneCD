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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kothaijabencd.utils.ReadWriteUserDetails;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserReg extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    EditText userName, userAddress, userOccupation, userContact, userEmail, userPass, userPassC;
    Button date_birth, select_gender, select_religion, SignUpBtn;
    ShapeableImageView userNid, userProfilePhoto;
    ArrayList<String>  religion,  gender;
    int day, month, year, myday, myMonth, myYear, request_code;
    String religion_ref= "", gender_ref= "", birth_date = "", encodedNid = "", encodedProfile = "", today;
    Bitmap bitmap;
    Dialog dialog;
    ProgressBar progressbar;
    FirebaseFirestore firestore;
    String uuid ;
    StorageReference storageReference;
    ArrayList<Pair<Uri, String>> imageUris = new ArrayList<>();
    Uri nidUri, profileUri;


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
        userContact = findViewById(R.id.userContact);
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPass);
        userPassC = findViewById(R.id.userPassC);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        progressbar = findViewById(R.id.progressbar);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_user");


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
                                gender_ref = "Male";
                                select_gender.setText(item);
                                select_gender.setError(null);
                                dialog.dismiss();
                                break;
                            case "Female" :
                                gender_ref = "Female";
                                select_gender.setText(item);
                                select_gender.setError(null);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                gender_ref = "Other";
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
                                religion_ref = "Islam";
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Hinduism" :
                                religion_ref = "Hinduism";
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Christianity" :
                                religion_ref = "Christianity";
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Buddhism" :
                                religion_ref = "Buddhism";
                                select_religion.setText(item);
                                dialog.dismiss();
                                break;
                            case "Other" :
                                religion_ref = "Other";
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
                request_code = 1;
                imageSelection(request_code);
            }
        });

        // select profile photo
        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_code = 2;
                imageSelection(request_code);
            }
        });


        // sign up event
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });


    }



    //    get image method
    private void imageSelection(int REQ_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(UserReg.this)
                    .withPermission(Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/jpg");
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);
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
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);

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




    // create user method
    private void createUserAccount() {
        String name = userName.getText().toString();
        String contact  = userContact.getText().toString();
        String address  = userAddress.getText().toString();
        String occupation  = userOccupation.getText().toString();
        String email  = userEmail.getText().toString();
        String pass  = userPass.getText().toString();
        String passC  = userPassC.getText().toString();

        if (name.equals("")){
            userName.setError("Name can't be blank");
            userName.requestFocus();
        } else if (birth_date.equals("")) {
            Toast.makeText(this, "Birthday can't be blank", Toast.LENGTH_SHORT).show();
            date_birth.setError("Birthday can't be blank");
        } else if (TextUtils.isEmpty(contact)) {
            userContact.setError("Contact can't be blank");
            userContact.requestFocus();
        } else if (contact.length() != 11) {
            userContact.setError("Contact is not valid");
            userContact.requestFocus();
        } else if (email.equals("")) {
            userEmail.setError("Email can't be blank");
            userEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Email can't valid");
            userEmail.requestFocus();
        } else if (pass.equals("") && pass.length() >= 6) {
            userPass.setError("Password min char 6");
            userPass.requestFocus();
        } else if (passC == pass) {
            userPassC.setError("Password not matching");
            userPassC.requestFocus();
        }  else if (address.equals("")) {
            userAddress.setError("Address can't be blank");
            userAddress.requestFocus();
        } else if (occupation.equals("")) {
            userOccupation.setError("Occupation can't be blank");
            userOccupation.requestFocus();
        } else if (gender_ref.equals("")) {
            Toast.makeText(this, "Gender can't be blank", Toast.LENGTH_SHORT).show();
            select_gender.setError("Gender can't be blank");
        } else if (religion_ref.equals("")) {
            Toast.makeText(this, "Religion can't be blank", Toast.LENGTH_SHORT).show();
            select_religion.setError("Religion can't be blank");
        } else if (nidUri.equals("")) {
            Toast.makeText(this, "Nid can't be blank", Toast.LENGTH_SHORT).show();
        } else if (profileUri.equals("")) {
            Toast.makeText(this, "Image can't be blank", Toast.LENGTH_SHORT).show();
        } else {
            progressbar.setVisibility(View.VISIBLE);
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
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(UserReg.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       FirebaseUser firebaseUser = auth.getCurrentUser();
                       uuid = firebaseUser.getUid();

//                       user data save in firestore
                       ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name,birth_date,contact,address,occupation,gender_ref,religion_ref,today,4);
                       DocumentReference documentReference = firestore.collection("user_profile").document(uuid);

                       documentReference.set(writeUserDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(UserReg.this, "Registration Successful " + uuid, Toast.LENGTH_SHORT).show();
                               Log.d("create uuid", "onSuccess: user is "+ uuid);

//                               save images
                               if (!nidUri.equals("") && !profileUri.equals("")){

                                   for (Pair<Uri, String> pair : imageUris){
                                       Uri imgUri = pair.first;
                                       String name = pair.second;
                                       StorageReference fileReference  = storageReference.child(uuid + name +"." +
                                               getFileExtensions(imgUri));
                                       fileReference.putFile(imgUri);

//                                     open login system
                                       Intent loginIntent = new Intent(UserReg.this, LoginActivity.class);
                                       startActivity(loginIntent);
                                       finish();
                                   }
                               }


//                        send verification email
                               firebaseUser.sendEmailVerification();
                               progressbar.setVisibility(View.GONE);


                           }

                       });


                   } else {
                       try {
                           throw task.getException();
                       } catch (FirebaseAuthInvalidCredentialsException e){
                           userEmail.setError("Your email is invalid or already in use. Kindly re-enter");
                           userEmail.requestFocus();
                           progressbar.setVisibility(View.GONE);
                       } catch (FirebaseAuthUserCollisionException e){
                           userEmail.setError("Your email is already registered. Use another email");
                           userEmail.requestFocus();
                           progressbar.setVisibility(View.GONE);
                       } catch (Exception e){
                           Log.e("User Auth", "Reg: " + e );
                           progressbar.setVisibility(View.GONE);
                       }
                   }
                }
            });
        }

    }

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
             nidUri=data.getData();
             if ("image/jpeg".equals(getContentResolver().getType(nidUri))){
                 imageUris.add(new Pair<>(nidUri,"nid"));
                 try {

                     InputStream inputStream= getContentResolver().openInputStream(nidUri);
                     bitmap= BitmapFactory.decodeStream(inputStream);
                     userNid.setImageBitmap(bitmap);
//                encodeBitmapImage(1);

                 } catch (Exception ex){
                     ex.printStackTrace();
                 }
             } else {
                 Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
             }

        } else if (requestCode==2 && resultCode==RESULT_OK) {

            assert data != null;
             profileUri=data.getData();
            if ("image/jpeg".equals(getContentResolver().getType(profileUri))) {
                imageUris.add(new Pair<>(profileUri, "profile"));
                try {

                    InputStream inputStream = getContentResolver().openInputStream(profileUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    userProfilePhoto.setImageBitmap(bitmap);
//                encodeBitmapImage(2);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }  else {
                Toast.makeText(this, "Only select jpg format", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    // set date method
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        birth_date = (myday + "/" + (myMonth+1) + "/" + myYear);
        date_birth.setText(birth_date);
    }
}