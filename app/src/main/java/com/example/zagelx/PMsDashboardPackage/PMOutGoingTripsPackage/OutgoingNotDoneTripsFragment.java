package com.example.zagelx.PMsDashboardPackage.PMOutGoingTripsPackage;

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
import com.example.zagelx.R;
import com.example.zagelx.TripsPackage.TripsAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OutgoingNotDoneTripsFragment extends Fragment {
    private String userId;
    private String uGroup;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTripsDatabaseReference;
    private ChildEventListener mTripsChildEventListener;


    private TripsAdapter mTripsAdapter;


    public OutgoingNotDoneTripsFragment() {
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
            //outgoing means it goes from alex to cairo
            mTripsDatabaseReference = mFirebaseDatabase.getReference().child("AlexToCairoTrips");
        }else if (uGroup.equals("CairoPM")){
            mTripsDatabaseReference = mFirebaseDatabase.getReference().child("CairoToAlexTrips");
        }

        final List<Trips> tipsList = new ArrayList<>();
        mTripsAdapter = new TripsAdapter(getActivity()
                , R.layout.trip_item, tipsList);
        mListView.setAdapter(mTripsAdapter);

        mTripsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Trips currentTrip = dataSnapshot.getValue(Trips.class);
                Log.e("test orders", "onChildAdded: " + currentTrip);
                if( !currentTrip.isPassedToOtherSide()
                        && !currentTrip.isStatus()){
                    mTripsAdapter.notifyDataSetChanged();
                    mTripsAdapter.add(currentTrip);
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
        mTripsDatabaseReference.addChildEventListener(mTripsChildEventListener);



        return rootView;
    }

}