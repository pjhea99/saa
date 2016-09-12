package com.grotesque.saa.fixture;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.grotesque.saa.R;
import com.grotesque.saa.common.activity.BaseActivity;
import com.grotesque.saa.common.actionbar.BaseActionBar;

import static com.grotesque.saa.util.LogUtils.LOGE;

public class FixtureActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseActionBar.OnActionBarListener {
    private static final String TAG = FixtureActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private MyPagerAdapter mAdapter;
    private BaseActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture);

        mActionBar = (BaseActionBar) findViewById(R.id.actionBar);
        mViewPager = (ViewPager) findViewById(R.id.main_content);

        mActionBar.setOnActionBarListener(this);

        mAdapter = new MyPagerAdapter(getFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        onPageLoad(0);
    }

    private void onPageLoad(int position){
        LOGE(TAG, position + " onPageLoad");
        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_content);
        if(fragment != null) {

            ((FixtureFragment) fragment).onLoadFixture();
        }
        mActionBar.setTitleCount(position+1);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_FIXTURE;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onPageLoad(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTitleTextClicked() {

    }

    @Override
    public void onLeftButtonClicked() {
        if (!isNavDrawerOpen()) {
            openNavDrawer();
        }
    }

    @Override
    public void onRightButtonClicked() {

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 38;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return FixtureFragment.newInstance(position+1);
        }

    }
}
