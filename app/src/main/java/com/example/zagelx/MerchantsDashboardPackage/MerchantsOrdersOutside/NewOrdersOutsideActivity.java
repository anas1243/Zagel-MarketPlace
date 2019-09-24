package com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersOutside;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.NewOrdersInsideFragment;
import com.example.zagelx.R;

public class NewOrdersOutsideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new NewOrdersOutsideFragment())
                .commit();
    }
}