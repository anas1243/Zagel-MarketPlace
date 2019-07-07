package com.example.zagelx.OrdersPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.Utilities.DrawerUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference, mUserDatabaseReference, mRequestInfoDatabaseReference;
    private ValueEventListener mUserEventListener;


    private TextView merchantName, packageName, packageSource, packageDestination
            , packageDate, packagePrice, deliveryFees
            , packageBreakablility, packageVehicle, packageStatus;
    private EditText packageNotes;
    private ImageView merchantImage, packageImage, packageVihicleIcon, infoDelivery, verificationIcon;

    private Button  deliveryRequest;
    private Orders currentOrder;
    private String priceOffer;
    private FirebaseUser user;
    private Users currentUser;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_delivery_fees:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "you can make a request with your offer!!", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.button_delivery_request:
                confirmDeliveryFees();
        }
    }
    private void confirmDeliveryFees(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivery fees Confirmation!");
        alertDialog.setMessage("\nThis customer is willing to pay\n"+
                currentOrder.getDeliveryPrice()+" EGP. \n\n Please agree or enter your offer");

        final EditText input = new EditText(OrderDetails.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(16,16,16,16);

        input.setLayoutParams(lp);
        input.setText(currentOrder.getDeliveryPrice());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_cash);

        alertDialog.setPositiveButton("I agree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        priceOffer = input.getText().toString();
                        RequestInfo currentRequestInfo = new RequestInfo(user.getUid(), currentUser.getName(), currentUser.getProfilePictureURL()
                                ,currentUser.getRate(), priceOffer,currentUser.isVerified());


                        mRequestInfoDatabaseReference.child(System.currentTimeMillis() + user.getUid()).setValue(currentRequestInfo);
                        int currentNumberOfRequests = currentOrder.getNumberOfRequests();
                        mOrdersDatabaseReference.child("numberOfRequests").setValue(currentNumberOfRequests+1);

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent i = getIntent();
        currentOrder = (Orders) i.getSerializableExtra("Package_ID");
        Log.e("hey now brownCow", "onCreate: "+ currentOrder );



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders").child(currentOrder.getOrderId());
        mRequestInfoDatabaseReference = mOrdersDatabaseReference.child("currentRequestInfo");


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
        verificationIcon = findViewById(R.id.verification_icon);
        if(currentOrder.isVerifiedUser())
            verificationIcon.setVisibility(View.VISIBLE);

        deliveryRequest = findViewById(R.id.button_delivery_request);
        deliveryRequest.setOnClickListener(this);

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

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);




    }

}
}
