package com.beta.cls.angelcar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beta.cls.angelcar.fragment.ChatAllFragment;
import com.beta.cls.angelcar.fragment.ChatBuyFragment;
import com.beta.cls.angelcar.fragment.ChatSellFragment;

/**
 * Created by humnoy on 20/1/59.
 */
public class MessageBlogViewPagerAdapter extends FragmentStatePagerAdapter {

    public MessageBlogViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatAllFragment();
            case 1: return new ChatBuyFragment();
            case 2: return new ChatSellFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
