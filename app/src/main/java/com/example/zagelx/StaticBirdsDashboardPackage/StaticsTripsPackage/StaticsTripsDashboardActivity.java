package com.example.zagelx.StaticBirdsDashboardPackage.StaticsTripsPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.TripsPackage.AddTripsActivity;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.Utilities.NavDrawerPackage.MerchantDrawerUtil;
import com.example.zagelx.Utilities.NavDrawerPackage.StaticDDrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nex3z.notificationbadge.NotificationBadge;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaticsTripsDashboardActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;

    private String whichActivity;

    private ImageButton notificaitonsButton;
    private ImageView addButton;
    private TextView toolbarText;
    private NotificationBadge mBadge;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    StaticDDrawerUtil drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_view_pager_main_dashboard);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        addButton = findViewById(R.id.add_button);
        toolbarText = findViewById(R.id.toolbar_text);
        toolbarText.setText("رحلاتك المسؤل عنها");
        notificaitonsButton = findViewById(R.id.ic_notification_toolbar);
        notificaitonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaticsTripsDashboardActivity.this, NotificationsActivity.class);
                startActivity(i);
            }
        });

        mBadge = findViewById(R.id.badge);

//        Intent i = getIntent();
//        whichActivity = (String) i.getSerializableExtra("Which_Activity");
//        if (whichActivity.equals("OrderDetails")) {
//            String pickedOrDelivered = (String) i.getSerializableExtra("PickedORDelivered");
//            if (pickedOrDelivered.equals("Picked")) {
//                Snackbar snackbar = Snackbar
//                        .make(findViewById(R.id.main_main_layout), "الرجاء السراع في توصيل الشحنة لتعزيز الثقة بينك و بين التطبيق", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            } else if (pickedOrDelivered.equals("Delivered")) {
//                Snackbar snackbar = Snackbar
//                        .make(findViewById(R.id.main_main_layout), "نتمنا ان تكون قد استمتعت باستخدام التطبيق", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        } else {
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(R.id.main_main_layout), "الرحلات و الشحنات الخاصة بك !", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        }

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);
                    final String[] newToken = new String[1];
                    ButterKnife.bind(StaticsTripsDashboardActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new StaticDDrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(StaticsTripsDashboardActivity.this, toolbar);
                    mBadge.setNumber(currentUser.getNumberOfNotifications());
                    // Find the view pager that will allow the user to swipe between fragments
                    ViewPager viewPager = findViewById(R.id.viewpager);

                    // Create an adapter that knows which fragment should be shown on each page
                    TripsFragmentPagerAdapterForStatics adapter
                            = new TripsFragmentPagerAdapterForStatics(getSupportFragmentManager()
                            , StaticsTripsDashboardActivity.this, user.getUid(), currentUser.getGroup());

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = findViewById(R.id.tabMode);
                    tabLayout.setupWithViewPager(viewPager);

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( StaticsTripsDashboardActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            newToken[0] = instanceIdResult.getToken();
                            Log.e("newToken", newToken[0]);

                            if (dataSnapshot.hasChild("userToken")){
                                if(!currentUser.getUserToken().equals(newToken[0]))
                                    mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);

                            }
                            else{
                                mUserDatabaseReference.child(user.getUid()).child("userToken").setValue(newToken[0]);

                            }

                        }
                    });



                    Animation ranim =  AnimationUtils.loadAnimation(StaticsTripsDashboardActivity.this, R.anim.shake_add_button);
                    addButton.startAnimation(ranim);

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(StaticsTripsDashboardActivity.this, AddTripsActivity.class);
                            startActivity(i);


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
