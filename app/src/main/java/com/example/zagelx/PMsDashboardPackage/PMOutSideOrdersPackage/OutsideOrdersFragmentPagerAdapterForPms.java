package com.example.zagelx.PMsDashboardPackage.PMOutSideOrdersPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class OutsideOrdersFragmentPagerAdapterForPms extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public OutsideOrdersFragmentPagerAdapterForPms(FragmentManager fm, Context context
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
            OutSideNewOrdersFragment fragObj = new OutSideNewOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if (position == 1){
            OutSideUnPickedOrdersFragment fragObj = new OutSideUnPickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else if(position == 2){
            OutSidePickedOrdersFragment fragObj = new OutSidePickedOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else if(position == 3){
            OutSideHeadQuarOrdersFragment fragObj = new OutSideHeadQuarOrdersFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        }else{
            OutSideOtherHeadQuarOrdersFragment fragObj = new OutSideOtherHeadQuarOrdersFragment();
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
        }else if (position == 1){
            return mContext.getResources().getString(R.string.reserved_orders);
        }else if (position == 2){
            return mContext.getResources().getString(R.string.picked_orders);
        }else if (position == 3){
            return mContext.getResources().getString(R.string.current_headquarters_orders);
        }else {
            return mContext.getResources().getString(R.string.other_headquarters_orders);
        }
    }
}
