package com.beta.cls.angelcar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by humnoy on 11/3/59.
 */
public class PhotoAdapter extends FragmentPagerAdapter{
    int countPage = 0;
    public PhotoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }
}
