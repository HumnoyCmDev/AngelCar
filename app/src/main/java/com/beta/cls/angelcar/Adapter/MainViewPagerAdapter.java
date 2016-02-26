package com.beta.cls.angelcar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.beta.cls.angelcar.fragment.FilterFragment;
import com.beta.cls.angelcar.fragment.FinanceFragment;
import com.beta.cls.angelcar.fragment.HelpFragment;
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
            case 0: return new HomeFragment();
            case 1: return new NoticeFragment();
            case 2: return new FinanceFragment();
            case 3: return new FilterFragment();
            case 4: return HelpFragment.newInstance();//HelpFragment
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
