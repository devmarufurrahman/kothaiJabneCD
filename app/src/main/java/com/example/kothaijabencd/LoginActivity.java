package com.example.kothaijabencd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kothaijabencd.utils.ReadWriterRiderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView SignUpBtn, forgotPassword;
    EditText emailTv, passTv;
    Button loginBtn;
    String email, pass;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        loginBtn = findViewById(R.id.loginBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        emailTv = findViewById(R.id.loginEmail);
        passTv = findViewById(R.id.loginPass);
        forgotPassword = findViewById(R.id.forgotPassword);
        firestore = FirebaseFirestore.getInstance();



        // all permission required
        permision();


        //login process event
        loginBtn.setOnClickListener(v -> {
           email = emailTv.getText().toString();
           pass = passTv.getText().toString();

           if (email.equals("")){
               emailTv.setError("email can't be blank");
               emailTv.requestFocus();
           } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
               emailTv.setError("please provide a valid email");
               emailTv.requestFocus();
           } else if (pass.equals("")) {
               passTv.setError("password can't be blank");
               passTv.requestFocus();
           } else {
               progressBar.setVisibility(View.VISIBLE);
               loginMethod(email,pass);
           }

        });


        // forgotPassword method here
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String mail = resetMail.getText().toString().trim();
                       firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(LoginActivity.this, "Please check your email, Reset Link", Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(LoginActivity.this, "Error!reset link not sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passwordResetDialog.create().cancel();
                    }
                });
                passwordResetDialog.create().show();
            }
        });



        // signup process event
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PassengerOrDriver.class);
                startActivity(intent);
            }
        });
    }

    private void loginMethod(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUser = firebaseAuth.getCurrentUser();
                    progressBar.setVisibility(View.GONE);

//                    email verify check
                    if (firebaseUser.isEmailVerified()){

                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                            String token = task1.getResult();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("userToken",token);
                            DocumentReference documentReference = firestore.collection("user_profile").document(firebaseUser.getUid());

                            SharedPreferences sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userToken", token);
                            editor.apply();

                            documentReference.update(updates).addOnSuccessListener(unused -> {

                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            });
                        });

                    } else {
                     firebaseUser.sendEmailVerification();
                     firebaseAuth.signOut();
                     showAlertDialog();
                    }

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(LoginActivity.this, "Users dose not exist or is no longer valid. Please register again", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Wrong password. Check and re-enter.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Log.e("login error", e.getMessage() );
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without verification");

//        open email to verify
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Intent intent = new Intent(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_APP_EMAIL);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void permision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.FOREGROUND_SERVICE}, 1);
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.FOREGROUND_SERVICE}, 1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
}