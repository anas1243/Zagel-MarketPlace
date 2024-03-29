package com.example.zagelx.OrdersPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zagelx.Authentication.AfterRegisterUserInfo;
import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.DashboardPackage.MerchantDashboardActivity;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.LocationInfoForPackage;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Users;
import com.example.zagelx.Models.ZagelNumbers;
import com.example.zagelx.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;
import java.util.Locale;


public class AddOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddOrdersActivity";
    private static final int RC_PHOTO_PICKER = 2;
    private final int sdk = android.os.Build.VERSION.SDK_INT;

    private ImageView packageImage;
    private ImageView editPackageImage;

    private EditText packageNameET;
    private DatePicker deliveryDateDP;
    private EditText deliveryPriceET;
    private EditText endConsumerMobile, endConsumerName;
    private SwitchCompat isPrePaidSwitch, isSameSourceSwitch, isBreakableSwitch;

    private TextView vehicle;

    private Spinner packageSLocation, packageSAreaName, packageDLocation, packageDAreaName, packageWeight;
    ArrayAdapter<CharSequence> adapter;
    private TextView userSLocationLable, userSAreaNameLable, userDLocationLable, userDAreaNameLable;

    private EditText packageDescriptionET;
    private EditText packagePriceET;

    private EditText sourceET;
    private EditText destinationET;


    private Button AddOrderButton;

    private ImageButton icAny, icBus, icTrain, icCar, icMotorcycle, icNosNal;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference mUsersDatabaseReference, numbersDatabaseReference;

    private ValueEventListener mNumbersEventListener;
    private ZagelNumbers zagelNumbers;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPackagePhotoStorageReference;

    private FirebaseAuth mFirebaseAuth;

    private UploadTask uploadTask;
    private Uri selectedImageUri;

    private String merchantId, merchantMobile, merchantImageURL, getMerchantName, oImageUrl,
            oName, dPrice, RMobile, RName, oPrice, oVehicle, oDescription, oWeight, oSourceLocation, oSourceAdmin;

    private LocationInfoForPackage locationInfoForPackage;

    private boolean isBreakable, merchantVerification, isPrePaid;
    private BirthDate dDate;

    double destenationLatlng[];
    double sourceLatlng[];

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_orders_activity);


        locationInfoForPackage = new LocationInfoForPackage();

        packageImage = findViewById(R.id.package_image);
        editPackageImage = findViewById(R.id.edit_package_image);

        packageNameET = findViewById(R.id.package_name);
        deliveryPriceET = findViewById(R.id.delivery_price);
        endConsumerMobile = findViewById(R.id.end_consumer_mobile);
        endConsumerName = findViewById(R.id.end_consumer_name);


        isPrePaidSwitch = findViewById(R.id.pre_paid_switch);
        isBreakableSwitch = findViewById(R.id.breakable_switch);
        isSameSourceSwitch = findViewById(R.id.is_same_source_switch);
        packagePriceET = findViewById(R.id.package_price);
        deliveryDateDP = findViewById(R.id.delivery_date);
        vehicle = findViewById(R.id.vehicle_name);

        packageSLocation = findViewById(R.id.user_Slocation);
        packageSAreaName = findViewById(R.id.area_Sname);

        packageDLocation = findViewById(R.id.user_Dlocation);
        packageDAreaName = findViewById(R.id.area_Dname);

        packageWeight = findViewById(R.id.spinner_weight);


        userSLocationLable = findViewById(R.id.user_Slocation_lable);
        userSAreaNameLable = findViewById(R.id.area_Sname_lable);

        userDLocationLable = findViewById(R.id.user_Dlocation_lable);
        userDAreaNameLable = findViewById(R.id.area_Dname_lable);


        icBus = findViewById(R.id.ic_bus);
        icAny = findViewById(R.id.ic_any);
        icTrain = findViewById(R.id.ic_train);
        icCar = findViewById(R.id.ic_car);
        icMotorcycle = findViewById(R.id.ic_motorcycle);
        icNosNal = findViewById(R.id.ic_nos_na2l);

        packageDescriptionET = findViewById(R.id.package_description);

        sourceET = findViewById(R.id.source_txt_view);
        destinationET = findViewById(R.id.destination_txt_view);


        AddOrderButton = findViewById(R.id.button_yalla);

        //this is there when MapsOrdersActivity was first

