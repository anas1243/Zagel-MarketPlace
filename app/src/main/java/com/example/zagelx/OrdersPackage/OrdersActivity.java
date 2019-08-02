package com.example.zagelx.OrdersPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.zagelx.Authentication.AfterRegisterUserInfo;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.TripsPackage.TripsActivity;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.Utilities.DrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends AppCompatActivity {

    private ListView mOrdersListView;
    private OrdersAdapter mOrdersAdapter;
    private Button ordersButton;
    private Button tripsButton;
    private Button addOrderCButton;
    private ImageButton notificaitonsButton;
//    private ProgressBar progressBar;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;


    private NotificationBadge mBadge;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");


        mOrdersListView = findViewById(R.id.main_list);
        ordersButton = findViewById(R.id.orders_button);
        tripsButton = findViewById(R.id.trips_button);
        addOrderCButton = findViewById(R.id.add_order_cbutton);

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


        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to open the {@link AddOrdersActivity}
//                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    Intent i = new Intent(OrdersActivity.thiselectedImageUri.getLastPathSegment()s, AddOrdersMapActivity.class);
//                    startActivity(i);
//                } else {
//                    isGPSopened();
//                }
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_main_layout), "Already there!", Snackbar.LENGTH_SHORT);
                snackbar.show();

            }
        });
        tripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to open the {@link AddTripsActivity}

                Intent i = new Intent(OrdersActivity.this, TripsActivity.class);
                startActivity(i);
            }
        });

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "الشحنات !", Snackbar.LENGTH_LONG);
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
                    final String[] newToken = new String[1];
                    ButterKnife.bind(OrdersActivity.this);
                    setSupportActionBar(toolbar);
                    mBadge.setNumber(currentUser.getNumberOfNotifications());

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL());
                    drawer.getDrawer(OrdersActivity.this, toolbar);
                    if (!currentUser.getMode().equals("Merchant")) {
                        setAddOrderButtonListenerInActive();
                    } else {
                        setAddOrderButtonListenerActive();
                    }

                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( OrdersActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                newToken[0] = instanceIdResult.getToken();
                                if(!currentUser.getUserToken().equals(newToken[0])){
                                    mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);

                                }
                                Log.e("newToken", newToken[0]);

                            }
                        });

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
                    if(orders.getPackageState().equals("New")
                    || orders.getPackageState().equals("Negotiable")
                    )
                    mOrdersAdapter.add(orders);

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

    private void setAddOrderButtonListenerInActive(){
        addOrderCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_main_layout), "انت لست تاجر,   \nيمكنل التواصل مع فريق عمل زاجل لتغير نوع الحساب", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void setAddOrderButtonListenerActive() {
        addOrderCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersActivity.this);

                alertDialog.setTitle("Pin on Map?");

                alertDialog.setMessage("do you want to set a pin on the map" +
                        " to make the process more easy and accurate");

                alertDialog.setPositiveButton("Yes Open the Map", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent i = new Intent(OrdersActivity.this, AddOrdersMapActivity.class);
                            startActivity(i);
                        } else {
                            isGPSopened();
                        }
                    }
                });

                alertDialog.setNegativeButton("i don't have the accurate location", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(OrdersActivity.this, AddOrdersActivity.class);
                        i.putExtra("FROM_ACTIVITY", "OrdersActivity");
                        startActivity(i);
                    }
                });

                alertDialog.show();


            }
        });
    }

    private void isGPSopened() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled) {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersActivity.this);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                OrdersActivity.this.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
