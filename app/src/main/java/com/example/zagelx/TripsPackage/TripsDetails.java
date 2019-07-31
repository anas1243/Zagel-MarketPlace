package com.example.zagelx.TripsPackage;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.OrdersPackage.OrderDetails;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripsDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mOrdersDatabaseReference, mRequestInfoDatabaseReference;
    private ValueEventListener mUserEventListener;


    private TextView delegateName, routeSource, routeDestination, routeDate, routePrice, deliveryFees, routeBreakablility, routeVehicle;
    private EditText routeNotes;
    private ImageView delegateImage, routeVihicleIcon, infoDelivery;

    private Button deliveryRequest, deleteTrip, showRequests;
    private Spinner orderItems;
    private String selectedOrderItemID;
    private Trips currentTrip;
    private FirebaseUser user;
    private Users currentUser;
    private String userType;

    private LinearLayout ownerLayout;
    private RelativeLayout youOrdersLayout;

    private Query queryOrders;
    final List<String> ordersNameArray =  new ArrayList<>();
    final List<String> ordersIDArray =  new ArrayList<>();

    private int currentNumberOfNotifications;
    private int currentNumberOfCurrentOrderRequests;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_delivery_fees:
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "you can make a request with your price offer", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.button_delivery_request:
                confirmDeliveryFees();
                break;
            case R.id.delete_request:
                //deleteTheCurrentTrip();
            case R.id.show_requests:
                //showAllPackagesInThisRoute();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Intent i = getIntent();
        currentTrip = (Trips) i.getSerializableExtra("Route_ID");
        Log.e("hey now brownCow", "onCreate: " + currentTrip);

        currentNumberOfCurrentOrderRequests = currentTrip.getNumberOfRequests();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips").child(currentTrip.getTripId());
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mRequestInfoDatabaseReference = mTripsDatabaseReference.child("currentRequestInfo");



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
        orderItems =  findViewById(R.id.yourOrders_spinner);
        ownerLayout = findViewById(R.id.layout_owner);
        youOrdersLayout = findViewById(R.id.youOrders_layout);
        deleteTrip = findViewById(R.id.delete_request);
        showRequests = findViewById(R.id.show_requests);



        deliveryRequest = findViewById(R.id.button_delivery_request);
        deliveryRequest.setOnClickListener(this);

        orderItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {

                selectedOrderItemID = ordersIDArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                Log.e("routes", "nothing selected");
            }
        });

        delegateName.setText(currentTrip.getDelegateName());
        Glide.with(delegateImage.getContext())
                .load(currentTrip.getDelegateImageURL())
                .into(delegateImage);




        routeSource.setText(currentTrip.getCurrentOrderLocationInfo().fullSourceLocation());
        routeDestination.setText(currentTrip.getCurrentOrderLocationInfo().fullDestinationLocation());
        routeDate.setText(currentTrip.getRouteDate().toString());
        deliveryFees.setText(currentTrip.getRoutePrice() + " EGP");
        routeVehicle.setText(currentTrip.getVehicle());
        if (currentTrip.isPrePaid()) {
            routePrice.setText(currentTrip.getMaxPrePaidLimit() + " EGP");
        } else {
            routePrice.setText("Pre Paid utility is not allowed in this route");
        }
        if (currentTrip.isBreakable())
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
        findUserType();

    }

    private void findUserType() {
        if (user != null) {
            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);
                    currentNumberOfNotifications = currentUser.getNumberOfNotifications();
                    userType = currentUser.getMode();
                    if(userType.equals("Merchant")){
                        makeHimChooseHisOrder();
                    }else {
                        youOrdersLayout.setVisibility(View.GONE);
                        if(currentTrip.getDelegateID().contains(currentUser.getID())){
                            deliveryRequest.setVisibility(View.GONE);
                            ownerLayout.setVisibility(View.VISIBLE);
                            deleteTrip.setOnClickListener(TripsDetails.this);
                            showRequests.setOnClickListener(TripsDetails.this);

                        }else{
                            deliveryRequest.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);
        }
    }
    private void makeHimChooseHisOrder(){
        queryOrders = mOrdersDatabaseReference.orderByChild("merchantId").equalTo(user.getUid());


        queryOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot order : dataSnapshot.getChildren()) {
                        Orders currentOrder = order.getValue(Orders.class);
                        ordersNameArray.add(currentOrder.getPackageName());
                        ordersIDArray.add(currentOrder.getOrderId());
                    }
                    ArrayAdapter<String> OrderSpinnerAdapter = new ArrayAdapter<>(
                            TripsDetails.this, android.R.layout.simple_spinner_item, ordersNameArray);

                    OrderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    orderItems.setAdapter(OrderSpinnerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void confirmDeliveryFees() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TripsDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivery fees Confirmation!");
        alertDialog.setMessage("\nThis delegate want \n" +
                currentTrip.getRoutePrice() + " EGP to deliver your package "+ orderItems.getSelectedItem()+". \n\n Please agree or enter your offer");

        final EditText input = new EditText(TripsDetails.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(16, 16, 16, 16);

        input.setLayoutParams(lp);
        input.setText(currentTrip.getRoutePrice());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_cash);

        alertDialog.setPositiveButton("I agree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String priceOffer = input.getText().toString();
                        String requestId = System.currentTimeMillis() + user.getUid();
                        String notificationId = System.currentTimeMillis() + currentTrip.getDelegateID();
                        RequestInfo currentRequestInfo = new RequestInfo(requestId, user.getUid(), currentUser.getName(), currentUser.getProfilePictureURL()
                                , currentUser.getMobileNumber(), currentUser.getRate(), priceOffer, currentUser.isVerified());
                        DelegatesNotification delegatesNotifications = new DelegatesNotification(
                                notificationId,"toDelegate", "request", currentTrip.getDelegateID(), currentTrip.getTripId(), currentTrip.getRouteDate().toString()
                                , selectedOrderItemID, orderItems.getSelectedItem().toString()
                                , currentRequestInfo
                        );


                        mRequestInfoDatabaseReference.child(requestId).setValue(currentRequestInfo);
                        currentNumberOfNotifications += 1;
                        currentNumberOfCurrentOrderRequests += 1;
                        mTripsDatabaseReference.child("numberOfRequests").setValue(currentNumberOfCurrentOrderRequests);
                        mUserDatabaseReference.child(currentTrip.getDelegateID()).child("numberOfNotifications").setValue(currentNumberOfNotifications);
                        mUserDatabaseReference.child(currentTrip.getDelegateID()).child("Notifications").child(notificationId).setValue(delegatesNotifications);
                        mOrdersDatabaseReference.child(selectedOrderItemID).child("packageState").setValue("Negotiable");
                        dialog.cancel();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.scroll_view), "لقد تمل ارسال طلبك للمندوب", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Intent i = new Intent(TripsDetails.this, OrdersActivity.class);
                        finish();
                        startActivity(i);

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


}
