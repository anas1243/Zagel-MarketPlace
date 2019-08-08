package com.example.zagelx.DashboardPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zagelx.R;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class SimpleFragmentPagerAdapterForMerchant extends FragmentPagerAdapter {
    private Context mContext;
    private String userId;
    private Bundle bundle;

    public SimpleFragmentPagerAdapterForMerchant(FragmentManager fm, Context context
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
            NewOrdersFragment fragObj = new NewOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 1){
            NegotiableOrdersFragment fragObj = new NegotiableOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 2){
            ReservedOrdersFragment fragObj = new ReservedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 3){
            PickedOrdersFragment fragObj = new PickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            DeliveredOrdersFragment fragObj = new DeliveredOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.new_orders);
        } else if (position == 1){
            return mContext.getResources().getString(R.string.negotiable_orders);
        } else if (position == 2){
            return mContext.getResources().getString(R.string.reserved_orders);
        }else if (position == 3){
            return mContext.getResources().getString(R.string.picked_orders);
        }else {
            return mContext.getResources().getString(R.string.delivered_orders);
        }
    }
}
