package com.himorfosis.kasirmegono.Admin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LaporanPager extends FragmentPagerAdapter {

    int mNumOfTabs;

    public LaporanPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LaporanHarian tab1 = new LaporanHarian();
                return tab1;
            case 1:
                LaporanSemua tab2 = new LaporanSemua();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
