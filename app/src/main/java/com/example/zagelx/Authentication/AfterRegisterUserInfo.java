package com.example.zagelx.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.Users;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AfterRegisterUserInfo extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    public static String TAG = "AfterRegisterUserinfo";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference ProfileImageReference;

    private EditText userName, userEmail;
    private DatePicker userDate;
    private CircleImageView userPhotoUrl;
    private Button registerButton;
    private Spinner userGender;

    private UploadTask uploadTask;
    private Uri filePath = null;

    private FirebaseUser user;

    String uName, uEmail, uId;
    private String userPhotoUrlVar;

    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = firebaseAuth.getCurrentUser();
        usersDatabaseReference = firebaseDatabase.getReference().child("Users");
        ProfileImageReference = storage.getReference().child("profile_images/" + UUID.randomUUID().toString());

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userDate = findViewById(R.id.user_date);
        userPhotoUrl = findViewById(R.id.iv_profile_image);
        userGender = findViewById(R.id.user_gender);
        registerButton = findViewById(R.id.button_register);

        userPhotoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseUserImage();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateTheUser();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Glide.with(getApplicationContext()).load(filePath).into(userPhotoUrl);

        }
    }

    private void chooseUserImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void validateTheUser() {
        uName = userName.getText().toString().trim();
        uEmail = userEmail.getText().toString().trim();
        if (uName.equals("")) {
            //Snackbar.make(findViewById(R.id.scroll_view), "Enter your name", Snackbar.LENGTH_LONG).show();
            userName.setError("Enter your name");
            userName.requestFocus();
            return;
        } else if (uEmail.equals("") || !Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
            userEmail.setError("Enter a valid email address");
            userEmail.requestFocus();
            return;
        } else if (filePath == null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "Choose your photo", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            uploadProfileImageAndProceed();
        }
    }

    private void uploadProfileImageAndProceed() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }catch (IOException e){
                Log.e(TAG, "uploadOrderImageAndProceed: ", e.fillInStackTrace() );
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            uploadTask = ProfileImageReference.putBytes(data);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AfterRegisterUserInfo.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage((int) progress + "%" + "uploaded");
                        }
                    });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ProfileImageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            userPhotoUrlVar = downloadUri.toString();

                            Log.e(TAG, "validateTheUser: the pp url is " + userPhotoUrlVar);

                            uId = user.getUid();
                            String uMobile = user.getPhoneNumber();
                            String gender = userGender.getSelectedItem().toString();
                            Users currenUser = new Users(uId, uName, gender, uMobile,
                                    userPhotoUrlVar, new BirthDate(userDate.getYear(),
                                    userDate.getMonth()+1, userDate.getDayOfMonth()), false);

                            usersDatabaseReference.child(uId).setValue(currenUser);

                            Toast.makeText(AfterRegisterUserInfo.this, "Resgistered!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AfterRegisterUserInfo.this, OrdersActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(AfterRegisterUserInfo.this, "cant upload package image please try again!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        }
    }

}