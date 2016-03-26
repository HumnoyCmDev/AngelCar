package com.beta.cls.angelcar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.beta.cls.angelcar.fragment.PhotoFragment;
import com.beta.cls.angelcar.dao.PictureAllCollectionDao;

/**
 * Created by ABaD on 12/2/2015.
 */
public class PictureAdapter extends FragmentPagerAdapter {
    private PictureAllCollectionDao gao;

    public PictureAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setGao(PictureAllCollectionDao gao) {
        this.gao = gao;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(gao.getRows().get(position));
    }

    @Override
    public int getCount() {
        if (gao == null) return 0;
        if (gao.getRows() == null) return 0;
        return gao.getRows().size();
    }
}