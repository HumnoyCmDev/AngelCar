package com.beta.cls.angelcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 5/2/59.
 */
public class HelpFragment extends Fragment {

    @Bind(R.id.viewPagerHelp) ViewPager viewPager;

    public HelpFragment() {
        super();
    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }

        HelpPagerAdapter adapter = new HelpPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);


    }


    // pager Adapter
    private class HelpPagerAdapter extends FragmentStatePagerAdapter {

        int numPager = 5;

        public HelpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = ShowCaseFragment.newInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return numPager;
        }
    }

}
