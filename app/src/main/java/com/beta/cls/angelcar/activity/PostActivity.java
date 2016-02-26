package com.beta.cls.angelcar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.fragment.BrandFragment;

/**
 * Created by humnoy on 26/2/59.
 */
public class PostActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);


    }

    class PostAdapterViewpager extends FragmentPagerAdapter{

        int numPager = 4;

        public PostAdapterViewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return BrandFragment.newInstance();
//                case 1: return
            }

            return null;
        }

        @Override
        public int getCount() {
            return numPager;
        }
    }
}
