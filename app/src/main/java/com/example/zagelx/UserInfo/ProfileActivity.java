package com.example.zagelx.UserInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.DashboardPackage.MerchantDashboardActivity;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.Users;
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
    private Spinner newLocation, newType, newAdmin;
    private ArrayAdapter<CharSequence> adapter;
    private DatePicker newDate;
    private Button saveButton, cancelButton, setAccurateLocation;
    private boolean editImageFlag = false;
    private ProgressBar progressBar;


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

        progressBar = findViewById(R.id.progressBar);

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
        setAccurateLocation = findViewById(R.id.set_current_location_on_map);
        editUserBirthdate = findViewById(R.id.date_edit);
        editUserType = findViewById(R.id.type_edit);

        newEmail = findViewById(R.id.new_email);
        emailLineSeparator = findViewById(R.id.email_line_separator);
        newLocation = findViewById(R.id.new_location);
        newAdmin = findViewById(R.id.new_admin);

        newLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {
                String AreaName = newLocation.getSelectedItem().toString();

                switch (AreaName){
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                ProfileActivity.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newAdmin.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });
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
        setAccurateLocation.setOnClickListener(this);
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
                if (currentUser.isAccurateLocation())
                    setAccurateLocation.setVisibility(View.GONE);
                else
                    setAccurateLocation.setVisibility(View.VISIBLE);
                userPhone.setText(currentUser.getMobileNumber());
                userEmail.setText(currentUser.getEmail());
                String date = currentUser.getBirthDate().toString();
                userDate.setText(date);
                userType.setText(currentUser.getMode());
                String fullLocation = currentUser.getLocationInfoForUser().getuAdminArea()+", "+currentUser.getLocationInfoForUser().getuSubAdmin();
                userLocation.setText(fullLocation);
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
                newAdmin.setVisibility(View.VISIBLE);
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
                if (currentUser.getMode().equals("Merchant")){
                    Intent i = new Intent(ProfileActivity.this, MerchantDashboardActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }
                else if (currentUser.getMode().equals("Delivery Delegate")){
                    Intent i = new Intent(ProfileActivity.this, DelegateDashboardActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }

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
                break;
            case R.id.set_current_location_on_map:
                progressBar.setVisibility(View.VISIBLE);
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent intent1 = new Intent(ProfileActivity.this, SetAcurrateLocationActivity.class);
                    startActivity(intent1);
                } else {
                    isGPSopened();
                }


        }
    }
    private void isGPSopened() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            progressBar.setVisibility(View.GONE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled) {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ProfileActivity.this.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
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
            usersDatabaseReference.child("locationInfoForUser").child("uAdminArea").setValue(newLocation.getSelectedItem().toString());
            usersDatabaseReference.child("locationInfoForUser").child("uSubAdmin").setValue(newAdmin.getSelectedItem().toString());
            usersDatabaseReference.child("accurateLocation").setValue(false);
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
        finish();
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
    @Override
    public void onBackPressed() {
        if (currentUser.getMode().equals("Merchant")){
            Intent i = new Intent(ProfileActivity.this, MerchantDashboardActivity.class);
            i.putExtra("Which_Activity", "SomethingElse");
            finish();
            startActivity(i);
        }
        else if (currentUser.getMode().equals("Delivery Delegate")){
            Intent i = new Intent(ProfileActivity.this, DelegateDashboardActivity.class);
            i.putExtra("Which_Activity", "SomethingElse");
            finish();
            startActivity(i);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentUser.getMode().equals("Merchant")){
                Intent i = new Intent(ProfileActivity.this, MerchantDashboardActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                finish();
                startActivity(i);
            }
            else if (currentUser.getMode().equals("Delivery Delegate")){
                Intent i = new Intent(ProfileActivity.this, DelegateDashboardActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                finish();
                startActivity(i);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
