package com.example.zagelx.MainPackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.zagelx.R;

public class MainOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new OrdersFragment())
                .commit();
    }
}