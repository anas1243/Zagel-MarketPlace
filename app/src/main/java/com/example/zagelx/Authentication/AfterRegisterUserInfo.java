package com.example.zagelx.Authentication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.MerchantDashboardInsideActivity;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.FreeDelegateDetails;
import com.example.zagelx.Models.LocationInfoForUsers;
import com.example.zagelx.Models.MerchantDetails;
import com.example.zagelx.Models.PMDetails;
import com.example.zagelx.Models.ZagelNumbers;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AfterRegisterUserInfo extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    public static String TAG = "AfterRegisterUserinfo";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference, numbersDatabaseReference;
    private ValueEventListener mNumbersEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference ProfileImageReference;

    private EditText userName, userEmail, userNumberOfOrdersET, userBusinessIndustryET;
    private DatePicker userDate;
    private CircleImageView userPhotoUrl;
    private Button registerButton;
    private Spinner userGender, userType, userLocation, userAreaName, zagelTeamSpinner;

    private UploadTask uploadTask;
    private Uri filePath = null;

    private FirebaseUser user;

    private String uName, uEmail, uId, uNumberOfOrders, uBusinessIndustry, uGroup, uTeamMemberIfHeWas;
    private String userPhotoUrlVar;

    String uMobile, gender, type, governorate, subAdmin;

    private String newToken;

    private Bitmap bmp;
    private Users currentUser;
    private ZagelNumbers zagelNumbers;
    private LocationInfoForUsers locationInfoForUser;


    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user = firebaseAuth.getCurrentUser();
        usersDatabaseReference = firebaseDatabase.getReference().child("Users");
        numbersDatabaseReference = firebaseDatabase.getReference().child("ZagelNumbers");
        ProfileImageReference = storage.getReference().child("profile_images/" + "ProfilePic-->" + user.getUid());

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userDate = findViewById(R.id.user_date);
        userDate.updateDate(1995, 0, 1);
        userPhotoUrl = findViewById(R.id.iv_profile_image);
        userGender = findViewById(R.id.user_gender);
        userType = findViewById(R.id.user_type);
        zagelTeamSpinner = findViewById(R.id.zagel_team_spinner);
        userLocation = findViewById(R.id.user_location);
        userAreaName = findViewById(R.id.area_name);
        registerButton = findViewById(R.id.button_register);

        userNumberOfOrdersET = findViewById(R.id.user_number_of_orders);
        userBusinessIndustryET = findViewById(R.id.user_business_industry);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AfterRegisterUserInfo.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);

            }
        });

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String userTypeString = userType.getSelectedItem().toString();
                switch (userTypeString) {
                    case "Merchant":
                        userNumberOfOrdersET.setVisibility(View.VISIBLE);
                        userBusinessIndustryET.setVisibility(View.VISIBLE);
                        zagelTeamSpinner.setVisibility(View.GONE);
                        break;
                    case "Delivery Delegate":
                        userNumberOfOrdersET.setVisibility(View.GONE);
                        userBusinessIndustryET.setVisibility(View.GONE);
                        zagelTeamSpinner.setVisibility(View.GONE);
                        break;
                    case "Zagel Team":
                        userNumberOfOrdersET.setVisibility(View.GONE);
                        userBusinessIndustryET.setVisibility(View.GONE);

                        zagelTeamSpinner.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        zagelTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String zagelTeamString = zagelTeamSpinner.getSelectedItem().toString();
                switch (zagelTeamString) {
                    case "Alex PM":
                        uTeamMemberIfHeWas = "AlexPM";
                        break;
                    case "Alex Static Birds":
                        uTeamMemberIfHeWas = "AlexStaticBirds";
                        break;
                    case "Cairo PM":
                        uTeamMemberIfHeWas = "CairoPM";
                        break;
                    case "Cairo Static Birds":
                        uTeamMemberIfHeWas = "CairoStaticBirds";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                String AreaName = userLocation.getSelectedItem().toString();

                switch (AreaName) {
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                AfterRegisterUserInfo.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userAreaName.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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
        uNumberOfOrders = userNumberOfOrdersET.getText().toString().trim();
        uBusinessIndustry = userBusinessIndustryET.getText().toString().trim();
        if (uName.equals("")) {
            //Snackbar.make(findViewById(R.id.scroll_view), "Enter your name", Snackbar.LENGTH_LONG).show();
            userName.setError("Enter your name");
            userName.requestFocus();
        } else if (uEmail.equals("") || !Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
            userEmail.setError("Enter a valid email address");
            userEmail.requestFocus();
        } else if (filePath == null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.scroll_view), "Choose your photo", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (userType.getSelectedItem().toString().equals("Merchant")) {

            if (uBusinessIndustry.equals("")) {
                userBusinessIndustryET.setError("ادخل نوع تجارتك من فضلك");
                userBusinessIndustryET.requestFocus();
            } else if (uNumberOfOrders.equals("")) {
                userNumberOfOrdersET.setError("ادخل عدد شحناتك في اليوم تقريبا");
                userNumberOfOrdersET.requestFocus();
            } else {
                uploadProfileImageAndProceed();
            }
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
                            uMobile = user.getPhoneNumber();
                            gender = userGender.getSelectedItem().toString();
                            type = userType.getSelectedItem().toString();
                            governorate = userLocation.getSelectedItem().toString();
                            subAdmin = userAreaName.getSelectedItem().toString();
                            locationInfoForUser = new LocationInfoForUsers("", "", governorate, subAdmin, "");

                            switch (type) {
                                case "Merchant":
                                    if (governorate.equals("الإسكندرية")) {
                                        uGroup = "AlexMerchants";
                                    } else if (governorate.equals("القاهرة") || governorate.equals("الجيزة")) {
                                        uGroup = "CairoMerchants";
                                    }
                                    initiateNewUserObject(true);
                                    MerchantDetails merchantDetails = new MerchantDetails(uNumberOfOrders, uBusinessIndustry, 0, 0, 0, 0, 0);
                                    currentUser.setMerchantDetails(merchantDetails);
                                    break;
                                case "Delivery Delegate":
                                    if (governorate.equals("الإسكندرية")) {
                                        uGroup = "AlexFreeBirds";
                                    } else if (governorate.equals("القاهرة") || governorate.equals("الجيزة")) {
                                        uGroup = "CairoFreeBirds";
                                    }
                                    initiateNewUserObject(false);
                                    FreeDelegateDetails freeDelegateDetails = new FreeDelegateDetails(0, "", "", 0, 0);
                                    currentUser.setFreeDelegateDetails(freeDelegateDetails);
                                    break;
                                case "Zagel Team":
                                    uGroup = uTeamMemberIfHeWas;
                                    type = zagelTeamSpinner.getSelectedItem().toString();

                                    if ("AlexPM".equals(uGroup) || "CairoPM".equals(uGroup)) {
                                        initiateNewUserObject(false);
                                        PMDetails pmDetails = new PMDetails(0, 0);
                                        currentUser.setPmDetails(pmDetails);
                                    } else if ("AlexStaticBirds".equals(uGroup) || "CairoStaticBirds".equals(uGroup)) {
                                        initiateNewUserObject(false);
                                        FreeDelegateDetails staticDelegateDetails = new FreeDelegateDetails(0, "", "", 0, 0);
                                        currentUser.setFreeDelegateDetails(staticDelegateDetails);
                                    }

                                    break;

                            }

                            Log.e(TAG, "onComplete: "+currentUser.toString() );
                            mNumbersEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    zagelNumbers = dataSnapshot.getValue(ZagelNumbers.class);
                                    Toast.makeText(AfterRegisterUserInfo.this, "Resgistered!", Toast.LENGTH_SHORT).show();
                                    usersDatabaseReference.child(uId).setValue(currentUser);

                                    incrementZagelNumbers(uGroup);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };

                            numbersDatabaseReference
                                    .addListenerForSingleValueEvent(mNumbersEventListener);


                        } else {
                            Toast.makeText(AfterRegisterUserInfo.this, "cant upload package image please try again!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        }
    }
    public void initiateNewUserObject(boolean isMerchant){
        //if isMerchant is true make him verified as default
        currentUser = new Users(uId, uName, gender, uMobile,
                userPhotoUrlVar, new BirthDate(userDate.getYear(),
                userDate.getMonth() + 1, userDate.getDayOfMonth()),
                type, uGroup, uEmail, locationInfoForUser, false, isMerchant, false, newToken
        );

    }

    public void addUserToAGroup(final String uGroup) {

        FirebaseMessaging.getInstance().subscribeToTopic(uGroup)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "successfully add this user to " + uGroup + " group";

                        } else {
                            msg = "unsuccessfully add this user to " + uGroup + " group";
                        }
                        Log.e("AfterRegisterUserInfo", msg);
                    }
                });

    }

    public void incrementZagelNumbers(final String uGroup) {

        switch (uGroup) {
            case "AlexMerchants":
                try{
                    numbersDatabaseReference.child("noOfMerchantsInAlex")
                            .setValue(zagelNumbers.getNoOfMerchantsInAlex() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfMerchantsInAlex")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent i = new Intent(AfterRegisterUserInfo.this, MerchantDashboardInsideActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                startActivity(i);
                break;
            case "CairoMerchants":
                try{
                    numbersDatabaseReference.child("noOfMerchantsInCairo")
                            .setValue(zagelNumbers.getNoOfMerchantsInCairo() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfMerchantsInCairo")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);

                Intent intentToCairoMerchants = new Intent(AfterRegisterUserInfo.this, MerchantDashboardInsideActivity.class);
                intentToCairoMerchants.putExtra("Which_Activity", "SomethingElse");
                startActivity(intentToCairoMerchants);
                break;
            case "AlexFreeBirds":
                try{
                    numbersDatabaseReference.child("noOfFreeAlexBirds")
                            .setValue(zagelNumbers.getNoOfFreeAlexBirds() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfFreeAlexBirds")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToAlexBirds = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToAlexBirds);
                break;
            case "CairoFreeBirds":
                try{
                    numbersDatabaseReference.child("noOfFreeCairoBirds")
                            .setValue(zagelNumbers.getNoOfFreeCairoBirds() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfFreeCairoBirds")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToCairoBirds = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToCairoBirds);
                break;
            case "AlexStaticBirds":
                try{
                    numbersDatabaseReference.child("noOfStaticAlexBirds")
                            .setValue(zagelNumbers.getNoOfStaticAlexBirds() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfStaticAlexBirds")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToAlexStatic = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToAlexStatic);
                break;
            case "CairoStaticBirds":
                try{
                    numbersDatabaseReference.child("noOfStaticCairoBirds")
                            .setValue(zagelNumbers.getNoOfStaticCairoBirds() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfStaticCairoBirds")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToCairoStatic = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToCairoStatic);
                break;
            case "AlexPM":
                try{
                    numbersDatabaseReference.child("noOfAlexPMs")
                            .setValue(zagelNumbers.getNoOfAlexPMs() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfAlexPMs")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToAlexPm = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToAlexPm);
                break;
            case "CairoPM":
                try{
                    numbersDatabaseReference.child("noOfCairoPMs")
                            .setValue(zagelNumbers.getNoOfCairoPMs() + 1);
                }catch (Exception e){
                    numbersDatabaseReference.child("noOfCairoPMs")
                            .setValue(1);
                }
                addUserToAGroup(uGroup);
                Intent intentToCairoPm = new Intent(AfterRegisterUserInfo.this, NotVerifiedUser.class);
                startActivity(intentToCairoPm);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}