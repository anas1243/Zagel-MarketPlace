package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.zagelx.Models.Orders;
import com.example.zagelx.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ListView mOrdersListView;
    private OrdersAdapter mOrdersAdapter;
    private Button ordersButton;
    private Button tripsButton;
    private ProgressBar progressBar;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("Orders");

        mOrdersListView = findViewById(R.id.main_list);
        ordersButton = findViewById(R.id.orders_button);
        tripsButton = findViewById(R.id.trips_button);
        ordersButton.setEnabled(false);
        progressBar = findViewById(R.id.progressbar);

        tripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to open the {@link AddTripsActivity}
                Intent i = new Intent(OrdersActivity.this, AddOrdersActivity.class);
                startActivity(i);
            }
        });

        // Initialize message ListView and its adapter
        List<Orders> ordersList = new ArrayList<>();
        mOrdersAdapter = new OrdersAdapter(this, R.layout.order_item, ordersList);
        mOrdersListView.setAdapter(mOrdersAdapter);

        if (user != null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Orders orders = dataSnapshot.getValue(Orders.class);
                    mOrdersAdapter.add(orders);
                    progressBar.setVisibility(View.GONE);
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
}
