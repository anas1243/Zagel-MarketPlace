package com.example.zagelx.MainPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class SimpleFragmentPagerAdapterForStart extends FragmentPagerAdapter {
    private Context mContext;
    private String userId;
    private Bundle bundle;

    public SimpleFragmentPagerAdapterForStart(FragmentManager fm, Context context
            , String userId) {
        super(fm);
        mContext = context;
        this.userId = userId;
        bundle = new Bundle();
        bundle.putString("userId", userId);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            OrdersFragment fragObj = new OrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else{
            TripsFragment fragObj = new TripsFragment();
            fragObj.setArguments(bundle);
            return fragObj;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.orders);
        } else {
            return mContext.getResources().getString(R.string.trips);
        }
    }
}
