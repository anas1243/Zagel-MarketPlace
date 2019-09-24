package com.example.zagelx.PMsDashboardPackage.PMInSideOrdersPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class InsideOrdersFragmentPagerAdapterForPms extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public InsideOrdersFragmentPagerAdapterForPms(FragmentManager fm, Context context
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
            InsideNewOrdersFragment fragObj = new InsideNewOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 1){
            InsideUnPickedOrdersFragment fragObj = new InsideUnPickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 2){
            InsidePickedOrdersFragment fragObj = new InsidePickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            InsideDeliveredOrdersFragment fragObj = new InsideDeliveredOrdersFragment();
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
