package com.example.zagelx.UserInfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.Utilities.DrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {
    private ListView mListView;
    private DashboardOrdersAdapter mOrdersAdapter;
    private DashboardTipsAdapter mTripsAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference mTripsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;

    private Query queryOrders;
    private Query queryTrips;
    private String userType;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");
        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mListView = findViewById(R.id.main_list);


        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "الشحنات الخاصة بك !", Snackbar.LENGTH_LONG);
        snackbar.show();



        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(DashboardActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(),currentUser.getProfilePictureURL());
                    drawer.getDrawer(DashboardActivity.this, toolbar);
                    userType = currentUser.getMode();
                    Log.e("testtttttttttt", "onDataChange: "+userType+"  merchant el mafrod" );

                    if(userType.equals("Merchant")){
                        final List<Orders> ordersList = new ArrayList<>();
                        mOrdersAdapter = new DashboardOrdersAdapter(DashboardActivity.this
                                , R.layout.order_list_dashboard, ordersList);
                        mListView.setAdapter(mOrdersAdapter);


                        queryOrders = mOrdersDatabaseReference.orderByChild("merchantId").equalTo(user.getUid());
                        queryOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot order : dataSnapshot.getChildren()) {
                                        Orders currentOrder = order.getValue(Orders.class);
                                        mOrdersAdapter.add(currentOrder);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{

                        final List<Trips> TripsList = new ArrayList<>();
                        mTripsAdapter = new DashboardTipsAdapter(DashboardActivity.this
                                , R.layout.trip_item, TripsList);
                        mListView.setAdapter(mTripsAdapter);

                        queryTrips = mTripsDatabaseReference.orderByChild("delegateID").equalTo(user.getUid());
                        queryTrips.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot trip : dataSnapshot.getChildren()) {
                                        Trips currentTrip = trip.getValue(Trips.class);
                                        mTripsAdapter.add(currentTrip);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


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
}
