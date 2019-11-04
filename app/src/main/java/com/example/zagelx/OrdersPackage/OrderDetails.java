package com.example.zagelx.OrdersPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zagelx.Authentication.NotVerifiedUser;
import com.example.zagelx.FreeBirdsDashboardPackage.FreesDashboardActivity;
import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.MerchantDashboardInsideActivity;
import com.example.zagelx.Models.CourierInfo;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.PmsNotifications;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.Models.ZagelNumbers;
import com.example.zagelx.PMsDashboardPackage.PMInSideOrdersPackage.InsideOrdersDashboardActivity;
import com.example.zagelx.PMsDashboardPackage.PMOutSideOrdersPackage.OutsideOrdersDashboardActivity;
import com.example.zagelx.R;
import com.example.zagelx.StaticBirdsDashboardPackage.StaticsOrdersPackage.StaticsOrdersDashboardActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference, mUserDatabaseReference, mPMDatabaseReference, mTripsDatabaseReference, mRequestInfoDatabaseReference, numbersDatabaseReference;
    private ValueEventListener mUserEventListener, mOrderEventListener, mTripEventListener, mNumbersEventListener;
    private ZagelNumbers zagelNumbers;
    private ChildEventListener mPMChildEventListener;


    private TextView merchantName, packageName, packageSource, packageDestination, packageDate, packagePrice, deliveryFees, packageBreakablility, packageVehicle, packageStatus, packageWeight;
    private EditText packageNotes;
    private ImageView merchantImage, packageImage, packageVehicleIcon, infoDelivery, verificationIcon;

    private Button deliveredHeadquarters, deliveryRequest, deleteOrder, showRequestOrder, refuseOffer, acceptOffer, orderPicked, orderDelivered;
    private Orders currentOrder;
    String orderId, whichActivity, purpose = "";
    private Trips currentTrip;
    private DelegatesNotification currentDelegateNotification = null;
    private MerchantsNotifications currentMerchantNotification = null;
    private FirebaseUser user;
    private Users currentUser;
    private int currentNumberOfNotifications;
    private LinearLayout ownerLayout, requestedDelegateLayout;

    private RelativeLayout assignedUserLayout, endConsumerLayout;
    private ImageView callUser, callEndConsumer;

    private String tripId, whichBranch;
    private ProgressBar progressBar;

    private TextView anotherUserName, anotherUserMobile, endConsumerName, endConsumerMobile, userLable, ownerTextMessage;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_delivery_fees:
