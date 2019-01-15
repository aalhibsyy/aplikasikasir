package com.himorfosis.kasirmegono.Mitra;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RiwayatPager extends FragmentPagerAdapter {

    int mNumOfTabs;

    public RiwayatPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RiwayatHarian tab1 = new RiwayatHarian();
                return tab1;
            case 1:
                RiwayatSemua tab2 = new RiwayatSemua();
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
