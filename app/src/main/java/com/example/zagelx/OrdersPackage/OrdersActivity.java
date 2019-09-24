package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.MerchantDashboardInsideActivity;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.Utilities.NavDrawerPackage.FreeDDrawerUtil;
import com.example.zagelx.Utilities.NavDrawerPackage.MerchantDrawerUtil;
import com.example.zagelx.Utilities.NavDrawerPackage.StaticDDrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends AppCompatActivity {

    private ListView mOrdersListView;
    private OrdersAdapter mOrdersAdapter;
    private ImageButton notificaitonsButton;
//    private ProgressBar progressBar;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;
    private DataSnapshot PublicdataSnapshot;

    private NotificationBadge mBadge;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MerchantDrawerUtil drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");


        mOrdersListView = findViewById(R.id.main_list);

        notificaitonsButton = findViewById(R.id.ic_notification_toolbar);
        notificaitonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrdersActivity.this, NotificationsActivity.class);
                startActivity(i);
            }
        });

        mBadge = findViewById(R.id.badge);
        //progressBar = findViewById(R.id.progressbar);

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "الشحنات المعروضة !", Snackbar.LENGTH_LONG);
        snackbar.show();

        // Initialize message ListView and its adapter
        final List<Orders> ordersList = new ArrayList<>();
        mOrdersAdapter = new OrdersAdapter(OrdersActivity.this, R.layout.order_item, ordersList);
        mOrdersListView.setAdapter(mOrdersAdapter);


        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);
                    PublicdataSnapshot = dataSnapshot;
                    ButterKnife.bind(OrdersActivity.this);
                    setSupportActionBar(toolbar);
                    mBadge.setNumber(currentUser.getNumberOfNotifications());

                    drawer = new MerchantDrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(OrdersActivity.this, toolbar);

                    checkTokenAndGroup();

                    switch (currentUser.getGroup()) {
                        case "AlexFreeBirds":
                            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("AlexOrders");
                            FreeDDrawerUtil drawer = new FreeDDrawerUtil(currentUser.getName()
                                    , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                            drawer.getDrawer(OrdersActivity.this, toolbar);
                            break;
                        case "CairoFreeBirds":
                            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("CairoOrders");
                            FreeDDrawerUtil drawer1 = new FreeDDrawerUtil(currentUser.getName()
                                    , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                            drawer1.getDrawer(OrdersActivity.this, toolbar);
                            break;
                        case "AlexStaticBirds":
                            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("AlexToCairoOrders");
                            StaticDDrawerUtil drawer2 = new StaticDDrawerUtil(currentUser.getName()
                                    , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                            drawer2.getDrawer(OrdersActivity.this, toolbar);
                            break;
                        case "CairoStaticBirds":
                            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("CairoToAlexOrders");
                            StaticDDrawerUtil drawer3 = new StaticDDrawerUtil(currentUser.getName()
                                    , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                            drawer3.getDrawer(OrdersActivity.this, toolbar);
                            break;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);


            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Orders orders = dataSnapshot.getValue(Orders.class);
                    Log.e("test orders", "onChildAdded: " + orders);
                    if(orders.getPackageState().equals("New")){
                        mOrdersAdapter.notifyDataSetChanged();
                        mOrdersAdapter.add(orders);
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
            mOrdersDatabaseReference.addChildEventListener(mChildEventListener);

        } else {
            AuthUI.getInstance().signOut(this);

        }
    }

    private void checkTokenAndGroup(){
        final String[] newToken = new String[1];
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(OrdersActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken[0] = instanceIdResult.getToken();
                Log.e("newToken", newToken[0]);
                if (currentUser.getGroup().equals("")) {
                    mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);
                    //it means that this user was banned from the app before
                    switch (currentUser.getMode()){
                        case "Alex PM":
                            subscribeUserInAGroup("AlexPM");
                            break;
                        case "Cairo PM":
                            subscribeUserInAGroup("CairoPM");
                            break;
                        case "Merchant":
                            if (currentUser.getLocationInfoForUser().getuAdminArea().equals("الإسكندرية")) {
                                subscribeUserInAGroup("AlexMerchants");
                            } else if (currentUser.getLocationInfoForUser().getuAdminArea().equals("القاهرة")
                                    || currentUser.getLocationInfoForUser().getuAdminArea().equals("الجيزة")) {
                                subscribeUserInAGroup("CairoMerchants");
                            }
                            break;
                        case "Alex Static Birds":
                            subscribeUserInAGroup("AlexStaticBirds");
                            break;
                        case "Cairo Static Birds":
                            subscribeUserInAGroup("CairoStaticBirds");
                            break;
                        case "Delivery Delegate":
                            if (currentUser.getLocationInfoForUser().getuAdminArea().equals("الإسكندرية")) {
                                subscribeUserInAGroup("AlexFreeBirds");
                            } else if (currentUser.getLocationInfoForUser().getuAdminArea().equals("القاهرة")
                                    || currentUser.getLocationInfoForUser().getuAdminArea().equals("الجيزة")) {
                                subscribeUserInAGroup("CairoFreeBirds");
                            }
                            break;
                    }
                } else if (PublicdataSnapshot.hasChild("userToken")) {
                    if (!currentUser.getUserToken().equals(newToken[0])) {
                        mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);
                        subscribeUserInAGroup(currentUser.getGroup());
                    }
                } else {
                    mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);
                    subscribeUserInAGroup(currentUser.getGroup());
                }
            }
        });
    }

    public void subscribeUserInAGroup(final String uGroup) {
        FirebaseMessaging.getInstance().subscribeToTopic(uGroup)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "succ subscribing user in " + uGroup;
                        if (!task.isSuccessful()) {
                            msg = "failed subscribing user in " + uGroup;
                        }
                        Log.e("homeActivity", msg);
                        Toast.makeText(OrdersActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OrdersActivity.this, DelegateDashboardActivity.class);
        i.putExtra("Which_Activity", "another_activity");
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

                Intent i = new Intent(OrdersActivity.this, DelegateDashboardActivity.class);
                i.putExtra("Which_Activity", "another_activity");
                startActivity(i);


            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
