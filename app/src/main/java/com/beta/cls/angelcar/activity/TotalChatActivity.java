package com.beta.cls.angelcar.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.fragment.ChatAllFragment;
import com.beta.cls.angelcar.fragment.ChatBuyFragment;
import com.beta.cls.angelcar.fragment.ChatSellFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TotalChatActivity extends AppCompatActivity {
    @Bind(R.id.activity_chat_tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toltal_chat);
        ButterKnife.bind(this);
        initTabLayout();
        initViewPager();
        initListentnerTab();

    }

    private void initViewPager() {
        TotalChatViewPagerAdapter pagerAdapter = new TotalChatViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("ทั้งหมด"));
        tabLayout.addTab(tabLayout.newTab().setText("คุยกับคนซื้อ"));
        tabLayout.addTab(tabLayout.newTab().setText("คุยกับคนขาย"));


    }

    private void initListentnerTab() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    /**************
    *Listener Zone*
    ***************/



    /************
    *Inner Class*
    *************/
    public class TotalChatViewPagerAdapter extends FragmentPagerAdapter {
        public TotalChatViewPagerAdapter(FragmentManager fm) {
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

}
