package com.example.zagelx.OrdersPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.DashboardPackage.MerchantDashboardActivity;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference, mUserDatabaseReference
            , mTripsDatabaseReference, mRequestInfoDatabaseReference;
    private ValueEventListener mUserEventListener, mOrderEventListener, mTripEventListener;


    private TextView merchantName, packageName, packageSource, packageDestination
            , packageDate, packagePrice, deliveryFees, packageBreakablility
            , packageVehicle, packageStatus, packageWeight;
    private EditText packageNotes;
    private ImageView merchantImage, packageImage, packageVehicleIcon, infoDelivery, verificationIcon;

    private Button deliveryRequest, deleteOrder, showRequestOrder, refuseOffer, acceptOffer, orderPicked, orderDelivered;
    private Orders currentOrder;
    private Trips currentTrip;
    private DelegatesNotification currentDelegateNotification = null;
    private MerchantsNotifications currentMerchantNotification = null;
    private String priceOffer;
    private FirebaseUser user;
    private Users currentUser;
    private int currentNumberOfNotifications;
    private int currentNumberOfCurrentOrderRequests;

    private LinearLayout ownerLayout, requestedDelegateLayout;

    private RelativeLayout assignedUserLayout, endConsumerLayout;
    private ImageView callUser, callEndConsumer;

    private String purpose, tripId;

    private TextView anotherUserName, anotherUserMobile, endConsumerName, endConsumerMobile, userLable;
    private boolean isPickedOrDelivered=false;

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
                break;
            case R.id.refuse_request:
                break;
            case R.id.accept_request:
                acceptMerchantOffer();
                break;
            case R.id.button_delivered_order:
                markOrderDelivered();
                break;
            case R.id.button_picked_order:
                markOrderPicked();
                break;
            case R.id.call_end_consumer_icon:
                callUser(currentOrder.getEndConsumerMobile());
                break;
            case  R.id.call_user_icon:
                if (userLable.getText().toString().equals("Your Merchant"))
                    callUser(currentOrder.getMerchantMobile());
                else
                    callUser(currentOrder.getAcceptedDelegateMobile());
                break;
        }
    }
    private void callUser(String userMobileNumber){
        Intent myIntent = new Intent(Intent.ACTION_DIAL);
        String phNum = "tel:" + userMobileNumber;
        myIntent.setData(Uri.parse(phNum));
        startActivity( myIntent ) ;
    }
    private void markOrderPicked(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Picking "+currentOrder.getPackageName()+ " Confirmation!");
        alertDialog.setMessage("سيتم ارسال اشعار للتاجر بانك قمت بضغت علي تم استلام الشحنة\n هل استلمت الشحنة بالفعل؟");
        alertDialog.setIcon(R.drawable.ic_delegates_requested);
        alertDialog.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        mOrdersDatabaseReference.child("packageState").setValue("Picked");
                        mOrdersDatabaseReference.child("picked").setValue(true);


                        Intent i = new Intent(OrderDetails.this, DelegateDashboardActivity.class);
                        i.putExtra("Which_Activity", "OrderDetails");
                        i.putExtra("PickedORDelivered", "Picked");
                        startActivity(i);



                    }
                });

        alertDialog.setNegativeButton("لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    private void markOrderDelivered(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivering "+currentOrder.getPackageName()+ " Confirmation!");
        alertDialog.setMessage("سيتم ارسال اشعار للمندوب بانك قمت بضغت علي تم توصيل الشحنة\n هل استلم عميلك الشحنة بالفعل؟");
        alertDialog.setIcon(R.drawable.ic_delegates_requested);

        alertDialog.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        mOrdersDatabaseReference.child("packageState").setValue("Delivered");
                        mOrdersDatabaseReference.child("delivered").setValue(true);


                        Intent i = new Intent(OrderDetails.this, MerchantDashboardActivity.class);
                        i.putExtra("Which_Activity", "OrderDetails");
                        i.putExtra("PickedORDelivered", "Delivered");
                        startActivity(i);



                    }
                });

        alertDialog.setNegativeButton("لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void confirmDeliveryFees() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivery fees Confirmation!");
        alertDialog.setMessage("\nThis customer is willing to pay\n" +
                currentOrder.getDeliveryPrice() + " EGP. \n\n Please agree or enter your offer");

        final EditText input = new EditText(OrderDetails.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(16, 16, 16, 16);

        input.setLayoutParams(lp);
        input.setText(currentOrder.getDeliveryPrice());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_cash);

        alertDialog.setPositiveButton("I agree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        priceOffer = input.getText().toString();
                        String requestId = System.currentTimeMillis() + user.getUid();
                        String notificationId = System.currentTimeMillis()
                                + currentOrder.getMerchantId();
                        RequestInfo currentRequestInfo = new RequestInfo(requestId
                                , user.getUid(), currentUser.getName()
                                , currentUser.getProfilePictureURL()
                                , currentUser.getMobileNumber(), currentUser.getRate(), priceOffer, currentUser.isVerified());
                        MerchantsNotifications merchantsNotifications = new MerchantsNotifications(
                                notificationId,"toMerchant","request"
                                , currentOrder.getMerchantId(), currentOrder.getPackageName()
                                , currentOrder.getOrderId(), currentRequestInfo, "", ""
                        );


                        mRequestInfoDatabaseReference.child(requestId).setValue(currentRequestInfo);
                        currentNumberOfNotifications += 1;
                        currentNumberOfCurrentOrderRequests += 1;
                        mOrdersDatabaseReference.child("numberOfRequests").setValue(currentNumberOfCurrentOrderRequests);
                        mOrdersDatabaseReference.child("packageState").setValue("Negotiable");
                        mUserDatabaseReference.child(currentOrder.getMerchantId()).child("numberOfNotifications").setValue(currentNumberOfNotifications);
                        mUserDatabaseReference.child(currentOrder.getMerchantId()).child("Notifications").child(notificationId).setValue(merchantsNotifications);
                        dialog.cancel();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.scroll_view), "لقد تمل ارسال طلبك للتاجر", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        if (currentUser.getMode().equals("Merchant")){
                            Intent i = new Intent(OrderDetails.this, MerchantDashboardActivity.class);
                            i.putExtra("Which_Activity", "SomethingElse");
                            startActivity(i);
                        }
                        else if (currentUser.getMode().equals("Delivery Delegate")){
                            Intent i = new Intent(OrderDetails.this, DelegateDashboardActivity.class);
                            i.putExtra("Which_Activity", "SomethingElse");
                            startActivity(i);
                        }


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

    private void acceptMerchantOffer(){

        Log.e("test button pos.", "onClick: "+ currentDelegateNotification.getOrderId() );

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Accept Delivery Request Confirmation!");
        alertDialog.setMessage("Are you sure you want " +" to deliver "
                +currentDelegateNotification.getOrderName()+" for "
                + currentDelegateNotification.getRequestInfo().getOfferPrice()+" EGP to "
                +currentDelegateNotification.getRequestInfo().getUserName()+" in "+ currentDelegateNotification.getTripDate());
        alertDialog.setIcon(R.drawable.ic_delegates_requested);

        alertDialog.setPositiveButton("Yes I agree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                        if (user != null) {

                            mUserEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    currentUser = dataSnapshot.getValue(Users.class);


                                    String requestId = System.currentTimeMillis() + user.getUid();
                                    String notificationId = System.currentTimeMillis()
                                            + currentOrder.getMerchantId();
                                    RequestInfo currentRequestInfo = new RequestInfo(requestId
                                            , user.getUid(), currentUser.getName()
                                            , currentUser.getProfilePictureURL()
                                            , currentUser.getMobileNumber(), currentUser.getRate()
                                            , currentDelegateNotification.getRequestInfo().getOfferPrice()
                                            , currentUser.isVerified());

                                    currentRequestInfo.setStatus(true);

                                    MerchantsNotifications merchantsNotifications = new MerchantsNotifications(
                                            notificationId,"toMerchant","acceptance"
                                            , currentOrder.getMerchantId(), currentOrder.getPackageName()
                                            , currentOrder.getOrderId(), currentRequestInfo
                                            , currentDelegateNotification.getTripId()
                                            , currentDelegateNotification.getTripDate());

                                    mUserDatabaseReference.child(currentOrder.getMerchantId())
                                            .child("Notifications")
                                            .child(notificationId).setValue(merchantsNotifications);

                                    mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders")
                                            .child(currentDelegateNotification.getOrderId());

                                    mOrdersDatabaseReference.child("currentRequestInfo")
                                            .child(requestId).setValue(currentRequestInfo);

                                    mOrdersDatabaseReference.child("acceptedDelegateID")
                                            .setValue(user.getUid());
                                    mOrdersDatabaseReference.child("acceptedDelegateName")
                                            .setValue(currentUser.getName());
                                    mOrdersDatabaseReference.child("acceptedDelegateMobile")
                                            .setValue(currentUser.getMobileNumber());
                                    mOrdersDatabaseReference.child("packageState").setValue("Reserved");
                                    mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips")
                                            .child(tripId);

                                    mTripEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            currentTrip = dataSnapshot.getValue(Trips.class);
                                            Map<String, Map<String, String>> ordersInThisRoute;
                                            if (currentTrip.getRouteOrders()!= null){
                                                 ordersInThisRoute = currentTrip.getRouteOrders();
                                                ordersInThisRoute.put(currentOrder.getOrderId(), new HashMap<String, String>() {{
                                                    put(currentOrder.getPackageName(),currentOrder.getPackageImageURL());
                                                }
                                                });
                                                mTripsDatabaseReference.child("routeOrders").setValue(ordersInThisRoute);
                                            }else{
                                                 ordersInThisRoute = new HashMap<String, Map<String, String>>(){{
                                                    put(currentOrder.getOrderId(), new HashMap<String, String>() {{
                                                        put(currentOrder.getPackageName(),currentOrder.getPackageImageURL());
                                                    }
                                                    });
                                                }};

                                                mTripsDatabaseReference.child("routeOrders").setValue(ordersInThisRoute);

                                            }
                                            Log.e("OrderDetails", "onDataChange: route orders list sise is"+ ordersInThisRoute.size() );
                                            if (ordersInThisRoute.size() == currentTrip.getMaxNoOrders())
                                                mTripsDatabaseReference.child("fullRoute").setValue(true);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };

                                    mTripsDatabaseReference.addListenerForSingleValueEvent(mTripEventListener);

                                    Intent i = new Intent(OrderDetails.this, OrderDetails.class);
                                    i.putExtra("Package_ID", currentDelegateNotification);
                                    finish();
                                    startActivity(i);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };

                            mUserDatabaseReference.child(user.getUid())
                                    .addListenerForSingleValueEvent(mUserEventListener);


                        }



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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        merchantName = findViewById(R.id.user_name);
        packageName = findViewById(R.id.package_name);
        packageStatus = findViewById(R.id.package_status);
        packageSource = findViewById(R.id.package_source);
        packageDestination = findViewById(R.id.package_destination);
        packageWeight = findViewById(R.id.package_weight);
        packageDate = findViewById(R.id.package_date);
        packagePrice = findViewById(R.id.package_pre_paid_value);
        deliveryFees = findViewById(R.id.delivery_fees);
        packageNotes = findViewById(R.id.package_description);
        packageBreakablility = findViewById(R.id.package_breakability);
        packageVehicle = findViewById(R.id.package_vehicle);

        merchantImage = findViewById(R.id.user_image);
        packageImage = findViewById(R.id.package_image);
        packageVehicleIcon = findViewById(R.id.vehicle_icon);
        infoDelivery = findViewById(R.id.info_delivery_fees);
        verificationIcon = findViewById(R.id.verification_icon);

        deliveryRequest = findViewById(R.id.button_delivery_request);
        deleteOrder = findViewById(R.id.delete_request);
        showRequestOrder = findViewById(R.id.show_requests);
        refuseOffer = findViewById(R.id.refuse_request);
        acceptOffer = findViewById(R.id.accept_request);

        ownerLayout = findViewById(R.id.layout_owner);
        requestedDelegateLayout = findViewById(R.id.layout_requested_delegate);

        assignedUserLayout = findViewById(R.id.assigned_user_info);
        endConsumerLayout = findViewById(R.id.end_consumer_layout);

        callUser = findViewById(R.id.call_user_icon);
        callEndConsumer = findViewById(R.id.call_end_consumer_icon);

        userLable = findViewById(R.id.user_lable);
        anotherUserName = findViewById(R.id.another_user_name);
        anotherUserMobile = findViewById(R.id.another_user_mobile);
        endConsumerName = findViewById(R.id.end_consumer_name);
        endConsumerMobile = findViewById(R.id.end_consumer_mobile);

        orderPicked = findViewById(R.id.button_picked_order);
        orderDelivered = findViewById(R.id.button_delivered_order);


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                 String value = getIntent().getExtras().getString(key);
                Log.e("test notifications", "Key: " + key + " Value: " + value);
                if(key.equals("orderId")){
                    isPickedOrDelivered = true;
                    Log.e("test value of FCM", "onCreate: "+ value );
                    mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders")
                            .child(value);


                    mOrderEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentOrder = dataSnapshot.getValue(Orders.class);
                            Log.e("test Orders", "onDataChange: " + currentOrder.toString()
                                    + "from cloud functions test");

                            fillViewsWithCorrectData();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    mOrdersDatabaseReference.addListenerForSingleValueEvent(mOrderEventListener);

                }
            }
        }
        //if isPickedOrDelivered equal true there for cloud function is triggered to send picked or delivered acknowledge
        if (isPickedOrDelivered)
            return;



            Intent i = getIntent();
            String whichClass = i.getSerializableExtra("Package_ID").getClass().getName();
            Log.e("test el ", "onCreate: " + whichClass);
            if (whichClass.equals(Orders.class.getName())) {
                currentOrder = (Orders) i.getSerializableExtra("Package_ID");


                fillViewsWithCorrectData();

            }
            else if (whichClass.equals(DelegatesNotification.class.getName())) {
                purpose = (String) i.getSerializableExtra("Purpose");
                currentDelegateNotification = (DelegatesNotification) i.getSerializableExtra("Package_ID");
                tripId = currentDelegateNotification.getTripId();

                mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders")
                        .child(currentDelegateNotification.getOrderId());


                mOrderEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentOrder = dataSnapshot.getValue(Orders.class);
                        Log.e("test Orders", "onDataChange: " + currentOrder.getOrderId());
//
                        fillViewsWithCorrectData();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                mOrdersDatabaseReference.addListenerForSingleValueEvent(mOrderEventListener);
            }
            else if (whichClass.equals(MerchantsNotifications.class.getName())){
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.scroll_view), "سيتواصل معك المندوب في اقرب وقت ", Snackbar.LENGTH_LONG);
                snackbar.show();


                currentMerchantNotification = (MerchantsNotifications) i.getSerializableExtra("Package_ID");
                mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders")
                        .child(currentMerchantNotification.getOrderId());


                mOrderEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentOrder = dataSnapshot.getValue(Orders.class);

                        fillViewsWithCorrectData();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                mOrdersDatabaseReference.addListenerForSingleValueEvent(mOrderEventListener);
            }




    }

    private void fillViewsWithCorrectData() {

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);
                    currentNumberOfNotifications = currentUser.getNumberOfNotifications();

                    if (currentUser.getMode().equals("Merchant")) {
                        if (currentOrder.getMerchantId().equals(user.getUid())
                                && currentOrder.getAcceptedDelegateID().equals("")) {

                            deliveryRequest.setVisibility(View.GONE);
                            assignedUserLayout.setVisibility(View.GONE);
                            endConsumerLayout.setVisibility(View.GONE);

                            ownerLayout.setVisibility(View.VISIBLE);
                            deleteOrder.setOnClickListener(OrderDetails.this);
                            showRequestOrder.setOnClickListener(OrderDetails.this);

                        } else if(currentOrder.getMerchantId().equals(user.getUid())
                                && !currentOrder.getAcceptedDelegateID().equals("")) {

                            assignedUserLayout.setVisibility(View.VISIBLE);
                            endConsumerLayout.setVisibility(View.VISIBLE);

                            anotherUserName.setText(currentOrder.getAcceptedDelegateName());
                            anotherUserMobile.setText(currentOrder.getAcceptedDelegateMobile());
                            endConsumerName.setText(currentOrder.getEndConsumerName());
                            endConsumerMobile.setText(currentOrder.getEndConsumerMobile());
                            callUser.setOnClickListener(OrderDetails.this);
                            callEndConsumer.setOnClickListener(OrderDetails.this);


                            deliveryRequest.setVisibility(View.GONE);
                            ownerLayout.setVisibility(View.GONE);
                            if (currentOrder.isPicked()){
                                orderDelivered.setVisibility(View.VISIBLE);
                                orderDelivered.setOnClickListener(OrderDetails.this);
                            }
                        }else{
                            deliveryRequest.setVisibility(View.GONE);
                            assignedUserLayout.setVisibility(View.GONE);
                            endConsumerLayout.setVisibility(View.GONE);
                            ownerLayout.setVisibility(View.GONE);
                        }

                    } else if( currentUser.getMode().equals("Delivery Delegate") ){

                        if(currentOrder.getAcceptedDelegateID().equals(user.getUid())){

                            ownerLayout.setVisibility(View.GONE);
                            deliveryRequest.setVisibility(View.GONE);

                            assignedUserLayout.setVisibility(View.VISIBLE);
                            endConsumerLayout.setVisibility(View.VISIBLE);
                            userLable.setText("Your Merchant");

                            anotherUserName.setText(currentOrder.getMerchantName());
                            anotherUserMobile.setText(currentOrder.getMerchantMobile());
                            endConsumerName.setText(currentOrder.getEndConsumerName());
                            endConsumerMobile.setText(currentOrder.getEndConsumerMobile());
                            callUser.setOnClickListener(OrderDetails.this);
                            callEndConsumer.setOnClickListener(OrderDetails.this);

                            if (!currentOrder.isPicked()){
                            orderPicked.setVisibility(View.VISIBLE);
                            orderPicked.setOnClickListener(OrderDetails.this);
                            }

                        }
                        else {
                            if(currentDelegateNotification!= null && purpose.equals("request")){
                                deliveryRequest.setVisibility(View.GONE);
                                ownerLayout.setVisibility(View.GONE);
                                assignedUserLayout.setVisibility(View.GONE);
                                endConsumerLayout.setVisibility(View.GONE);
                                requestedDelegateLayout.setVisibility(View.VISIBLE);
                                refuseOffer.setOnClickListener(OrderDetails.this);
                                acceptOffer.setOnClickListener(OrderDetails.this);

                            }else{
                                deliveryRequest.setVisibility(View.VISIBLE);
                                assignedUserLayout.setVisibility(View.GONE);
                                endConsumerLayout.setVisibility(View.GONE);
                                ownerLayout.setVisibility(View.GONE);

                            }

                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);


        }else {
            AuthUI.getInstance().signOut(this);

        }


        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders")
                .child(currentOrder.getOrderId());
        mRequestInfoDatabaseReference = mOrdersDatabaseReference.child("currentRequestInfo");

        currentNumberOfCurrentOrderRequests = currentOrder.getNumberOfRequests();
        if (currentOrder.isVerifiedUser())
            verificationIcon.setVisibility(View.VISIBLE);


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

        packageSource.setText(currentOrder.getLocationInfoForPackage().fullSourceLocation());
        packageDestination.setText(currentOrder.getLocationInfoForPackage().fullDestinationLocation());
        packageWeight.setText(currentOrder.getPackageWeight());
        packageDate.setText(currentOrder.getDeliveryDate().toString());
        deliveryFees.setText(currentOrder.getDeliveryPrice() + " EGP");
        packageVehicle.setText(currentOrder.getVehicle());
        if (currentOrder.isPrePaid()) {
            packagePrice.setText(currentOrder.getPackagePrice() + " EGP");
        } else {
            packagePrice.setText("0" + " EGP");
        }
        if (currentOrder.isBreakable())
            packageBreakablility.setText("Breakable");
        else
            packageBreakablility.setText("Is Not Breakable");
        switch (currentOrder.getVehicle()) {
            case "Car":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_car_yellow);
                break;
            case "Train":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Metro":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_metro_yellow);
                break;
            case "Nos Na2l":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_nos_na2l_yellow);
                break;
            case "Bus":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_bus_yellow);
                break;
        }
        infoDelivery.setOnClickListener(OrderDetails.this);
        packageNotes.setHint(currentOrder.getPackageDescription());


    }
}
