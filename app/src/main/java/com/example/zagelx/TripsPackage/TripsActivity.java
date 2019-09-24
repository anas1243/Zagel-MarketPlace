package com.example.zagelx.TripsPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.MerchantDashboardInsideActivity;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.Utilities.NavDrawerPackage.MerchantDrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripsActivity extends AppCompatActivity {

    private ListView mTripsListView;
    private TripsAdapter mTripsAdapter;
    private ImageButton notificaitonsButton;
    private NotificationBadge mBadge;
//    private ProgressBar progressBar;



    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MerchantDrawerUtil drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");


        mTripsListView = findViewById(R.id.main_list);
        //progressBar = findViewById(R.id.progressbar);

        notificaitonsButton = findViewById(R.id.ic_notification_toolbar);
        notificaitonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripsActivity.this, NotificationsActivity.class);
                startActivity(i);
            }
        });

        mBadge = findViewById(R.id.badge);

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "خطوط مندوبي الشحن !", Snackbar.LENGTH_LONG);
        snackbar.show();

        // Initialize message ListView and its adapter
        List<Trips> tripsList = new ArrayList<>();
        mTripsAdapter = new TripsAdapter(this, R.layout.order_item, tripsList);
        mTripsListView.setAdapter(mTripsAdapter);

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(TripsActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new MerchantDrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(),currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(TripsActivity.this, toolbar);
                    mBadge.setNumber(currentUser.getNumberOfNotifications());

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
                    Trips currentTrip = dataSnapshot.getValue(Trips.class);
                    if (!currentTrip.isFullRoute())
                    mTripsAdapter.add(currentTrip);

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
            mTripsDatabaseReference.addChildEventListener(mChildEventListener);

        } else {
            AuthUI.getInstance().signOut(this);

        }
    }

    @Override
    public void onBackPressed() {

            Intent i = new Intent(TripsActivity.this, MerchantDashboardInsideActivity.class);
            i.putExtra("Which_Activity", "SomethingElse");
            startActivity(i);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

                Intent i = new Intent(TripsActivity.this, MerchantDashboardInsideActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                startActivity(i);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
