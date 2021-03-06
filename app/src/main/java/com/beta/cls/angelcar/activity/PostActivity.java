package com.beta.cls.angelcar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.fragment.PostFragment;
import com.beta.cls.angelcar.fragment.BrandFragment;
import com.beta.cls.angelcar.fragment.CarDetailFragment;
import com.beta.cls.angelcar.fragment.CarTypeFragment;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.view.AngelCarViewPager;
import com.viewpagerindicator.LinePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 26/2/59. เวลา 10:43
 ***************************************/
public class PostActivity extends AppCompatActivity implements OnSelectData{
    private static final String TAG = "PostActivity";
    public static final int CALLBACK_BRAND = 1;
    public static final int CALLBACK_CAR_TYPE = 2;
    public static final int CALLBACK_CAR_TYPE_DETAIL = 3;
    public static final int CALLBACK_ALL_POST = 4;

    private int lastPosition = 0;

    @Bind(R.id.post_viewpager) AngelCarViewPager pager;
    @Bind(R.id.indicator) LinePageIndicator indicator;
    @Bind(R.id.btnNext) Button next;
    @Bind(R.id.btnPrevious) Button previous;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

            PostAdapterViewpager adapter =
                    new PostAdapterViewpager(getSupportFragmentManager());
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);

            pager.setPagingEnabled(false);
            previous.setEnabled(false);
            next.setEnabled(false);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                enabledButton(position,lastPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.btnNext,R.id.btnPrevious})
    public void next(View v){
        if (v.getId() == R.id.btnNext)
            pager.setCurrentItem(pager.getCurrentItem()+1);
        else
            pager.setCurrentItem(pager.getCurrentItem()-1);
        enabledButton(pager.getCurrentItem(),lastPosition);
    }

    private void enabledButton(int position,int lastPosition) {
        previous.setEnabled(position > 0);
        next.setEnabled(lastPosition > position);
        pager.setPagingEnabled(lastPosition > position);
    }
    @Override
    public void onSelectedCallback(int callback) {
        if (callback == CALLBACK_ALL_POST){
            finish();
            return;
        }

            pager.setCurrentItem(pager.getCurrentItem()+1);
            lastPosition = pager.getCurrentItem();
            enabledButton(pager.getCurrentItem(),lastPosition);
    }

    private class PostAdapterViewpager extends FragmentStatePagerAdapter{

        int numPager = 4;

        public PostAdapterViewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new BrandFragment();
                case 1: return new CarTypeFragment();
                case 2: return new CarDetailFragment();
                case 3: return new PostFragment();
                default:return new BrandFragment();
            }
        }

        @Override
        public int getCount() {
            return numPager;
        }
    }
}