//        Intent i = getIntent();
//        String previousActivity = i.getStringExtra("FROM_ACTIVITY");
//        if (previousActivity.equals("AddOrdersMapActivity")) {
//            packageSLocation.setVisibility(View.GONE);
//            packageSAreaName.setVisibility(View.GONE);
//            packageDLocation.setVisibility(View.GONE);
//            packageDAreaName.setVisibility(View.GONE);
//
//            userSLocationLable.setVisibility(View.GONE);
//            userSAreaNameLable.setVisibility(View.GONE);
//            userDLocationLable.setVisibility(View.GONE);
//            userDAreaNameLable.setVisibility(View.GONE);
//
////            destenationLatlng = (double[]) i.getSerializableExtra("destenationLatlng");
////            sourceLatlng = (double[]) i.getSerializableExtra("sourceLatlng");
////            locationInfoForPackage.setsLat(sourceLatlng[0]+"");
////            locationInfoForPackage.setsLng(sourceLatlng[1]+"");
////            locationInfoForPackage.setdLat(destenationLatlng[0]+"");
////            locationInfoForPackage.setdLng(destenationLatlng[1]+"");
//
//            destenationLatlng = (double[]) i.getSerializableExtra("destenationLatlng");
//            sourceLatlng = (double[]) i.getSerializableExtra("sourceLatlng");
//
//            Log.e(TAG, "onCreate: " + latlngToAddress(sourceLatlng[0], sourceLatlng[1])
//                    + latlngToAddress(destenationLatlng[0], destenationLatlng[1]));
//
//            SetLocationInfo(sourceLatlng[0], sourceLatlng[1], "S");
//            SetLocationInfo(destenationLatlng[0], destenationLatlng[1], "D");
//
//
//            Log.e(TAG, "onCreate: currentOrderLocation: source is " + new LatLng(sourceLatlng[0]
//                    , sourceLatlng[1]) + " destination is " + new LatLng(destenationLatlng[0]
//                    , destenationLatlng[1]));
//
//        }

        isSameSourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    packageSLocation.setVisibility(View.GONE);
                    packageSAreaName.setVisibility(View.GONE);
                    userSLocationLable.setVisibility(View.GONE);
                    userSAreaNameLable.setVisibility(View.GONE);

                } else {
                    packageSLocation.setVisibility(View.VISIBLE);
                    packageSAreaName.setVisibility(View.VISIBLE);
                    userSLocationLable.setVisibility(View.VISIBLE);
                    userSAreaNameLable.setVisibility(View.VISIBLE);

                }

            }
        });


        packageDLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                String AreaName = packageDLocation.getSelectedItem().toString();

                switch (AreaName) {
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageDAreaName.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        packageSLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                String AreaName = packageSLocation.getSelectedItem().toString();

                switch (AreaName) {
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                AddOrdersActivity.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        packageSAreaName.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        merchantId = mFirebaseAuth.getCurrentUser().getUid();

        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");
        numbersDatabaseReference = mFirebaseDatabase.getReference().child("ZagelNumbers");

        mPackagePhotoStorageReference = mFirebaseStorage.getReference().child("packages_photos");


        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(("Users")).child(merchantId);

        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                merchantImageURL = dataSnapshot.getValue(Users.class).getProfilePictureURL();
                getMerchantName = dataSnapshot.getValue(Users.class).getName();
                merchantVerification = dataSnapshot.getValue(Users.class).isVerified();
                merchantMobile = dataSnapshot.getValue(Users.class).getMobileNumber();
                oSourceLocation = dataSnapshot.getValue(Users.class).getLocationInfoForUser().getuAdminArea();
                oSourceAdmin = dataSnapshot.getValue(Users.class).getLocationInfoForUser().getuSubAdmin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        icAny.setOnClickListener(this);
        icBus.setOnClickListener(this);
        icTrain.setOnClickListener(this);
        icCar.setOnClickListener(this);
        icMotorcycle.setOnClickListener(this);
        icNosNal.setOnClickListener(this);
        packageImage.setOnClickListener(this);
        editPackageImage.setOnClickListener(this);
        AddOrderButton.setOnClickListener(this);

        isPrePaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isPrePaid = true;
                    packagePriceET.setVisibility(View.VISIBLE);
                } else {
                    isPrePaid = false;
                    packagePriceET.setVisibility(View.GONE);
                }

            }
        });

        isBreakableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    isBreakable = true;
                else
                    isBreakable = false;
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            selectedImageUri = data.getData();
            Glide.with(getApplicationContext()).load(selectedImageUri).into(packageImage);

        }
    }

    Address latlngToAddress(double LATITUDE, double LONGITUDE) {
        Address returnedAddress;
        Geocoder geocoder = new Geocoder(AddOrdersActivity.this, Locale.getDefault());
        Log.e(TAG, "getCompleteAddressString: " + LATITUDE + " " + LONGITUDE);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {
                returnedAddress = addresses.get(0);
                return returnedAddress;
            } else {
                Log.e(TAG, "No Address returned!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot get Address!" + e.getMessage());
            return null;
        }

    }

    public String getAddressListFromLatLong(double lat, double lng, String WhichLocation) {

        String adminName = "";
        Geocoder geocoder = new Geocoder(this);

        List<Address> list;
        try {
            list = geocoder.getFromLocation(lat, lng, 20);

            // 20 is no of address you want to fetch near by the given lat-long

            for (Address address : list) {
                if (address.getAdminArea() != null) {
                    Log.e(TAG, "getAddressListFromLatLong: " + address.getAdminArea());
                    return address.getAdminArea();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return adminName;
    }

    public void SetLocationInfo(double lat, double lng, String whichLocation) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> list;
        try {
            list = geocoder.getFromLocation(lat, lng, 20);

            // 20 is no of address you want to fetch near by the given lat-long

            for (Address address : list) {
                if (address.getAdminArea() != null && address.getSubAdminArea() != null
                        && address.getLocality() != null) {
                    if (whichLocation.equals("S")) {
                        locationInfoForPackage.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), address.getLocality());
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        locationInfoForPackage.setdLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), address.getLocality());
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    }
                }
            }

            for (Address address : list) {
                if (address.getAdminArea() != null && address.getSubAdminArea() != null) {
                    if (whichLocation.equals("S")) {
                        locationInfoForPackage.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        locationInfoForPackage.setdLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    }
                }
            }
            for (Address address : list) {
                if (address.getAdminArea() != null) {
                    if (whichLocation.equals("S")) {
                        locationInfoForPackage.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , "", "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        locationInfoForPackage.setdLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , "", "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void validateTheUser() {
        oName = packageNameET.getText().toString().trim();
        oPrice = packagePriceET.getText().toString().trim();
        dPrice = deliveryPriceET.getText().toString().trim();
        RMobile = endConsumerMobile.getText().toString().trim();
        RName = endConsumerName.getText().toString().trim();
        oVehicle = vehicle.getText().toString().trim();
        oDescription = packageDescriptionET.getText().toString().trim();
        oWeight = packageWeight.getSelectedItem().toString();
        dDate = new BirthDate(deliveryDateDP.getYear(),
                deliveryDateDP.getMonth() + 1, deliveryDateDP.getDayOfMonth());
//        if (uName.equals("")) {
//            //Snackbar.make(findViewById(R.id.scroll_view), "Enter your name", Snackbar.LENGTH_LONG).show();
//            userName.setError("Enter your name");
//            userName.requestFocus();
//            return;
//        } else if (uEmail.equals("") || !Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
//            userEmail.setError("Enter a valid email address");
//            userEmail.requestFocus();
//            return;
//        } else if (filePath == null) {
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(R.id.scroll_view), "Choose your photo", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        } else {
//            uploadOrderImageAndProceed();
//        }
        uploadOrderImageAndProceed();
    }

    private void uploadOrderImageAndProceed() {
        if (selectedImageUri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference photoRef =
                    mPackagePhotoStorageReference.child(merchantId + System.currentTimeMillis());
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                Log.e(TAG, "uploadOrderImageAndProceed: ", e.fillInStackTrace());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            uploadTask = photoRef.putBytes(data);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddOrdersActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            oImageUrl = downloadUri.toString();

                            Log.e(TAG, "validateTheUser: the pp url is " + oImageUrl);


                            if (packageSLocation.getVisibility() == View.VISIBLE) {

                                locationInfoForPackage.setsAdminArea(packageSLocation.getSelectedItem().toString());
                                locationInfoForPackage.setsSubAdmin(packageSAreaName.getSelectedItem().toString());

                                locationInfoForPackage.setdAdminArea(packageDLocation.getSelectedItem().toString());
                                locationInfoForPackage.setdSubAdmin(packageDAreaName.getSelectedItem().toString());
                            }else if (packageSLocation.getVisibility() == View.GONE){
                                locationInfoForPackage.setsAdminArea(oSourceLocation);
                                locationInfoForPackage.setsSubAdmin(oSourceAdmin);

                                locationInfoForPackage.setdAdminArea(packageDLocation.getSelectedItem().toString());
                                locationInfoForPackage.setdSubAdmin(packageDAreaName.getSelectedItem().toString());
                            }
                            String orderId = System.currentTimeMillis() + merchantId + RMobile;

                            Orders order = new Orders(orderId, merchantId,merchantMobile, merchantImageURL, getMerchantName, oName, oWeight, oImageUrl
                                    , oDescription, oPrice
                                    , isPrePaid, isBreakable,merchantVerification, dDate,
                                    dPrice, oVehicle, RMobile, RName
                                    , locationInfoForPackage, "New");

                            mOrdersDatabaseReference.child(orderId).setValue(order);

                            mNumbersEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    zagelNumbers = dataSnapshot.getValue(ZagelNumbers.class);
                                    numbersDatabaseReference.child("noOfOrders").setValue(zagelNumbers.getNoOfOrders()+1);
                                    Toast.makeText(AddOrdersActivity.this, "your order has been add!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddOrdersActivity.this, MerchantDashboardActivity.class);
                                    i.putExtra("Which_Activity", "OtherActivity");
                                    finish();
                                    startActivity(i);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };

                            numbersDatabaseReference
                                    .addListenerForSingleValueEvent(mNumbersEventListener);


                        } else {
                            Toast.makeText(AddOrdersActivity.this, "cant upload package image please try again!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_bus:
                clearVehicleOptionsColor();
                vehicle.setText("Bus");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icBus.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_bus_yellow));
                } else {
                    icBus.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_bus_yellow));
                }
                break;
            case R.id.ic_any:
                clearVehicleOptionsColor();
                vehicle.setText("Any");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icAny.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_any_yellow));
                } else {
                    icAny.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_any_yellow));
                }
                break;
            case R.id.ic_train:
                clearVehicleOptionsColor();
                vehicle.setText("Train");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icTrain.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_train_yellow));
                } else {
                    icTrain.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_train_yellow));
                }
                break;
            case R.id.ic_motorcycle:
                clearVehicleOptionsColor();
                vehicle.setText("MotorCycle");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icMotorcycle.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_motorcycle_yellow));
                } else {
                    icMotorcycle.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_motorcycle_yellow));
                }
                break;
            case R.id.ic_car:
                clearVehicleOptionsColor();
                vehicle.setText("Car");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icCar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_car_yellow));
                } else {
                    icCar.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_car_yellow));
                }
                break;
            case R.id.ic_nos_na2l:
                clearVehicleOptionsColor();
                vehicle.setText("Nos Na2l");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icNosNal.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_nos_na2l_yellow));
                } else {
                    icNosNal.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_nos_na2l_yellow));
                }
                break;
            case R.id.edit_package_image:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

                break;
            case R.id.package_image:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/jpeg");
                intent1.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent1, "Complete action using"), RC_PHOTO_PICKER);

                break;
            case R.id.button_yalla:
                validateTheUser();
                break;
        }
    }

    public void clearVehicleOptionsColor() {
        editPackageImage.setOnClickListener(this);
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            icBus.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_bus));
            icAny.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_any));
            icTrain.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_train));
            icCar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_car));
            icMotorcycle.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_motorcycle));
            icNosNal.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_nos_na2l));

        } else {
            icBus.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_bus));
            icAny.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_any));
            icTrain.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_train));
            icCar.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_car));
            icMotorcycle.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_motorcycle));
            icNosNal.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_nos_na2l));

        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
