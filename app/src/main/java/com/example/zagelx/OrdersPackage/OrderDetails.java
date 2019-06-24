package com.example.zagelx.OrdersPackage;

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
import com.example.zagelx.Models.Orders;
import com.example.zagelx.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserEventListener;


    private TextView merchantName, packageName, packageSource, packageDestination
            , packageDate, packagePrice, deliveryFees
            , packageBreakablility, packageVehicle, packageStatus;
    private EditText packageNotes;
    private ImageView merchantImage, packageImage, packageVihicleIcon, infoDelivery;

    private Button changeFeesRequest, deliveryRequest;
    Orders currentOrder;

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
        setContentView(R.layout.activity_order_details);

        Intent i = getIntent();
        currentOrder = (Orders) i.getSerializableExtra("Package_ID");
        Log.e("hey now brownCow", "onCreate: "+ currentOrder );



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");

        merchantName = findViewById(R.id.user_name);
        packageName = findViewById(R.id.package_name);
        packageStatus = findViewById(R.id.package_status);
        packageSource = findViewById(R.id.package_source);
        packageDestination = findViewById(R.id.package_destination);
        packageDate = findViewById(R.id.package_date);
        packagePrice = findViewById(R.id.package_pre_paid_value);
        deliveryFees = findViewById(R.id.delivery_fees);
        packageNotes = findViewById(R.id.package_description);
        packageBreakablility = findViewById(R.id.package_breakability);
        packageVehicle = findViewById(R.id.package_vehicle);

        merchantImage = findViewById(R.id.user_image);
        packageImage = findViewById(R.id.package_image);
        packageVihicleIcon = findViewById(R.id.vehicle_icon);
        infoDelivery = findViewById(R.id.info_delivery_fees);

        changeFeesRequest = findViewById(R.id.button_bid_fees);
        deliveryRequest = findViewById(R.id.button_delivery_request);

        merchantName.setText(currentOrder.getMerchantName());
        Glide.with(merchantImage.getContext())
                .load(currentOrder.getMerchantImageURL())
                .into(merchantImage);
        merchantName.setText(currentOrder.getMerchantName());

        Glide.with(packageImage.getContext())
                .load(currentOrder.getPackageImageURL())
                .into(packageImage);
        packageName.setText(currentOrder.getPackageName());
        packageStatus.setText(currentOrder.getPackageState());

        packageSource.setText(currentOrder.getCurrentOrderLocationInfo().fullSourceLocation());
        packageDestination.setText(currentOrder.getCurrentOrderLocationInfo().fullDestinationLocation());
        packageDate.setText(currentOrder.getDeliveryDate().toString());
        deliveryFees.setText(currentOrder.getDeliveryPrice() + " EGP");
        packageVehicle.setText(currentOrder.getVehicle());
        if(currentOrder.isPrePaid()){
            packagePrice.setText(currentOrder.getPackagePrice()+ " EGP");}
        else{
            packagePrice.setText("0"+ " EGP");}
        if(currentOrder.isBreakable())
            packageBreakablility.setText("Breakable");
        else
            packageBreakablility.setText("Is Not Breakable");
        switch (currentOrder.getVehicle()) {
            case "Car":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_car_yellow);
                break;
            case "Train":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Metro":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_metro_yellow);
                break;
            case "Nos Na2l":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_nos_na2l_yellow);
                break;
            case "Bus":
                packageVihicleIcon.setImageResource(R.drawable.vehicle_bus_yellow);
                break;
        }
        infoDelivery.setOnClickListener(OrderDetails.this);
        packageNotes.setHint(currentOrder.getPackageDescription());




    }

}
