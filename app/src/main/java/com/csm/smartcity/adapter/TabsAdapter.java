package com.csm.smartcity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.csm.smartcity.idea.CatagoryFragment;
import com.csm.smartcity.idea.MostLikedFragment;
import com.csm.smartcity.idea.RecentFragment;


/**
 * Created by arundhati on 8/20/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {


    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new RecentFragment();
            case 1:
                // Games fragment activity
                return new MostLikedFragment();
            case 2:
                // Movies fragment activity
                return new CatagoryFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
