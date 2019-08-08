package com.example.zagelx.DashboardPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zagelx.R;

public class SimpleFragmentPagerAdapterForDelegate extends FragmentPagerAdapter {
    private Context mContext;
    private String userId;
    private Bundle bundle;

    public SimpleFragmentPagerAdapterForDelegate(FragmentManager fm, Context context
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
            DelegateOrdersFragment fragObj = new DelegateOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else{
            DelegateTripsFragment fragObj = new DelegateTripsFragment();
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
            return mContext.getResources().getString(R.string.delegate_orders);
        } else {
            return mContext.getResources().getString(R.string.delegate_trips);
        }
    }
}
