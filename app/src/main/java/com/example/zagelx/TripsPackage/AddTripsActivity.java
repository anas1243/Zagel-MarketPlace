package com.example.zagelx.TripsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.LocationInfoForPackage;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.Models.ZagelNumbers;
import com.example.zagelx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTripsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddTripsActivity";
    private final int sdk = android.os.Build.VERSION.SDK_INT;

    private DatePicker routeDateDP;
    private EditText routePriceET;
    private SwitchCompat isPrePaidSwitch, isBreakableSwitch, isSameSourceSwitch;
    private TextView vehicle;

    private EditText routeNotesET;
    private EditText maxPrepaidPriceET;

    private EditText sourceET;
    private EditText destinationET;
    private EditText maxNoOrdersET;

    private Button AddTripButton;

    private ImageButton icAny, icBus, icTrain, icCar, icMotorcycle, icNosNal;

    private Spinner routeSLocation, routeSAreaName, routeDLocation, routeDAreaName;
    ArrayAdapter<CharSequence> adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference, numbersDatabaseReference, mUsersDatabaseReference;

    private ValueEventListener mNumbersEventListener;
    private ZagelNumbers zagelNumbers;

    private FirebaseAuth mFirebaseAuth;

    private String delegateId, delegateName, delegateImageURL, TPrice, oMaxPrice, tVehicle, tNotes, delegateLocation, delegateAdmin;
    private int maxNoOrders;
    private boolean isPrePaid = false;
    private boolean isBreakable = false;
    private boolean delegateVerification;
    private BirthDate dDate;
    private LocationInfoForPackage locationInfoForTrip;
    private TextView userSLocationLable, userSAreaNameLable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trips_activity);

        locationInfoForTrip = new LocationInfoForPackage();
        userSLocationLable = findViewById(R.id.user_Slocation_lable);
        userSAreaNameLable = findViewById(R.id.area_Sname_lable);

        routePriceET = findViewById(R.id.trip_price);
        isPrePaidSwitch = findViewById(R.id.pre_paid_switch);
        isBreakableSwitch = findViewById(R.id.breakable_switch);
        maxPrepaidPriceET = findViewById(R.id.maximum_price_prepaid);
        routeDateDP = findViewById(R.id.trip_date);
        maxNoOrdersET = findViewById(R.id.max_no_orders);

        vehicle = findViewById(R.id.vehicle_name);

        routeSLocation = findViewById(R.id.user_Slocation);
        routeSAreaName = findViewById(R.id.area_Sname);

        routeDLocation = findViewById(R.id.user_Dlocation);
        routeDAreaName = findViewById(R.id.area_Dname);

        icBus = findViewById(R.id.ic_bus);
        icAny = findViewById(R.id.ic_any);
        icTrain = findViewById(R.id.ic_train);
        icCar = findViewById(R.id.ic_car);
        icMotorcycle = findViewById(R.id.ic_motorcycle);
        icNosNal = findViewById(R.id.ic_nos_na2l);

        routeNotesET = findViewById(R.id.trip_description);

        sourceET = findViewById(R.id.source_txt_view);
        destinationET = findViewById(R.id.destination_txt_view);

        isSameSourceSwitch = findViewById(R.id.is_same_source_switch);


        AddTripButton = findViewById(R.id.button_yalla);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        delegateId = mFirebaseAuth.getCurrentUser().getUid();

        isSameSourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    routeSLocation.setVisibility(View.GONE);
                    routeSAreaName.setVisibility(View.GONE);
                    userSLocationLable.setVisibility(View.GONE);
                    userSAreaNameLable.setVisibility(View.GONE);

                } else {
                    routeSLocation.setVisibility(View.VISIBLE);
                    routeSAreaName.setVisibility(View.VISIBLE);
                    userSLocationLable.setVisibility(View.VISIBLE);
                    userSAreaNameLable.setVisibility(View.VISIBLE);

                }

            }
        });

        routeSLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                String AreaName = routeSLocation.getSelectedItem().toString();

                switch (AreaName) {
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeSAreaName.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        routeDLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id) {
                String AreaName = routeDLocation.getSelectedItem().toString();

                switch (AreaName) {
                    case "الإسكندرية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الإسكندرية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "القاهرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.القاهرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الإسماعيلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الإسماعيلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "السويس":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.السويس, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "أسوان":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.أسوان, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "بورسعيد":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.بورسعيد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الشرقية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الشرقية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "كفر الشيخ":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.كفر_الشيخ, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "أسيوط":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.أسيوط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "جنوب سيناء":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.جنوب_سيناء, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "مطروح":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.مطروح, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الأقصر":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الأقصر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الجيزة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الجيزة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الغربية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الغربية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "المنوفية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.المنوفية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "البحر الأحمر":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.البحر_الأحمر, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الدقهلية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الدقهلية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الفيوم":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الفيوم, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "المنيا":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.المنيا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "البحيرة":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.البحيرة, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "دمياط":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.دمياط, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "الوادي الجديد":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.الوادي_الجديد, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "قنا":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.قنا, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "بني سويف":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.بني_سويف, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "سوهاج":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.سوهاج, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;
                    case "القليوبية":
                        adapter = ArrayAdapter.createFromResource(
                                AddTripsActivity.this, R.array.القليوبية, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        routeDAreaName.setAdapter(adapter);
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");

        numbersDatabaseReference = mFirebaseDatabase.getReference().child("ZagelNumbers");


        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(("Users")).child(delegateId);

        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                delegateName = dataSnapshot.getValue(Users.class).getName();
                delegateImageURL = dataSnapshot.getValue(Users.class).getProfilePictureURL();
                delegateVerification = dataSnapshot.getValue(Users.class).isVerified();
                delegateLocation = dataSnapshot.getValue(Users.class).getLocationInfoForUser().getuAdminArea();
                delegateAdmin = dataSnapshot.getValue(Users.class).getLocationInfoForUser().getuSubAdmin();
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
        AddTripButton.setOnClickListener(this);

        isPrePaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isPrePaid = true;
                    maxPrepaidPriceET.setVisibility(View.VISIBLE);
                } else {
                    isPrePaid = false;
                    maxPrepaidPriceET.setVisibility(View.GONE);
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


    private void validateTheUser() {
        oMaxPrice = maxPrepaidPriceET.getText().toString().trim();
        TPrice = routePriceET.getText().toString().trim();
        tVehicle = vehicle.getText().toString().trim();
        maxNoOrders = Integer.parseInt(maxNoOrdersET.getText().toString().trim());
        tNotes = routeNotesET.getText().toString().trim();
        dDate = new BirthDate(routeDateDP.getYear(),
                routeDateDP.getMonth() + 1, routeDateDP.getDayOfMonth());

        if (routeSLocation.getVisibility() == View.VISIBLE){
            locationInfoForTrip.setsAdminArea(routeSLocation.getSelectedItem().toString());
            locationInfoForTrip.setsSubAdmin(routeSAreaName.getSelectedItem().toString());
        }
        else if(routeSLocation.getVisibility() == View.GONE){
            locationInfoForTrip.setsAdminArea(delegateLocation);
            locationInfoForTrip.setsSubAdmin(delegateAdmin);
        }



        locationInfoForTrip.setdAdminArea(routeDLocation.getSelectedItem().toString());
        locationInfoForTrip.setdSubAdmin(routeDAreaName.getSelectedItem().toString());
        uploadTripAndProceed();
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
            case R.id.button_yalla:
                validateTheUser();
                break;
        }
    }

    public void clearVehicleOptionsColor() {
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

    private void uploadTripAndProceed(){
        String tripId = System.currentTimeMillis() + delegateId;
        Trips trip = new Trips(tripId, "",delegateImageURL ,delegateId , delegateName, dDate, TPrice
                , tNotes, tVehicle, locationInfoForTrip,
                maxNoOrders , isPrePaid, isBreakable, oMaxPrice,delegateVerification, false, false);

        mTripsDatabaseReference.child(tripId).setValue(trip);

        mNumbersEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zagelNumbers = dataSnapshot.getValue(ZagelNumbers.class);
                //TODO wrong logic
                numbersDatabaseReference.child("noOfTrips").setValue(zagelNumbers.getNoOfTripsFromAlexToCairo()+1);
                Toast.makeText(AddTripsActivity.this, "your route has been add!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddTripsActivity.this, DelegateDashboardActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                finish();
                startActivity(i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        numbersDatabaseReference
                .addListenerForSingleValueEvent(mNumbersEventListener);


    }



}
