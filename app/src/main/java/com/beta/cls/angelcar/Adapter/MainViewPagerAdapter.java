package com.beta.cls.angelcar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.beta.cls.angelcar.fragment.FilterFragment;
import com.beta.cls.angelcar.fragment.FinanceFragment;
import com.beta.cls.angelcar.fragment.SampleUploadFileFragment;
import com.beta.cls.angelcar.fragment.HomeFragment;
import com.beta.cls.angelcar.fragment.NoticeFragment;

/**
 * Created by humnoy on 20/1/59.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return HomeFragment.newInstance();
            case 1: return NoticeFragment.newInstance();
            case 2: return FinanceFragment.newInstance();
            case 3: return FilterFragment.newInstance();
            case 4: return new SampleUploadFileFragment();
            default: return HomeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