//                Snackbar snackbar = Snackbar
//                        .make(findViewById(R.id.scroll_view), "you can make a request with your offer!!", Snackbar.LENGTH_LONG);
//                snackbar.show();
                break;
            case R.id.button_delivery_request:
                confirmDeliveryRequest();
                break;
            case R.id.refuse_request:
                refuseMerchantOffer();
                break;
            case R.id.accept_request:
                //acceptMerchantOffer();
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
            case R.id.call_user_icon:
                if (userLable.getText().toString().equals("Your Merchant"))
                    callUser(currentOrder.getMerchantMobile());
                else
                    callUser(currentOrder.getAcceptedDelegateMobile());
                break;
            case R.id.show_requests:
                showOrderRequests();
                break;
            case R.id.delete_request:
                deleteThisOrder();
                break;
            case R.id.button_delivered_headQuarters:
                setOrderAsDeliveredToHeadquarters();
                break;
        }
    }

    public void setOrderAsDeliveredToHeadquarters() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivering " + currentOrder.getPackageName() + " Confirmation!");
        if (currentUser.getGroup().equals("AlexStaticBirds")
                || currentUser.getGroup().equals("CairoStaticBirds"))
            alertDialog.setMessage("سيتم ارسال اشعار لمديرك انك سلمت الشحنة في مقر الشركة\n هل سلمت الشحنة في المقر بالفعل؟");
        else
            alertDialog.setMessage("هل سلم المندوب الشحنة في المقر و قمت بترقيمها بالفعل؟");

        alertDialog.setIcon(R.drawable.ic_delegates_requested);

        alertDialog.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        String requestId = System.currentTimeMillis() + user.getUid();
                        if (currentUser.getGroup().equals("AlexStaticBirds")
                                || currentUser.getGroup().equals("CairoStaticBirds")) {
                            currentOrder.setDelegateHeadquarters(true);
                            mOrdersDatabaseReference.child("delegateHeadquarters").setValue(true);

                            CourierInfo currentCourierInfo = new CourierInfo(requestId
                                    , user.getUid(), currentUser.getName()
                                    , currentUser.getProfilePictureURL()
                                    , currentUser.getMobileNumber(), currentUser.getGroup()
                                    , currentUser.getRate(), currentUser.isVerified());

                            if (currentOrder.getWhichBranch().equals("AlexToCairoOrders"))
                                sendNotificationToPM("AlexPM", currentCourierInfo, "CurrentHeadquarters");
                            else if (currentOrder.getWhichBranch().equals("CairoToAlexOrders"))
                                sendNotificationToPM("CairoPM", currentCourierInfo, "CurrentHeadquarters");

                        } else if (currentUser.getGroup().equals("AlexPM")
                                || currentUser.getGroup().equals("CairoPM")) {
                            currentOrder.setPmHeadquarters(true);
                            mOrdersDatabaseReference.child("pmHeadquarters").setValue(true);
                        }

                        if (currentOrder.isDelegateHeadquarters() && currentOrder.isPmHeadquarters())
                            mOrdersDatabaseReference.child("packageStateForPm").setValue("CurrentHeadquarters");

                        Intent i;
                        if (currentUser.getGroup().equals("AlexStaticBirds")
                                || currentUser.getMode().equals("CairoStaticBirds")) {
                            i = new Intent(OrderDetails.this, StaticsOrdersDashboardActivity.class);

                        } else {
                            i = new Intent(OrderDetails.this, InsideOrdersDashboardActivity.class);
                        }
                        i.putExtra("WhichActivity", "OrderDetails");
                        i.putExtra("PickedORDelivered", "CurrentHeadquarters");
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

    public void sendNotificationToPM(final String pmGroup
            , final CourierInfo currentCourierInfo, final String purpose) {
        mPMChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users userPM = dataSnapshot.getValue(Users.class);

                if (userPM.getGroup().equals(pmGroup)) {

                    String notificationId = System.currentTimeMillis()
                            + userPM.getID();
                    PmsNotifications pmNotifications = new PmsNotifications(
                            notificationId, "ToPM", purpose
                            , currentOrder.getMerchantId(), currentOrder.getMerchantName()
                            , currentCourierInfo.getUserID(), currentCourierInfo.getUserName(), currentOrder.getEndConsumerName()
                            , currentOrder.getPackageName()
                            , currentOrder.getOrderId(), whichBranch, currentCourierInfo
                    );
                    mPMDatabaseReference.child(userPM.getID())
                            .child("PMNotifications").child(notificationId).setValue(pmNotifications);


                }


                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPMDatabaseReference.addChildEventListener(mPMChildEventListener);
    }

    public void showOrderRequests() {
        Intent i = new Intent(OrderDetails.this, OrdersRequestsActivity.class);
        i.putExtra("Which_Activity", "OrderDetails");
        i.putExtra("orderId", currentOrder.getOrderId());
        startActivity(i);

    }

    public void deleteThisOrder() {

    }

    private void callUser(String userMobileNumber) {
        Intent myIntent = new Intent(Intent.ACTION_DIAL);
        String phNum = "tel:" + userMobileNumber;
        myIntent.setData(Uri.parse(phNum));
        startActivity(myIntent);
    }

    private void markOrderPicked() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Picking " + currentOrder.getPackageName() + " Confirmation!");
        if (currentUser.getGroup().equals("AlexMerchants")
                || currentUser.getGroup().equals("CairoMerchants"))
            alertDialog.setMessage("سيتم ارسال اشعار للمندوب بانك قمت بضغت علي تم تسليم الشحنة\n هل سلمت الشحنة بالفعل؟");
        else
            alertDialog.setMessage("سيتم ارسال اشعار للتاجر بانك قمت بضغت علي تم استلام الشحنة\n هل استلمت الشحنة بالفعل؟");

        alertDialog.setIcon(R.drawable.ic_delegates_requested);
        alertDialog.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        String requestId = System.currentTimeMillis() + user.getUid();
                        String notificationId;

                        if (currentUser.getGroup().equals("AlexMerchants")
                                || currentUser.getGroup().equals("CairoMerchants")) {
                            currentOrder.setMerchantPicked(true);
                            mOrdersDatabaseReference.child("merchantPicked").setValue(true);

                            notificationId = System.currentTimeMillis()
                                    + currentOrder.getAcceptedDelegateID();
                            CourierInfo currentCourierInfo = new CourierInfo(requestId
                                    , user.getUid(), currentUser.getName()
                                    , currentUser.getProfilePictureURL()
                                    , currentUser.getMobileNumber(), currentUser.getGroup()
                                    , currentUser.getRate(), currentUser.isVerified());
                            DelegatesNotification delegatesNotifications = new DelegatesNotification(
                                    notificationId, "ToDelegate", "Picked"
                                    , currentOrder.getAcceptedDelegateID(), currentOrder.getOrderId()
                                    , currentOrder.getPackageName(), whichBranch, currentCourierInfo
                            );
                            mUserDatabaseReference.child(currentOrder.getAcceptedDelegateID())
                                    .child("Notifications").child(notificationId).setValue(delegatesNotifications);


                        } else {
                            currentOrder.setDelegatePicked(true);
                            mOrdersDatabaseReference.child("delegatePicked").setValue(true);

                            notificationId = System.currentTimeMillis()
                                    + currentOrder.getMerchantId();
                            CourierInfo currentCourierInfo = new CourierInfo(requestId
                                    , user.getUid(), currentUser.getName()
                                    , currentUser.getProfilePictureURL()
                                    , currentUser.getMobileNumber(), currentUser.getGroup()
                                    , currentUser.getRate(), currentUser.isVerified());
                            MerchantsNotifications merchantsNotifications = new MerchantsNotifications(
                                    notificationId, "ToMerchant", "Picked"
                                    , currentOrder.getMerchantId(), currentOrder.getPackageName()
                                    , currentOrder.getOrderId(), whichBranch, currentCourierInfo
                            );
                            mUserDatabaseReference.child(currentOrder.getMerchantId())
                                    .child("Notifications").child(notificationId).setValue(merchantsNotifications);

                            if (currentOrder.getWhichBranch().equals("AlexOrders")
                                    || currentOrder.getWhichBranch().equals("AlexToCairoOrders"))
                                sendNotificationToPM("AlexPM", currentCourierInfo, "Picked");
                            else if (currentOrder.getWhichBranch().equals("CairoOrders")
                                    || currentOrder.getWhichBranch().equals("CairoToAlexOrders"))
                                sendNotificationToPM("CairoPM", currentCourierInfo, "Picked");


                        }

                        if (currentOrder.isDelegatePicked() && currentOrder.isMerchantPicked()){
                            mOrdersDatabaseReference.child("packageState").setValue("Picked");
                            mOrdersDatabaseReference.child("packageStateForPm").setValue("Picked");
                        }



                        Intent i;
                        if (currentUser.getGroup().equals("AlexMerchants")
                                || currentUser.getGroup().equals("CairoMerchants")) {
                            i = new Intent(OrderDetails.this, MerchantDashboardInsideActivity.class);

                        } else if (currentUser.getGroup().equals("AlexStaticBirds")
                                || currentUser.getGroup().equals("CairoStaticBirds")) {
                            i = new Intent(OrderDetails.this, StaticsOrdersDashboardActivity.class);

                        } else if (currentUser.getGroup().equals("AlexFreeBirds")
                                || currentUser.getGroup().equals("CairoFreeBirds")) {
                            i = new Intent(OrderDetails.this, FreesDashboardActivity.class);

                        } else {
                            i = new Intent(OrderDetails.this, InsideOrdersDashboardActivity.class);
                        }
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

    private void markOrderDelivered() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Delivering " + currentOrder.getPackageName() + " Confirmation!");
        if (currentUser.getGroup().equals("AlexMerchants")
                || currentUser.getGroup().equals("CairoMerchants"))
            alertDialog.setMessage("سيتم ارسال اشعار للمندوب بانك قمت بضغت علي تم توصيل الشحنة\n هل استلم عميلك الشحنة بالفعل؟");
        else
            alertDialog.setMessage("سيتم ارسال اشعار للتاجر بانك قمت بضغت علي تم توصيل الشحنة\n هل استلم عميلك الشحنة بالفعل؟");
        alertDialog.setIcon(R.drawable.ic_delegates_requested);

        alertDialog.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        String requestId = System.currentTimeMillis() + user.getUid();
                        String notificationId;

                        if (currentUser.getGroup().equals("AlexMerchants")
                                || currentUser.getGroup().equals("CairoMerchants")) {
                            currentOrder.setMerchantDelivered(true);
                            mOrdersDatabaseReference.child("merchantDelivered").setValue(true);

                            notificationId = System.currentTimeMillis()
                                    + currentOrder.getMerchantId();
                            CourierInfo currentCourierInfo = new CourierInfo(requestId
                                    , user.getUid(), currentUser.getName()
                                    , currentUser.getProfilePictureURL()
                                    , currentUser.getMobileNumber(), currentUser.getGroup()
                                    , currentUser.getRate(), currentUser.isVerified());
                            DelegatesNotification delegatesNotifications = new DelegatesNotification(
                                    notificationId, "ToDelegate", "Delivered"
                                    , currentOrder.getAcceptedDelegateID(), currentOrder.getOrderId()
                                    , currentOrder.getPackageName(), whichBranch, currentCourierInfo
                            );
                            mUserDatabaseReference.child(currentOrder.getAcceptedDelegateID())
                                    .child("Notifications").child(notificationId).setValue(delegatesNotifications);


                        } else {
                            currentOrder.setDelegateDelivered(true);
                            mOrdersDatabaseReference.child("delegateDelivered").setValue(true);

                            notificationId = System.currentTimeMillis()
                                    + currentOrder.getMerchantId();
                            CourierInfo currentCourierInfo = new CourierInfo(requestId
                                    , user.getUid(), currentUser.getName()
                                    , currentUser.getProfilePictureURL()
                                    , currentUser.getMobileNumber(), currentUser.getGroup()
                                    , currentUser.getRate(), currentUser.isVerified());
                            MerchantsNotifications merchantsNotifications = new MerchantsNotifications(
                                    notificationId, "ToMerchant", "Delivered"
                                    , currentOrder.getMerchantId(), currentOrder.getPackageName()
                                    , currentOrder.getOrderId(), whichBranch, currentCourierInfo
                            );
                            mUserDatabaseReference.child(currentOrder.getMerchantId())
                                    .child("Notifications").child(notificationId).setValue(merchantsNotifications);

                            if (currentOrder.getWhichBranch().equals("AlexOrders")
                                    || currentOrder.getWhichBranch().equals("AlexToCairoOrders"))
                                sendNotificationToPM("AlexPM", currentCourierInfo, "Delivered");
                            else if (currentOrder.getWhichBranch().equals("CairoOrders")
                                    || currentOrder.getWhichBranch().equals("CairoToAlexOrders"))
                                sendNotificationToPM("CairoPM", currentCourierInfo, "Delivered");


                        }

                        if (currentOrder.isDelegateDelivered() && currentOrder.isMerchantDelivered()){
                            mOrdersDatabaseReference.child("packageState").setValue("Delivered");
                            mOrdersDatabaseReference.child("packageStateForPm").setValue("Delivered");
                        }



                        Intent i;
                        if (currentUser.getGroup().equals("AlexMerchants")
                                || currentUser.getGroup().equals("CairoMerchants")) {
                            i = new Intent(OrderDetails.this, MerchantDashboardInsideActivity.class);

                        } else if (currentUser.getGroup().equals("AlexStaticBirds")
                                || currentUser.getMode().equals("CairoStaticBirds")) {
                            i = new Intent(OrderDetails.this, StaticsOrdersDashboardActivity.class);

                        } else if (currentUser.getGroup().equals("AlexFreeBirds")
                                || currentUser.getGroup().equals("CairoFreeBirds")) {
                            i = new Intent(OrderDetails.this, FreesDashboardActivity.class);

                        } else {
                            i = new Intent(OrderDetails.this, InsideOrdersDashboardActivity.class);
                        }
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

    private void confirmDeliveryRequest() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetails.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("تأكيد طلب التوصيل!");
        alertDialog.setMessage("\nبضغتك علي زر موافق سيتطاب منك توصيل الشحنة في خلال الساعات القليلة القادمة \nولا يمكن رفض الطلب بعد قبوله");
        alertDialog.setIcon(R.drawable.ic_cash);

        alertDialog.setPositiveButton("موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String requestId = System.currentTimeMillis() + user.getUid();
                        String notificationId = System.currentTimeMillis()
                                + currentOrder.getMerchantId();
                        CourierInfo currentCourierInfo = new CourierInfo(requestId
                                , user.getUid(), currentUser.getName()
                                , currentUser.getProfilePictureURL()
                                , currentUser.getMobileNumber(), currentUser.getGroup(), currentUser.getRate(), currentUser.isVerified());
                        MerchantsNotifications merchantsNotifications = new MerchantsNotifications(
                                notificationId, "ToMerchant", "OnWay"
                                , currentOrder.getMerchantId(), currentOrder.getPackageName()
                                , currentOrder.getOrderId(), whichBranch, currentCourierInfo
                        );
                        if (currentOrder.getWhichBranch().equals("AlexOrders")
                                || currentOrder.getWhichBranch().equals("AlexToCairoOrders"))
                            sendNotificationToPM("AlexPM", currentCourierInfo, "OnWay");
                        else if (currentOrder.getWhichBranch().equals("CairoOrders")
                                || currentOrder.getWhichBranch().equals("CairoToAlexOrders"))
                            sendNotificationToPM("CairoPM", currentCourierInfo, "OnWay");

                        mOrdersDatabaseReference.child("acceptedDelegateID")
                                .setValue(currentCourierInfo.getUserID());
                        mOrdersDatabaseReference.child("acceptedDelegateName")
                                .setValue(currentCourierInfo.getUserName());
                        mOrdersDatabaseReference.child("acceptedDelegateMobile")
                                .setValue(currentCourierInfo.getUserMobile());
                        mRequestInfoDatabaseReference = mOrdersDatabaseReference.child("currentCourierInfo");
                        mRequestInfoDatabaseReference.child(requestId).setValue(currentCourierInfo);
                        mOrdersDatabaseReference.child("packageState").setValue("UnPicked");
                        mUserDatabaseReference.child(currentOrder.getMerchantId()).child("Notifications").child(notificationId).setValue(merchantsNotifications);
                        dialog.cancel();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.scroll_view), "من فضلك اسرع في استيلام الشحنة", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Intent i;
                        switch (currentUser.getGroup()) {
                            case "AlexFreeBirds":
                            case "CairoFreeBirds":
                                i = new Intent(OrderDetails.this, FreesDashboardActivity.class);
                                i.putExtra("Which_Activity", "SomethingElse");
                                i.putExtra("Purpose", "AfterDeliveryRequest");
                                startActivity(i);
                                break;
                            case "AlexStaticBirds":
                            case "CairoStaticBirds":
                                i = new Intent(OrderDetails.this, StaticsOrdersDashboardActivity.class);
                                i.putExtra("Which_Activity", "SomethingElse");
                                i.putExtra("Purpose", "AfterDeliveryRequest");
                                startActivity(i);
                                break;
                            case "AlexPM":
                            case "CairoPM":
                                if (currentOrder.getLocationInfoForPackage().getsAdminArea()
                                        .equals(currentOrder.getLocationInfoForPackage().getdAdminArea())) {
                                    i = new Intent(OrderDetails.this, InsideOrdersDashboardActivity.class);
                                    i.putExtra("Which_Activity", "SomethingElse");
                                    i.putExtra("Purpose", "AfterDeliveryRequest");
                                    startActivity(i);
                                } else {
                                    i = new Intent(OrderDetails.this, OutsideOrdersDashboardActivity.class);
                                    i.putExtra("Which_Activity", "SomethingElse");
                                    i.putExtra("Purpose", "AfterDeliveryRequest");
                                    startActivity(i);
                                }

                                break;

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

    private void refuseMerchantOffer() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.scroll_view), "مازال بامكانك قبول طلبات التوصيل الاخري", Snackbar.LENGTH_LONG);
        snackbar.show();
        //but now merchants don't know anything about this rejection
        //TODO send notification to merchant to find another delegate(route)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mPMDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        numbersDatabaseReference = mFirebaseDatabase.getReference().child("ZagelNumbers");

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
        deliveredHeadquarters = findViewById(R.id.button_delivered_headQuarters);
        deleteOrder = findViewById(R.id.delete_request);
        showRequestOrder = findViewById(R.id.show_requests);
        refuseOffer = findViewById(R.id.refuse_request);
        acceptOffer = findViewById(R.id.accept_request);

        ownerLayout = findViewById(R.id.layout_owner);
        ownerTextMessage = findViewById(R.id.owner_text_message);
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);
                    if (!currentUser.isVerified()) {
                        final String uGroup = currentUser.getGroup();
                        Intent i = new Intent(OrderDetails.this, NotVerifiedUser.class);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(uGroup)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "succ unsubscribing user in " + uGroup;
                                        if (!task.isSuccessful()) {
                                            msg = "failed unsubscribing user in " + uGroup;
                                        }
                                        Log.e("DetailsActivity", msg);
                                        Toast.makeText(OrderDetails.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        mUserDatabaseReference.child("group").setValue("");
                        finish();
                        startActivity(i);
                    }

                    retrieveDataFromIntent();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);


        } else {
            AuthUI.getInstance().signOut(this);

        }


    }

    private void retrieveDataFromIntent() {
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                switch (key) {
                    case "orderId":
                        orderId = getIntent().getExtras().getString(key);
                        break;
                    case "orderObject":
                        currentOrder = (Orders) getIntent().getSerializableExtra(key);
                        break;
                    case "WhichActivity":
                        whichActivity = getIntent().getExtras().getString(key);
                        break;
                    case "Purpose":
                        purpose = getIntent().getExtras().getString(key);
                        break;
                    case "DelegateNotification":
                        currentDelegateNotification = (DelegatesNotification) getIntent().getSerializableExtra(key);
                        break;
                    case "MerchantNotification":
                        currentMerchantNotification = (MerchantsNotifications) getIntent().getSerializableExtra(key);
                        break;
                    case "WhichBranch":
                        whichBranch = getIntent().getExtras().getString(key);
                        break;
                    case "mOrdersDatabaseReference":
                        mOrdersDatabaseReference = (DatabaseReference) getIntent().getSerializableExtra(key);
                        break;
                }

            }
            prepareTheRetrievedDataToFillActivity();


        }
    }

    private void prepareTheRetrievedDataToFillActivity() {
        switch (whichActivity) {
            case "ordersAdapter":
            case "MDashboardAdapter":
                //came from merchants dashboards and orders Activity
                //it gives Orders object and which order branch, ex. AlexOrders or AlexToCairoOrders
                mOrdersDatabaseReference = mFirebaseDatabase.getReference().child(whichBranch)
                        .child(currentOrder.getOrderId());
                fillViewsWithCorrectData();
                break;
            default:
                //came from fireBase cloud messaging
                // or Notification Adapters
                //it gives orderId and which order branch, ex. AlexOrders or AlexToCairoOrders
                mOrdersDatabaseReference = mFirebaseDatabase.getReference().child(whichBranch)
                        .child(orderId);


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
                break;
        }
    }


    private void fillDataToFrees() {
        if (currentOrder.getAcceptedDelegateID().equals(user.getUid())) {
            fillOrderHandlerIfFree();

        } else if (currentOrder.getAcceptedDelegateID().equals("")) {
            deliveryRequest.setVisibility(View.VISIBLE);
            assignedUserLayout.setVisibility(View.GONE);
            endConsumerLayout.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.GONE);
        } else if (!currentOrder.getAcceptedDelegateID().equals(user.getUid())) {
            deliveryRequest.setVisibility(View.GONE);
            assignedUserLayout.setVisibility(View.GONE);
            endConsumerLayout.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.VISIBLE);
            ownerTextMessage.setText("هذه الشحنة محجوزة اذهب للشحنات المعروضة لتوصيل شحنة اخرى");
        }
    }

    private void fillOrderHandlerIfFree() {
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

        if (!currentOrder.isDelegatePicked()) {
            orderPicked.setVisibility(View.VISIBLE);
            orderPicked.setOnClickListener(OrderDetails.this);
        } else if (currentOrder.getPackageState().equals("Picked") && !currentOrder.isDelegateDelivered()) {
            orderDelivered.setVisibility(View.VISIBLE);
            orderDelivered.setOnClickListener(OrderDetails.this);
        } else if (currentOrder.isDelegateDelivered()) {
            deliveryRequest.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.VISIBLE);
            ownerTextMessage.setText("نتمنى ان تكون سعيد بتجربتك مع زاجل, للشكاوي و المقترحات من فضلك اتصل ب 01275904481 ");

        }
    }

    private void fillDataToStatics() {
        if (currentOrder.getAcceptedDelegateID().equals(user.getUid())
                || currentOrder.getSecondAcceptedDelegateID().equals(user.getUid())) {

            fillOrderHandlerIfStatic();

        } else if (currentOrder.getAcceptedDelegateID().equals("")) {
            deliveryRequest.setVisibility(View.VISIBLE);
            assignedUserLayout.setVisibility(View.GONE);
            endConsumerLayout.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.GONE);
        } else if (!currentOrder.getAcceptedDelegateID().equals(user.getUid())) {
            deliveryRequest.setVisibility(View.GONE);
            assignedUserLayout.setVisibility(View.GONE);
            endConsumerLayout.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.VISIBLE);
            ownerTextMessage.setText("هذه الشحنة محجوزة اذهب للشحنات المعروضة لتوصيل شحنة اخرى");

        }
    }

    private void fillOrderHandlerIfStatic() {
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

        if (!currentOrder.isDelegatePicked()) {
            orderPicked.setVisibility(View.VISIBLE);
            orderPicked.setOnClickListener(OrderDetails.this);
        } else if (currentOrder.getPackageState().equals("Picked")) {
            deliveredHeadquarters.setVisibility(View.VISIBLE);
            deliveredHeadquarters.setOnClickListener(OrderDetails.this);
            if (currentUser.getGroup().equals("AlexPm")
            || currentUser.getGroup().equals("CairoPm"))
                deliveredHeadquarters.setText("هل سلم المندوب الشحنة للمقر؟");

        } else if (currentOrder.getPackageState().equals("CurrentHeadquarters")
                && !currentOrder.isDelegateDelivered()) {
            orderDelivered.setVisibility(View.VISIBLE);
            orderDelivered.setOnClickListener(OrderDetails.this);
        } else if (currentOrder.isDelegateDelivered()) {
            deliveryRequest.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.VISIBLE);
            ownerTextMessage.setText("نتمنى ان تكون سعيد بتجربتك مع زاجل, للشكاوي و المقطرحات من فضلك اتصل ب 01275904481 ");

        }
    }

    private void fillViewsWithCorrectData() {
        progressBar.setVisibility(View.GONE);

        switch (currentUser.getGroup()) {
            case "AlexMerchants":
            case "CairoMerchants":
                if (currentOrder.getAcceptedDelegateID().equals("")) {

                    deliveryRequest.setVisibility(View.GONE);
                    assignedUserLayout.setVisibility(View.GONE);
                    endConsumerLayout.setVisibility(View.GONE);

                    ownerLayout.setVisibility(View.VISIBLE);

                } else if (!currentOrder.getAcceptedDelegateID().equals("")) {

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

                    if (!currentOrder.isMerchantPicked() && currentOrder.isDelegatePicked()) {
                        orderPicked.setText("هل سلمت الشحنة للمندوب؟");
                        orderPicked.setVisibility(View.VISIBLE);
                        orderPicked.setOnClickListener(OrderDetails.this);
                    } else if (!currentOrder.isMerchantPicked() && !currentOrder.isDelegatePicked()) {

                        ownerLayout.setVisibility(View.VISIBLE);
                        ownerTextMessage.setText("سيصلك اشعار تأكيد من المندوب بأنه استلم الشحنة منك للتأكيد");

                    } else if (currentOrder.getPackageState().equals("Picked")
                            && !currentOrder.isDelegateDelivered()
                            && !currentOrder.isMerchantDelivered()) {

                        ownerLayout.setVisibility(View.VISIBLE);
                        ownerTextMessage.setText("سيصلك اشعار تأكيد من المندوب بأنه سلم الشحنة لعميلنا النهائي");

                    } else if (currentOrder.getPackageState().equals("Picked")
                            && currentOrder.isDelegateDelivered()
                            && !currentOrder.isMerchantDelivered()) {
                        orderDelivered.setVisibility(View.VISIBLE);
                        orderDelivered.setOnClickListener(OrderDetails.this);
                    } else if (currentOrder.isMerchantDelivered()) {
                        deliveryRequest.setVisibility(View.GONE);
                        assignedUserLayout.setVisibility(View.GONE);
                        endConsumerLayout.setVisibility(View.GONE);
                        ownerLayout.setVisibility(View.VISIBLE);
                        ownerTextMessage.setText("نتمنى ان تكون سعيد بتجربتك مع زاجل, للشكاوي و المقترحات من فضلك اتصل ب 01275904481 ");

                    }
                }
                break;
            case "AlexFreeBirds":
            case "CairoFreeBirds":
                fillDataToFrees();

                break;
            case "AlexStaticBirds":
            case "CairoStaticBirds":
                fillDataToStatics();
                break;
            case "AlexPM":
            case "CairoPM":
                if (currentOrder.getWhichBranch().equals("AlexToCairoOrders")
                        || currentOrder.getWhichBranch().equals("CairoToAlexOrders")) {
                    if (currentOrder.getAcceptedDelegateID().equals("")) {
                        deliveryRequest.setVisibility(View.VISIBLE);
                        assignedUserLayout.setVisibility(View.GONE);
                        endConsumerLayout.setVisibility(View.GONE);
                        ownerLayout.setVisibility(View.GONE);
                    } else {
                        fillOrderHandlerIfStatic();
                    }


                } else if (currentOrder.getWhichBranch().equals("CairoOrders")
                        || currentOrder.getWhichBranch().equals("AlexOrders")) {
                    if (currentOrder.getAcceptedDelegateID().equals("")) {
                        deliveryRequest.setVisibility(View.VISIBLE);
                        assignedUserLayout.setVisibility(View.GONE);
                        endConsumerLayout.setVisibility(View.GONE);
                        ownerLayout.setVisibility(View.GONE);
                    } else {
                        fillOrderHandlerIfFree();
                    }
                }
                break;
        }
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
            case "Any":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_any_yellow);
                break;
            case "Train":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Car":
                packageVehicleIcon.setImageResource(R.drawable.vehicle_car_yellow);
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
