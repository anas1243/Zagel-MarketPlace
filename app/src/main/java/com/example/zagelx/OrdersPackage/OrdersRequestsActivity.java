package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersRequestsActivity extends AppCompatActivity {

    private ListView mListView;
    private OrderRequestsAdapter mRequestsAdapter;

    private DatabaseReference mRequestsDatabaseReference, mOrdersDatabaseReference;
    private DatabaseReference mUserDatabaseReference;

    private FirebaseUser user;
    private Users currentUser;
    private Orders currentOrder;
    private String whichActivity;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        String currentOrderId = (String) i.getSerializableExtra("orderId");
        whichActivity = (String) i.getSerializableExtra("WhichActivity");
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mOrdersDatabaseReference = mFirebaseDatabase.getReference("Orders/"+ currentOrderId);
        mRequestsDatabaseReference = mOrdersDatabaseReference.child("currentRequestInfo");

        mListView = findViewById(R.id.requests_list);


        ValueEventListener mOrderEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentOrder = dataSnapshot.getValue(Orders.class);

                getTheCurrentUser();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mOrdersDatabaseReference.addListenerForSingleValueEvent(mOrderEventListener);

//        delegateId = currentNotification.getRequestInfo().getUserID();
//         notificationId = System.currentTimeMillis() + delegateId;
//         requestId = System.currentTimeMillis() + currentNotification.getMerchantId();

//        mNotificationDatabaseReference = mFirebaseDatabase.getReference("Users/"
//                + delegateId + "/Notifications/" + notificationId);


    }

    private void getTheCurrentUser(){

        if (user != null) {

            ValueEventListener mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(OrdersRequestsActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(OrdersRequestsActivity.this, toolbar);


                    final List<RequestInfo> requestsList = new ArrayList<>();
                    mRequestsAdapter = new OrderRequestsAdapter(OrdersRequestsActivity.this
                            , R.layout.request_item, requestsList, mOrdersDatabaseReference, currentOrder, currentUser);
                    mListView.setAdapter(mRequestsAdapter);

                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.main_main_layout), "Order Requests", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    showAllRequests();


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

    private void showAllRequests(){
        //progressBar.setVisibility(View.GONE);
        ChildEventListener mRequestsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RequestInfo currentRequestInfo = dataSnapshot.getValue(RequestInfo.class);
                if (currentRequestInfo.getStatus().equals("pending"))
                    mRequestsAdapter.add(currentRequestInfo);

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
        mRequestsDatabaseReference.addChildEventListener(mRequestsChildEventListener);
    }

//    @Override
//    public void onBackPressed() {
////        Intent i = new Intent(NotificationsActivity.this, OrdersActivity.class);
////        finish();
////        startActivity(i);
//    }
}
