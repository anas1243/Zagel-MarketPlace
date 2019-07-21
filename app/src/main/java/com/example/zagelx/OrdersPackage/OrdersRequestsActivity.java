package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.UserInfo.NotificationsActivity;
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

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRequestsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;
    private String currentOrderId, currentOrderName;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Intent i = getIntent();
        currentOrderId = (String) i.getSerializableExtra("Package_ID");
        currentOrderName = (String) i.getSerializableExtra("Package_NAME");

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRequestsDatabaseReference = mFirebaseDatabase
                .getReference().child("Orders/"+currentOrderId+"/currentRequestInfo");

        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mListView = findViewById(R.id.requests_list);

        final List<RequestInfo> requestsList = new ArrayList<>();
        mRequestsAdapter = new OrderRequestsAdapter(OrdersRequestsActivity.this
                , R.layout.request_item, requestsList, currentOrderName);
        mListView.setAdapter(mRequestsAdapter);

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "Order Requests", Snackbar.LENGTH_SHORT);
        snackbar.show();

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(OrdersRequestsActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL());
                    drawer.getDrawer(OrdersRequestsActivity.this, toolbar);

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
                    RequestInfo currentRequestInfo = dataSnapshot.getValue(RequestInfo.class);
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
            mRequestsDatabaseReference.addChildEventListener(mChildEventListener);



        } else {
            AuthUI.getInstance().signOut(this);

        }

    }

//    @Override
//    public void onBackPressed() {
////        Intent i = new Intent(NotificationsActivity.this, OrdersActivity.class);
////        finish();
////        startActivity(i);
//    }
}
