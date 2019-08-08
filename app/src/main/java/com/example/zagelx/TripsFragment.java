package com.example.zagelx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zagelx.Models.Trips;
import com.example.zagelx.TripsPackage.TripsAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {
    private String userId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private ChildEventListener mTripsChildEventListener;


    private TripsAdapter mTripsAdapter;


    public TripsFragment() {
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
        View rootView = inflater.inflate(R.layout.activity_view_pager_list
                , container, false);
        ListView mListView = rootView.findViewById(R.id.list);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTripsDatabaseReference = mFirebaseDatabase.getReference().child("Trips");

        final List<Trips> tipsList = new ArrayList<>();
        mTripsAdapter = new TripsAdapter(getActivity()
                , R.layout.trip_item, tipsList);
        mListView.setAdapter(mTripsAdapter);

        mTripsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Trips currentTrip = dataSnapshot.getValue(Trips.class);
                Log.e("test orders", "onChildAdded: " + currentTrip);
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
        mTripsDatabaseReference.addChildEventListener(mTripsChildEventListener);



        return rootView;
    }

}