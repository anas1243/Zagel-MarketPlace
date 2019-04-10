package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.zagelx.TripsPackage.AddTripsActivity;
import com.example.zagelx.R;
import com.example.zagelx.TripsPackage.TripsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ListView mOrdersListView;
    private OrdersAdapter mOrdersAdapter;
    private Button ordersButton;
    private Button tripsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);

        mOrdersListView = findViewById(R.id.main_list);
        ordersButton = findViewById(R.id.orders_button);
        tripsButton = findViewById(R.id.trips_button);

        ordersButton.setEnabled(false);

        tripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new intent to open the {@link AddTripsActivity}
                Intent i = new Intent(OrdersActivity.this, TripsActivity.class);
                startActivity(i);
            }
        });




        // Initialize message ListView and its adapter
        List<Orders> orders = new ArrayList<>(
                Arrays.asList(
                        new Orders(R.drawable.sora, "Anas Hassan",R.drawable.sora
                                , "Mawad 5am w7sha", "20-10-2019"
                                , "70 EGP","ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car ),
                        new Orders(R.drawable.sora, "Hassan Mohamed",R.drawable.sora
                                , "7BR mlaza2 f7t", "50-50-2050"
                                , "2000 EGP","CAIRO", "ALEXANDRIA",
                                R.drawable.vehicle_car ),
                        new Orders(R.drawable.sora, "Aria mohsen",R.drawable.sora
                                , "Shampoo Gamil", "100-100-2100"
                                , "2000 EGP","ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car )
                        ,
                        new Orders(R.drawable.sora, "yass yass ya",R.drawable.sora
                                , "dmoo3 kter baa", "00-00-2000"
                                , "2000 EGP","KAFR EL DWAR", "ALEXANDRIA",
                                R.drawable.vehicle_car )
                        )
        );
        mOrdersAdapter = new OrdersAdapter(this, R.layout.order_item, orders);
        mOrdersListView.setAdapter(mOrdersAdapter);
    }
}
