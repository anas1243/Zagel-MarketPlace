package com.example.zagelx.UserInfo;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private static final int RC_PHOTO_PICKER = 2;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;
    private DatabaseReference ordersDatabaseReference;
    private DatabaseReference tripsDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference ProfileImageReference;
    private FirebaseUser user;
    private ValueEventListener mValueEventListener;
    private Users currentUser;
    private TextView userName, userPhone, userEmail, userDate, userType, userLocation, userVerification;
    private ImageView userImage;
    private ImageView editUserImageIcon, editUserName, editUserEmail, editUserPhone, editUserLocation, editUserBirthdate, editUserType, editUserVerification;
    private EditText newEmail;
    private View emailLineSeparator;
    private Spinner newLocation, newType;
    private DatePicker newDate;
    private Button saveButton, cancelButton;
    private boolean editImageFlag = false;


    private UploadTask uploadTask;
    private Uri filePath = null;
    private String userPhotoUrlVar;
    private boolean userVerificationStatus;

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = firebaseAuth.getCurrentUser();
        usersDatabaseReference = firebaseDatabase.getReference().child("Users").child(user.getUid());
        ordersDatabaseReference = firebaseDatabase.getReference().child("Orders");
        tripsDatabaseReference = firebaseDatabase.getReference().child("Trips");
        ProfileImageReference = storage.getReference().child("profile_images/"
                + "ProfilePic-->" + user.getUid());

        userName = findViewById(R.id.user_name);
        userVerification = findViewById(R.id.user_verification);
        userPhone = findViewById(R.id.user_mobile);
        userEmail = findViewById(R.id.user_email);
        userDate = findViewById(R.id.user_birthDate);
        userType = findViewById(R.id.user_acc_type);
        userLocation = findViewById(R.id.user_Location);
        userImage = findViewById(R.id.iv_profile_image);
        editUserImageIcon = findViewById(R.id.edit_profile_image);

        editUserName = findViewById(R.id.name_edit);
        editUserVerification = findViewById(R.id.verification_edit);
        editUserEmail = findViewById(R.id.email_edit);
        editUserPhone = findViewById(R.id.phone_edit);
        editUserLocation = findViewById(R.id.location_edit);
        editUserBirthdate = findViewById(R.id.date_edit);
        editUserType = findViewById(R.id.type_edit);

        newEmail = findViewById(R.id.new_email);
        emailLineSeparator = findViewById(R.id.email_line_separator);
        newLocation = findViewById(R.id.new_location);
        newType = findViewById(R.id.new_type);
        newDate = findViewById(R.id.new_date);
        newDate.updateDate(1995, 0, 1);

        saveButton = findViewById(R.id.button_save_edit);
        cancelButton = findViewById(R.id.button_cancel_edit);


        userImage.setOnClickListener(this);
        editUserImageIcon.setOnClickListener(this);
        editUserName.setOnClickListener(this);
        editUserEmail.setOnClickListener(this);
        editUserPhone.setOnClickListener(this);
        editUserLocation.setOnClickListener(this);
        editUserBirthdate.setOnClickListener(this);
        editUserType.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        editUserVerification.setOnClickListener(this);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(Users.class);
                userName.setText(currentUser.getName());
                if (currentUser.isVerified()) {
                    userVerification.setText("Verified! Thank you");
                    userVerificationStatus = true;
                } else {
                    userVerification.setText("Not verified!\nPlease verify your account");
                    userVerificationStatus = false;
                }
                userPhone.setText(currentUser.getMobileNumber());
                userEmail.setText(currentUser.getEmail());
                String date = currentUser.getBirthDate().toString();
                userDate.setText(date);
                userType.setText(currentUser.getMode());
                userLocation.setText(currentUser.getGovernment());
                if (editImageFlag) {
                    Glide.with(userImage)
                            .load(filePath)
                            .into(userImage);
                } else {
                    Glide.with(userImage)
                            .load(currentUser.getProfilePictureURL())
                            .into(userImage);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        usersDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);


    }


    @Override
    public void onClick(View v) {
        Snackbar snackbar;
        Intent intent;
        switch (v.getId()) {
            case R.id.name_edit:
                snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "يجب التواصل مع فريق عمل زاجل لطلب تغير الاسم", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.date_edit:
                editUserBirthdate.setVisibility(View.GONE);
                userDate.setVisibility(View.GONE);
                newDate.setVisibility(View.VISIBLE);
                break;
            case R.id.email_edit:
                editUserEmail.setVisibility(View.GONE);
                emailLineSeparator.setVisibility(View.GONE);
                userEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);

                break;
            case R.id.phone_edit:
                snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "يجب التواصل مع فريق عمل زاجل لطلب تغير رقم المحمول", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.type_edit:
                editUserType.setVisibility(View.GONE);
                userType.setVisibility(View.GONE);
                newType.setVisibility(View.VISIBLE);
                break;
            case R.id.location_edit:
                editUserLocation.setVisibility(View.GONE);
                userLocation.setVisibility(View.GONE);
                newLocation.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_profile_image:
                if (newEmail.getVisibility() == View.VISIBLE || newDate.getVisibility() == View.VISIBLE
                        || newLocation.getVisibility() == View.VISIBLE
                        || newType.getVisibility() == View.VISIBLE) {
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "Please save or cancel your changes first", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    saveButton.getParent().requestChildFocus(saveButton, saveButton);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
                break;
            case R.id.iv_profile_image:
                if (newEmail.getVisibility() == View.VISIBLE || newDate.getVisibility() == View.VISIBLE
                        || newLocation.getVisibility() == View.VISIBLE
                        || newType.getVisibility() == View.VISIBLE) {
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "Please save or cancel your changes first", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    saveButton.getParent().requestChildFocus(saveButton, saveButton);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
                break;
            case R.id.button_save_edit:
                if (newEmail.getVisibility() == View.VISIBLE || newDate.getVisibility() == View.VISIBLE
                        || newLocation.getVisibility() == View.VISIBLE
                        || newType.getVisibility() == View.VISIBLE || editImageFlag) {
                    editInfo();
                } else {
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "there is no changes", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            case R.id.button_cancel_edit:
                Intent i = new Intent(ProfileActivity.this, OrdersActivity.class);
                startActivity(i);
                break;
            case R.id.verification_edit:
                if (userVerificationStatus) {
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "you are already verified thank you!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "Please Complete the progress bar to be a verified user", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

        }
    }

    public void editInfo() {
        Snackbar snackbar;
        Intent intent;
        if (newEmail.getVisibility() == View.VISIBLE) {
            if (newEmail.getText().toString().equals("")
                    || !Patterns.EMAIL_ADDRESS.matcher(newEmail.getText().toString()).matches()) {
                newEmail.setError("Enter a valid email address");
                newEmail.requestFocus();
                return;
            }
            usersDatabaseReference.child("email").setValue(newEmail.getText().toString());
            snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "your Email has been changed!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        if (newDate.getVisibility() == View.VISIBLE) {

            usersDatabaseReference.child("birthDate").setValue(new BirthDate(newDate.getYear(),
                    newDate.getMonth() + 1, newDate.getDayOfMonth()));
            snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "your birthDate has been changed!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        if (newLocation.getVisibility() == View.VISIBLE) {
            usersDatabaseReference.child("government").setValue(newLocation.getSelectedItem().toString());
            snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "your location has been changed!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (newType.getVisibility() == View.VISIBLE) {
            usersDatabaseReference.child("mode").setValue(newType.getSelectedItem().toString());
            snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "your business has been changed!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (editImageFlag) {

            ProfileImageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Snackbar snackbar;
                    snackbar = Snackbar
                            .make(findViewById(R.id.scroll_view), "file deleted !", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    uploadProfileImageAndProceed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: did not delete file");
                }
            });
            return;

        }
        intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void uploadProfileImageAndProceed() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                Log.e(TAG, "uploadOrderImageAndProceed: ", e.fillInStackTrace());
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
                            Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            changeOrdersMerchantsURL();
                            changeTripsDelegatesURL();
                            Log.e(TAG, "validateTheUser: the pp url is " + userPhotoUrlVar);

                            usersDatabaseReference.child("profilePictureURL").setValue(userPhotoUrlVar);

                            Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(ProfileActivity.this, "cant upload package image please try again!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        }
    }

    public void changeOrdersMerchantsURL() {
        ordersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String orderID = item_snapshot.getKey();
                    if (orderID.contains(user.getUid())) {
                        ordersDatabaseReference.child(orderID).child("merchantImageURL").setValue(userPhotoUrlVar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeTripsDelegatesURL() {
        tripsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                    String tripID = item_snapshot.getKey();
                    if (tripID.contains(user.getUid())) {
                        tripsDatabaseReference.child(tripID).child("delegateImageURL").setValue(userPhotoUrlVar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Intent mdata = data;
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            editImageFlag = true;

            filePath = mdata.getData();
            Glide.with(getApplicationContext()).load(filePath).into(userImage);

        }
    }
}
