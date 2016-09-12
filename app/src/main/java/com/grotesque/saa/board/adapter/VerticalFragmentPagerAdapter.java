package com.grotesque.saa.board.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.grotesque.saa.board.SpecialFragment;
import com.grotesque.saa.home.data.DocumentList;

import java.util.ArrayList;

public class VerticalFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = VerticalFragmentPagerAdapter.class.getSimpleName();
    private ArrayList<DocumentList> mArrayList;
    private String moduleId;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView(((SpecialFragment) object).getView());    }

    public VerticalFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public VerticalFragmentPagerAdapter(FragmentManager fm, ArrayList<DocumentList> mArrayList, String moduleId) {
        super(fm);
        this.mArrayList = mArrayList;
        this.moduleId = moduleId;
    }

    @Override
    public Fragment getItem(int position) {
        return SpecialFragment.newInstance(moduleId, mArrayList.get(position));
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }
}