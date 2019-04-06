package com.example.zagelx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ListView mOrdersListView;
    private OrdersAdapter mOrdersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOrdersListView = findViewById(R.id.main_list);




        // Initialize message ListView and its adapter
        List<Orders> orders = new ArrayList<>(
                Arrays.asList(
                        new Orders(R.drawable.sora, "Anas Hassan",R.drawable.sora
                                , "Mawad 5am", "20-10-2019"
                                , "70 EGP","ALEXANDRIA", "CAIRO",
                                R.drawable.vehicle_car ),
                        new Orders(R.drawable.sora, "Hassan Mohamed",R.drawable.sora
                                , "7BR", "50-50-2050"
                                , "2000 EGP","CAIRO", "ALEXANDRIA",
                                R.drawable.vehicle_car )
                        )
        );
        mOrdersAdapter = new OrdersAdapter(this, R.layout.order_item, orders);
        mOrdersListView.setAdapter(mOrdersAdapter);
    }
}
