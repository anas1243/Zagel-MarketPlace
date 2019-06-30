package com.example.zagelx.TripsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripsDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserEventListener;


    private TextView delegateName, routeSource, routeDestination
            , routeDate, routePrice, deliveryFees
            , routeBreakablility, routeVehicle;
    private EditText routeNotes;
    private ImageView delegateImage, routeVihicleIcon, infoDelivery;

    private Button changeFeesRequest, deliveryRequest;
    private Trips currentTrip;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_delivery_fees:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "you can make a request with your price offer", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Intent i = getIntent();
        currentTrip = (Trips) i.getSerializableExtra("Route_ID");
        Log.e("hey now brownCow", "onCreate: "+ currentTrip );



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");

        delegateName = findViewById(R.id.user_name);
        routeSource = findViewById(R.id.route_source);
        routeDestination = findViewById(R.id.route_destination);
        routeDate = findViewById(R.id.route_date);
        routePrice = findViewById(R.id.package_pre_paid_value);
        deliveryFees = findViewById(R.id.route_fees);
        routeNotes = findViewById(R.id.route_description);
        routeBreakablility = findViewById(R.id.route_breakability);
        routeVehicle = findViewById(R.id.route_vehicle);

        delegateImage = findViewById(R.id.user_image);
        routeVihicleIcon = findViewById(R.id.vehicle_icon);
        infoDelivery = findViewById(R.id.route_fees_icon);

        changeFeesRequest = findViewById(R.id.button_bid_fees);
        deliveryRequest = findViewById(R.id.button_delivery_request);

        delegateName.setText(currentTrip.getDelegateName());
        Glide.with(delegateImage.getContext())
                .load(currentTrip.getDelegateImageURL())
                .into(delegateImage);



        routeSource.setText(currentTrip.getCurrentOrderLocationInfo().fullSourceLocation());
        routeDestination.setText(currentTrip.getCurrentOrderLocationInfo().fullDestinationLocation());
        routeDate.setText(currentTrip.getRouteDate().toString());
        deliveryFees.setText(currentTrip.getRoutePrice() + " EGP");
        routeVehicle.setText(currentTrip.getVehicle());
        if(currentTrip.isPrePaid()){
            routePrice.setText(currentTrip.getMaxPrePaidLimit()+ " EGP");}
        else{
            routePrice.setText("Pre Paid utility is not allowed in this route");}
        if(currentTrip.isBreakable())
            routeBreakablility.setText("are allowed");
        else
            routeBreakablility.setText("are not allowed");
        switch (currentTrip.getVehicle()) {
            case "Car":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_car_yellow);
                break;
            case "Train":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Metro":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_metro_yellow);
                break;
            case "Nos Na2l":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_nos_na2l_yellow);
                break;
            case "Bus":
                routeVihicleIcon.setImageResource(R.drawable.vehicle_bus_yellow);
                break;
        }
        infoDelivery.setOnClickListener(TripsDetails.this);
        routeNotes.setHint(currentTrip.getRouteDescription());




    }

}
