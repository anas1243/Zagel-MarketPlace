package com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersOutside;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zagelx.R;

public class FragmentPagerAdapterForMerchantOutside extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public FragmentPagerAdapterForMerchantOutside(FragmentManager fm, Context context
            , String userId, String uGroup) {
        super(fm);
        mContext = context;
        bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("uGroup", uGroup);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            NewOrdersOutsideFragment fragObj = new NewOrdersOutsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 1){
            UnPickedOrdersOutsideFragment fragObj = new UnPickedOrdersOutsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 2){
            PickedOrdersOutsideFragment fragObj = new PickedOrdersOutsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            DeliveredOrdersOutsideFragment fragObj = new DeliveredOrdersOutsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.new_orders);
        }else if (position == 1){
            return mContext.getResources().getString(R.string.reserved_orders);
        }else if (position == 2){
            return mContext.getResources().getString(R.string.picked_orders);
        }else {
            return mContext.getResources().getString(R.string.delivered_orders);
        }
    }
}
