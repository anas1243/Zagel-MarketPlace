package com.example.zagelx.PMsDashboardPackage.PMIncomingTripsPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.zagelx.R;

public class IncomingTripsFragmentPagerAdapterForPms extends FragmentPagerAdapter {
    private Context mContext;
    private Bundle bundle;
    public IncomingTripsFragmentPagerAdapterForPms(FragmentManager fm, Context context
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
            IncomingDoneTripsFragment fragObj = new IncomingDoneTripsFragment();
            fragObj.setArguments(bundle);
            return fragObj;
        } else{
            IncomingNotDoneTripsFragment fragObj = new IncomingNotDoneTripsFragment();
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
