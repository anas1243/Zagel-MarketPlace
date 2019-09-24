package com.example.zagelx.PMsDashboardPackage.PMOutGoingTripsPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class OutgoingTripsFragmentPagerAdapterForPms extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;

    public OutgoingTripsFragmentPagerAdapterForPms(FragmentManager fm, Context context
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
            OutgoingDoneTripsFragment fragObj = new OutgoingDoneTripsFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else{
            OutgoingNotDoneTripsFragment fragObj = new OutgoingNotDoneTripsFragment();
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
            return mContext.getResources().getString(R.string.done_trips);
        } else {
            return mContext.getResources().getString(R.string.not_done_trips);
        }
    }
}
