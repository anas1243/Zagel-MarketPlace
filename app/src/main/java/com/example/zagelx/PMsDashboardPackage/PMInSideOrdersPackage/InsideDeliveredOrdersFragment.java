package com.example.zagelx.PMsDashboardPackage.PMInSideOrdersPackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zagelx.Models.Orders;
import com.example.zagelx.OrdersPackage.OrdersAdapter;
import com.example.zagelx.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InsideDeliveredOrdersFragment extends Fragment {
    private String userId;
    private String uGroup;
    private String whichBranch;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mOrdersChildEventListener;


    private OrdersAdapter mOrdersAdapter;


    public InsideDeliveredOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userId = getArguments().getString("userId");
        uGroup = getArguments().getString("uGroup");
        View rootView = inflater.inflate(R.layout.activity_view_pager_list
                , container, false);
        ListView mListView = rootView.findViewById(R.id.list);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        if (uGroup.equals("AlexPM")){
            whichBranch = "AlexOrders";
            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child(whichBranch);
        }else if (uGroup.equals("CairoPM")){
            whichBranch = "CairoOrders";
            mOrdersDatabaseReference = mFirebaseDatabase.getReference().child(whichBranch);
        }
        final List<Orders> ordersList = new ArrayList<>();
        mOrdersAdapter = new OrdersAdapter(getActivity()
                , R.layout.order_item, ordersList, whichBranch);
        mListView.setAdapter(mOrdersAdapter);

        mOrdersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Orders orders = dataSnapshot.getValue(Orders.class);
                Log.e("test orders", "onChildAdded: " + orders);
                if(orders.getPackageStateForPm().equals("Delivered")){
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
        mOrdersDatabaseReference.addChildEventListener(mOrdersChildEventListener);



        return rootView;
    }

}