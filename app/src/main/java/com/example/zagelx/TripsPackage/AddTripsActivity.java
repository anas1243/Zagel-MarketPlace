package com.example.zagelx.TripsPackage;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.LocationInfo;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.UserInfo.DashboardActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Locale;

public class AddTripsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddTripsActivity";
    private final int sdk = android.os.Build.VERSION.SDK_INT;

    private DatePicker routeDateDP;
    private EditText routePriceET;
    private SwitchCompat isPrePaidSwitch;
    private TextView vehicle;

    private EditText routeNotesET;
    private EditText maxPrepaidPriceET;

    private EditText sourceET;
    private EditText destinationET;
    private EditText maxNoOrdersET;

    private Button AddTripButton;

    private ImageButton icCar, icBus, icTrain, icMetro, icMotorcycle, icNosNal;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;

    private FirebaseAuth mFirebaseAuth;

    private String delegateId, delegateName, delegateImageURL, TPrice, maxNoOrders, oMaxPrice, tVehicle, tNotes;

    private LocationInfo currenLocationInfo;

    private boolean isPrePaid = false;
    private BirthDate dDate;

    double destenationLatlng[];
    double sourceLatlng[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trips_activity);


        currenLocationInfo = new LocationInfo();
        Intent i = getIntent();
        destenationLatlng = (double[]) i.getSerializableExtra("destenationLatlng");
        sourceLatlng = (double[]) i.getSerializableExtra("sourceLatlng");

        Log.e(TAG, "onCreate: " + latlngToAddress(sourceLatlng[0], sourceLatlng[1])
                + latlngToAddress(destenationLatlng[0], destenationLatlng[1]));

        SetLocationInfo(sourceLatlng[0], sourceLatlng[1], "S");
        SetLocationInfo(destenationLatlng[0], destenationLatlng[1], "D");


        Log.e(TAG, "onCreate: currentOrderLocation: source is " + new LatLng(sourceLatlng[0]
                , sourceLatlng[1]) + " destination is " + new LatLng(destenationLatlng[0]
                , destenationLatlng[1]));


        routePriceET = findViewById(R.id.trip_price);
        isPrePaidSwitch = findViewById(R.id.pre_paid_switch);
        maxPrepaidPriceET = findViewById(R.id.maximum_price_prepaid);
        routeDateDP = findViewById(R.id.trip_date);
        maxNoOrdersET = findViewById(R.id.max_no_orders);

        vehicle = findViewById(R.id.vehicle_name);

        icBus = findViewById(R.id.ic_bus);
        icCar = findViewById(R.id.ic_car);
        icTrain = findViewById(R.id.ic_train);
        icMetro = findViewById(R.id.ic_metro);
        icMotorcycle = findViewById(R.id.ic_motorcycle);
        icNosNal = findViewById(R.id.ic_nos_na2l);

        routeNotesET = findViewById(R.id.trip_description);

        sourceET = findViewById(R.id.source_txt_view);
        destinationET = findViewById(R.id.destination_txt_view);


        AddTripButton = findViewById(R.id.button_yalla);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        delegateId = mFirebaseAuth.getCurrentUser().getUid();

        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");


        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(("Users")).child(delegateId);

        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                delegateName = dataSnapshot.getValue(Users.class).getName();
                delegateImageURL = dataSnapshot.getValue(Users.class).getProfilePictureURL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        icCar.setOnClickListener(this);
        icBus.setOnClickListener(this);
        icTrain.setOnClickListener(this);
        icMetro.setOnClickListener(this);
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

    }

    Address latlngToAddress(double LATITUDE, double LONGITUDE) {
        Address returnedAddress;
        Geocoder geocoder = new Geocoder(AddTripsActivity.this, Locale.getDefault());
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
                        currenLocationInfo.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), address.getLocality());
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        currenLocationInfo.setdLocationInfo(lat + "", lng + ""
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
                        currenLocationInfo.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , address.getSubAdminArea(), "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        currenLocationInfo.setdLocationInfo(lat + "", lng + ""
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
                        currenLocationInfo.setSLocationInfo(lat + "", lng + ""
                                , address.getAdminArea().replace("Governorate", "").trim()
                                , "", "");
                        Log.e(TAG, "SetLocationInfo: " + address);
                        return;
                    } else if (whichLocation.equals("D")) {
                        currenLocationInfo.setdLocationInfo(lat + "", lng + ""
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
        oMaxPrice = maxPrepaidPriceET.getText().toString().trim();
        TPrice = routePriceET.getText().toString().trim();
        tVehicle = vehicle.getText().toString().trim();
        maxNoOrders = maxNoOrdersET.getText().toString().trim();
        tNotes = routeNotesET.getText().toString().trim();
        dDate = new BirthDate(routeDateDP.getYear(),
                routeDateDP.getMonth() + 1, routeDateDP.getDayOfMonth());
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
            case R.id.ic_car:
                clearVehicleOptionsColor();
                vehicle.setText("Car");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icCar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_car_yellow));
                } else {
                    icCar.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_car_yellow));
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
            case R.id.ic_metro:
                clearVehicleOptionsColor();
                vehicle.setText("Metro");
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icMetro.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_metro_yellow));
                } else {
                    icMetro.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_metro_yellow));
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
            icCar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_car));
            icTrain.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_train));
            icMetro.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_metro));
            icMotorcycle.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_motorcycle));
            icNosNal.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.vehicle_nos_na2l));

        } else {
            icBus.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_bus));
            icCar.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_car));
            icTrain.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_train));
            icMetro.setBackground(ContextCompat.getDrawable(this, R.drawable.vehicle_metro));
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
    private void uploadTripAndProceed(){

        Trips trip = new Trips(delegateImageURL ,delegateId , delegateName, dDate, TPrice
                , tNotes, tVehicle, currenLocationInfo,
                maxNoOrders , isPrePaid, oMaxPrice);

        mTripsDatabaseReference.child(System.currentTimeMillis() + delegateId).setValue(trip);

        Toast.makeText(AddTripsActivity.this, "your order has been add!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddTripsActivity.this, TripsActivity.class);
        startActivity(i);
    }



}
