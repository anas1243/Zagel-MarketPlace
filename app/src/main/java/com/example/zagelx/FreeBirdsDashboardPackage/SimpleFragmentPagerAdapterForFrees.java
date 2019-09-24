package com.example.zagelx.FreeBirdsDashboardPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zagelx.R;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class SimpleFragmentPagerAdapterForFrees extends FragmentPagerAdapter {
    private Context mContext;
    private String userId;
    private String uGroup;
    private Bundle bundle;

    public SimpleFragmentPagerAdapterForFrees(FragmentManager fm, Context context
            , String userId, String uGroup) {
        super(fm);
        mContext = context;
        this.userId = userId;
        this.uGroup = uGroup;
        bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("uGroup", uGroup);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            FreeUnPickedOrdersFragment fragObj = new FreeUnPickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 1){
            FreePickedOrdersFragment fragObj = new FreePickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            FreeDeliveredOrdersFragment fragObj = new FreeDeliveredOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getResources().getString(R.string.reserved_orders);
        }else if (position == 1){
            return mContext.getResources().getString(R.string.picked_orders);
        }else {
            return mContext.getResources().getString(R.string.delivered_orders);
        }
    }
}
