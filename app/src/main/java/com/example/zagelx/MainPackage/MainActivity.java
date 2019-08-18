package com.example.zagelx.MainPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.zagelx.Models.Users;
import com.example.zagelx.OrdersPackage.AddOrdersActivity;
import com.example.zagelx.R;
import com.example.zagelx.TripsPackage.AddTripsActivity;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.Utilities.DrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;

    private NotificationBadge mBadge;
    private ImageButton notificaitonsButton;

    private ImageView addButton;
    private String mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_view_pager_main_start);


        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");


        addButton = findViewById(R.id.add_button);
        mBadge = findViewById(R.id.badge);


        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "الشحنات !", Snackbar.LENGTH_LONG);
        snackbar.show();


        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);




                    mode = currentUser.getMode();
                    ButterKnife.bind(MainActivity.this);
                    setSupportActionBar(toolbar);

                    notificaitonsButton = findViewById(R.id.ic_notification_toolbar);
                    notificaitonsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this, NotificationsActivity.class);
                            startActivity(i);
                        }
                    });
                    mBadge.setNumber(currentUser.getNumberOfNotifications());

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(MainActivity.this, toolbar);
                    // Find the view pager that will allow the user to swipe between fragments
                    ViewPager viewPager = findViewById(R.id.viewpager);

                    // Create an adapter that knows which fragment should be shown on each page
                    SimpleFragmentPagerAdapterForStart adapter
                            = new SimpleFragmentPagerAdapterForStart(getSupportFragmentManager()
                            , MainActivity.this, user.getUid());

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = findViewById(R.id.tabMode);
                    tabLayout.setupWithViewPager(viewPager);

                    Animation ranim =  AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_add_button);
                    addButton.startAnimation(ranim);

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mode.equals("Merchant")) {
                                setAddOrderButtonListenerActive();
                            } else if (mode.equals("Delivery Delegate")) {
                                Intent i = new Intent(MainActivity.this, AddTripsActivity.class);
                                startActivity(i);
                            }

                        }
                    });
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


    private void setAddOrderButtonListenerActive() {
                    //this was here when AddOrderMapActivity was here
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//
//                alertDialog.setTitle("Pin on Map?");
//
//                alertDialog.setMessage("do you want to set a pin on the map" +
//                        " to make the process more easy and accurate");
//
//                alertDialog.setPositiveButton("Yes Open the Map", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                            Intent i = new Intent(MainActivity.this, AddOrdersMapActivity.class);
//                            startActivity(i);
//                        } else {
//                            isGPSopened();
//                        }
//                    }
//                });
//
//                alertDialog.setNegativeButton("i don't have the accurate location", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Intent i = new Intent(MainActivity.this, AddOrdersActivity.class);
//                        i.putExtra("FROM_ACTIVITY", "MainActivity");
//                        startActivity(i);
//                    }
//                });
//
//                alertDialog.show();

        Intent i = new Intent(MainActivity.this, AddOrdersActivity.class);
        startActivity(i);


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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivity(intent);
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
