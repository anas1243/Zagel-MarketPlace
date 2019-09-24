package com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zagelx.R;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class FragmentPagerAdapterForMerchantInside extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public FragmentPagerAdapterForMerchantInside(FragmentManager fm, Context context
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
            NewOrdersInsideFragment fragObj = new NewOrdersInsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 1){
            UnPickedOrdersInsideFragment fragObj = new UnPickedOrdersInsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 2){
            PickedOrdersInsideFragment fragObj = new PickedOrdersInsideFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            DeliveredOrdersInsideFragment fragObj = new DeliveredOrdersInsideFragment();
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
