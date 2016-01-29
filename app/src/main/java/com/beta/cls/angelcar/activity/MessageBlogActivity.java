package com.beta.cls.angelcar.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beta.cls.angelcar.Adapter.MessageBlogViewPagerAdapter;
import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageBlogActivity extends AppCompatActivity {
    @Bind(R.id.activity_chat_tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_blog);
        ButterKnife.bind(this);
        initTabLayout();
        initViewPager();
        initListentnerTab();


    }

    private void initViewPager() {
        MessageBlogViewPagerAdapter pagerAdapter = new MessageBlogViewPagerAdapter(getSupportFragmentManager());
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

}
