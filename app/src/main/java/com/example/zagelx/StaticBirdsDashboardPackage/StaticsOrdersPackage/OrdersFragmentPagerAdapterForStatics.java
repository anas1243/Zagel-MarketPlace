package com.example.zagelx.StaticBirdsDashboardPackage.StaticsOrdersPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class OrdersFragmentPagerAdapterForStatics extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public OrdersFragmentPagerAdapterForStatics(FragmentManager fm, Context context
            , String userId, String uGroup) {
        super(fm);
        mContext = context;
        bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("uGroup", uGroup);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            StaticUnPickedOrdersFragment fragObj = new StaticUnPickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 1){
            StaticPickedOrdersFragment fragObj = new StaticPickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            StaticHeadQuarOrdersFragment fragObj = new StaticHeadQuarOrdersFragment();
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
            return mContext.getResources().getString(R.string.current_headquarters_orders);
        }
    }
}
