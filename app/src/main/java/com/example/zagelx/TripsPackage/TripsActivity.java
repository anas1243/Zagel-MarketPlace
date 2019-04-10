package com.example.zagelx.TripsPackage;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.zagelx.OrdersPackage.AddOrdersActivity;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripsActivity extends AppCompatActivity {

    private ListView mtripsListView;
    private TripsAdapter mTripsAdapter;
    private Button tripsButton;
    private Button ordersButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_activity);

        mtripsListView = findViewById(R.id.main_list);
        ordersButton = findViewById(R.id.orders_button);
        tripsButton = findViewById(R.id.trips_button);

        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to open the {@link AddTripsActivity}
                Intent i = new Intent(TripsActivity.this, OrdersActivity.class);
                startActivity(i);
            }
        });

        tripsButton.setEnabled(false);


        // Initialize message ListView and its adapter
        List<Trips> trips = new ArrayList<>(
                Arrays.asList(
                        new Trips(R.drawable.sora, "HAMO BEKA", "100-100-2100"
                                , "100 EGP", "ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car),
                        new Trips(R.drawable.sora, "KING OF THE NORTH", "50-50-2050"
                                , "200 EGP", "ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car),
                        new Trips(R.drawable.sora, "THE KNIGHT KING", "0-0-2000"
                                , "000 EGP", "ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car)
                        ,
                        new Trips(R.drawable.sora, "MAGDY SHATA", "30-30-2030"
                                , "70 EGP", "ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car)
                )
        );
        mTripsAdapter = new TripsAdapter(this, R.layout.trip_item, trips);
        mtripsListView.setAdapter(mTripsAdapter);
    }
}
